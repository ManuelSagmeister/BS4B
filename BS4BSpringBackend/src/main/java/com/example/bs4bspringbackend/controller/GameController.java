package com.example.bs4bspringbackend.controller;

import com.example.bs4bspringbackend.service.GameManager;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class GameController extends TextWebSocketHandler{

    private final GameManager gameManager;

    public GameController(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        gameManager.onClientConnected(session);
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) {
        gameManager.onClientMessage(session, message);

    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session,@NonNull CloseStatus status) {
        gameManager.onClientDisconnected(session);
    }


}