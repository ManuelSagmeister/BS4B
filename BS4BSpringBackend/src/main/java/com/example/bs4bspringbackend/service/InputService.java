package com.example.bs4bspringbackend.service;

import com.example.bs4bspringbackend.network.client.InputData;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InputService {

    private final Map<String, InputData> inputMap = new ConcurrentHashMap<>();

    @Getter
    private Map<String, Boolean> hasShotLastFrame = new ConcurrentHashMap<>();


    public void handleInput(String session, InputData input) {
        inputMap.put(session, input);
    }

    public InputData getInputBySessionId(String sessionId) {
        return inputMap.get(sessionId);
    }

    public void removeInput(String sessionId) {
        inputMap.remove(sessionId);
    }

    public void removeInputsForPlayers(Iterable<String> sessionIds) {
        for (String sessionId : sessionIds) {
            inputMap.remove(sessionId);
            hasShotLastFrame.remove(sessionId);
        }
    }



}
