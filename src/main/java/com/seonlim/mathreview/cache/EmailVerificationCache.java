package com.seonlim.mathreview.cache;

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
        long now = System.currentTimeMillis();

        return Optional.ofNullable(codeStore.get(email))
                .filter(code -> code.equals(input))
                .flatMap(code ->
                        Optional.ofNullable(timestamps.get(email))
                                .filter(t -> now - t <= EXPIRE_MS)
                                .map(t -> {
                                    verifiedStore.put(email, true);
                                    invalidate(email);
                                    return true;
                                })
                )
                .or(() -> {
                    Optional.ofNullable(timestamps.get(email))
                            .filter(t -> now - t > EXPIRE_MS)
                            .ifPresent(t -> invalidate(email));
                    return Optional.of(false);
                })
                .get();
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
