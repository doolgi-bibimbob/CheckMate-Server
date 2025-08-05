package com.seonlim.mathreview.aop;

import com.seonlim.mathreview.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
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

        MDC.put("ip", request.getRemoteAddr());

        Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(CustomUserDetails.class::isInstance)
                .map(CustomUserDetails.class::cast)
                .map(details -> details.getDomain().getId())
                .map(String::valueOf)
                .ifPresent(userId -> MDC.put("userId", userId));

        String method = request.getMethod();
        String uri = request.getRequestURI();

        String params = Stream.of(pjp.getArgs())
                .filter(arg -> !(arg instanceof HttpServletRequest) && arg != null)
                .map(Object::toString)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");

        log.info("➡️ [{} {}] params = {}", method, uri, params);

        long start = System.currentTimeMillis();

        try {
            Object result = pjp.proceed();
            log.info("⬅️ [{} {}] 응답 완료 ({} ms)", method, uri, System.currentTimeMillis() - start);
            return result;
        } catch (Exception ex) {
            log.error("❌ [{} {}] 예외 발생: {}", method, uri, ex.getMessage(), ex);
            throw ex;
        } finally {
            MDC.clear();
        }
    }
}
