package com.seonlim.mathreview.user.cache;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EmailVerificationCache {
    private final Map<String, String> codeStore = new ConcurrentHashMap<>();
    private final Map<String, Boolean> verifiedStore = new ConcurrentHashMap<>();
    private final Map<String, Long> timestamps = new ConcurrentHashMap<>();
    private static final long EXPIRE_MS = 5 * 60 * 1000;

    public void saveCode(String email, String code) {
        codeStore.put(email, code);
        timestamps.put(email, System.currentTimeMillis());
    }

    public boolean verify(String email, String input) {
        return Optional.ofNullable(codeStore.get(email))
                .filter(code -> code.equals(input))
                .filter(code -> Optional.ofNullable(timestamps.get(email))
                        .filter(t -> System.currentTimeMillis() - t <= EXPIRE_MS)
                        .isPresent()
                )
                .map(code -> {
                    verifiedStore.put(email, true);
                    return true;
                })
                .orElseGet(() -> {
                    invalidate(email);
                    return false;
                });
    }

    public boolean isVerified(String email) {
        return verifiedStore.getOrDefault(email, false);
    }

    public void invalidate(String email) {
        verifiedStore.remove(email);
        codeStore.remove(email);
        timestamps.remove(email);
    }
}
