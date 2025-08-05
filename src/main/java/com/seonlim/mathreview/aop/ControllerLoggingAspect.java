package com.seonlim.mathreview.aop;

import com.seonlim.mathreview.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;
import java.util.stream.Stream;

@Aspect
@Component
@Slf4j
public class ControllerLoggingAspect {

    @Pointcut("execution(* com.seonlim.mathreview.controller..*(..))")
    public void controllerMethods() {}

    @Around("controllerMethods()")
    public Object logController(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest)
                .orElse(null);

        if (request == null) return pjp.proceed();

        // MDC 저장
        String ip = request.getRemoteAddr();
        MDC.put("ip", ip);

        Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(auth -> auth.isAuthenticated() && auth.getPrincipal() instanceof CustomUserDetails)
                .map(Authentication::getPrincipal)
                .map(CustomUserDetails.class::cast)
                .map(details -> details.getDomain().getId())
                .map(String::valueOf)
                .ifPresent(userId -> MDC.put("userId", userId));

        String userId = MDC.get("userId");
        String method = request.getMethod();
        String uri = request.getRequestURI();

        String params = Stream.of(pjp.getArgs())
                .filter(arg -> arg != null &&
                        !(arg instanceof HttpServletRequest) &&
                        !(arg instanceof HttpServletResponse))
                .map(Object::toString)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");

        log.info("➡️ [{} {}] [userId={}] [ip={}] request = {}", method, uri, userId, ip, params);

        long start = System.currentTimeMillis();

        try {
            Object result = pjp.proceed();
            log.info("⬅️ [{} {}] [userId={}] [ip={}] 응답 완료 ({} ms)", method, uri, userId, ip, System.currentTimeMillis() - start);
            return result;
        } catch (Exception ex) {
            log.error("❌ [{} {}] [userId={}] [ip={}] 예외 발생: {}", method, uri, userId, ip, ex.getMessage(), ex);
            throw ex;
        } finally {
            MDC.clear();
        }
    }
}

