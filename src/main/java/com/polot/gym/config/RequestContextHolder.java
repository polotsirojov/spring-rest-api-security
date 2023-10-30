package com.polot.gym.config;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public class RequestContextHolder {
    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();

    public static void setRequest(HttpServletRequest request) {
        requestHolder.set(request);
    }

    public static HttpServletRequest getRequest() {
        return requestHolder.get();
    }

    public static void clear() {
        requestHolder.remove();
    }

    public static String getTransactionId() {
        return Optional.ofNullable(getRequest().getAttribute("TransactionId")).orElse("").toString();
    }
}
