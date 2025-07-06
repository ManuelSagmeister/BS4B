package com.example.bs4bspringbackend.security;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    private final Map<String, Long> lastMessageTimestamps = new ConcurrentHashMap<>();
    private final Map<String, Integer> messageCounts = new ConcurrentHashMap<>();
    private final int maxMessagesPerSecond = 20;

    public boolean allowMessage(String sessionId) {
        long now = System.currentTimeMillis() / 1000L; // current second
        lastMessageTimestamps.putIfAbsent(sessionId, now);
        messageCounts.putIfAbsent(sessionId, 0);

        if (lastMessageTimestamps.get(sessionId) == now) {
            // Same second → increment counter
            int count = messageCounts.compute(sessionId, (k, v) -> v + 1);
            return count <= maxMessagesPerSecond;
        } else {
            // New second → reset
            lastMessageTimestamps.put(sessionId, now);
            messageCounts.put(sessionId, 1);
            return true;
        }
    }

    public void removeSession(String sessionId) {
        lastMessageTimestamps.remove(sessionId);
        messageCounts.remove(sessionId);
    }
}
