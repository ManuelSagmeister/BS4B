package com.example.bs4bspringbackend.util;

import com.example.bs4bspringbackend.model.GameRoom;
import com.example.bs4bspringbackend.network.server.GameOverDTO;
import com.example.bs4bspringbackend.network.server.GameRoomDTO;
import com.example.bs4bspringbackend.network.server.PlayerLostDTO;
import com.example.bs4bspringbackend.network.server.WelcomeDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

public class Broadcaster {

    // Private constructor to prevent instantiation
    private Broadcaster() {
        throw new UnsupportedOperationException("Broadcaster is a utility class and cannot be instantiated.");
    }

    private static final Logger logger = LogManager.getLogger(Broadcaster.class);

    public static void sendWelcomeDTO(WebSocketSession session){
        WelcomeDTO welcomeDTO = new WelcomeDTO(null, session.getId());
        String json = JsonParser.toJson(welcomeDTO);
        try {
            if (session.isOpen()) {
                assert json != null;
                session.sendMessage(new TextMessage(json));
            } else {
                logger.warn("WebSocket session {} is not open. Skipping WelcomeDTO.", session.getId());
            }
        } catch (Exception e) {
            logger.error("Could not send WelcomeDTO to session {}", session.getId(), e);
        }
    }

    // == EngineService ==

    public static void sendGameRoomDTO(List<WebSocketSession> sessions, GameRoom room) {
        try{

            GameRoomDTO dto = DTOMapper.mapGameRoomToDTO(room);
            String json = JsonParser.toJson(dto);

            for(WebSocketSession session : sessions) {
                if(session.isOpen()) {
                    assert json != null;
                    session.sendMessage(new TextMessage(json));
                }
            }

        } catch (Exception e) {
            logger.error("Could not broadcast GameRoomDTO", e);
        }
    }

    public static void sendPlayerLost(List<WebSocketSession> sessions, String sessionId, int xp, int coins) {
        try {
            PlayerLostDTO dto = new PlayerLostDTO(sessionId, xp, coins);
            String json = JsonParser.toJson(dto);
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    assert json != null;
                    session.sendMessage(new TextMessage(json));
                } else {
                    logger.warn("Skipping closed session {} when sending PlayerLostDTO", session.getId());
                }
            }
        } catch (Exception e) {
            logger.error("Could not broadcast PlayerLostDTO", e);
        }
    }

    public static void sendGameOver(List<WebSocketSession> sessions, String winnerSessionId, int xp, int coins) {
        try {
            GameOverDTO dto = new GameOverDTO(winnerSessionId, xp, coins);
            String json = JsonParser.toJson(dto);
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    assert json != null;
                    session.sendMessage(new TextMessage(json));
                } else {
                    logger.warn("Skipping closed session {} when sending GameOverDTO", session.getId());
                }
            }
        } catch (Exception e) {
            logger.error("Could not broadcast GameOverDTO", e);
        }
    }
}
