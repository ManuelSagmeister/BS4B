package com.example.bs4bspringbackend.config;

import com.example.bs4bspringbackend.service.GameManager;
import com.example.bs4bspringbackend.service.MatchmakingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.example.bs4bspringbackend.controller.GameController;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

 private final GameManager gameManager;

    @Autowired
    public WebSocketConfig(GameManager gameManager) {
        this.gameManager = gameManager;
    }


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(gameController(), "/ws/game").setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler gameController() {
        return new GameController(gameManager);
    }
    

}
