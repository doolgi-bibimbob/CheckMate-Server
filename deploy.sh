#!/usr/bin/env bash
set -euo pipefail

# -----------------------------
# 설정
# -----------------------------
AWS_REGION="ap-northeast-2"
REPOSITORY_NAME="mathreview-app"
IMAGE_NAME="mathreview-app"

ACCOUNT_ID="$(aws sts get-caller-identity --query 'Account' --output text)"
ECR_URL="$ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$REPOSITORY_NAME"

# 고유 TAG (git commit‑hash + timestamp)
COMMIT_HASH="$(git rev-parse --short HEAD)"
BUILD_TS="$(date +%Y%m%d-%H%M%S)"
TAG="${COMMIT_HASH}-${BUILD_TS}"

BASTION_KEY=""
BASTION_HOST=""
PRIVATE_HOST=""

CONTAINER_NAME="mathreview-app"

# -----------------------------
# 1. ECR 로그인
# -----------------------------
echo "[1] ECR 로그인"
aws ecr get-login-password --region "$AWS_REGION" \
| docker login --username AWS --password-stdin "$ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com"

# -----------------------------
# 2. Docker 이미지 빌드 및 Push
# -----------------------------
echo "[2] Docker 이미지 빌드 및 ECR Push"
docker build --no-cache -t "$IMAGE_NAME:$TAG" .
docker tag  "$IMAGE_NAME:$TAG" "$ECR_URL:$TAG"
docker push "$ECR_URL:$TAG"

# -----------------------------
# 3. EC2에서 Pull & Run
# -----------------------------
echo "[3] Private EC2에서 Pull 및 Run"
ssh -i "$BASTION_KEY" ec2-user@"$BASTION_HOST" <<EOF
  set -euo pipefail
  ssh -i ~/mathreview-app.pem ec2-user@"$PRIVATE_HOST" <<INNER
    set -euo pipefail

    echo "[🔑] ECR 로그인"
    aws ecr get-login-password --region "$AWS_REGION" \
    | docker login --username AWS --password-stdin "$ECR_URL"

    echo "[📥] Docker 이미지 Pull"
    docker pull "$ECR_URL:$TAG"

    echo "[🧹] 기존 컨테이너 제거"
    docker rm -f "$CONTAINER_NAME" 2>/dev/null || true

    echo "[🚀] 새 컨테이너 실행"
    docker run -d --pull always --network ec2-user_default --name "$CONTAINER_NAME" -p 8080:8080 "$ECR_URL:$TAG"
INNER
EOF

echo "[✅] 배포 완료 – 태그 $TAG"
