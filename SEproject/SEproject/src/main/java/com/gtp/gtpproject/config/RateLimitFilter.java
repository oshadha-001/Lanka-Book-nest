package com.gtp.gtpproject.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {
    private static final int WINDOW_SECONDS = 60;
    private static final int MAX_REQUESTS = 30;

    private final Map<String, Window> windows = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        if ("/api/books/search".equals(path)) {
            String key = request.getRemoteAddr();
            Window window = windows.computeIfAbsent(key, k -> new Window());
            long now = Instant.now().getEpochSecond();
            synchronized (window) {
                if (now - window.start >= WINDOW_SECONDS) {
                    window.start = now;
                    window.count = 0;
                }
                window.count++;
                if (window.count > MAX_REQUESTS) {
                    response.setStatus(429);
                    response.getWriter().write("Too Many Requests");
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private static class Window {
        long start = Instant.now().getEpochSecond();
        int count = 0;
    }
}

