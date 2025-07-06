package com.example.bs4bspringbackend.service;


import com.example.bs4bspringbackend.interfaces.MatchmakingListener;
import com.example.bs4bspringbackend.interfaces.RoomListener;
import com.example.bs4bspringbackend.model.GameRoom;
import com.example.bs4bspringbackend.model.Weapon.Weapon;
import com.example.bs4bspringbackend.network.client.InputData;
import com.example.bs4bspringbackend.security.RateLimiterService;
import com.example.bs4bspringbackend.util.Broadcaster;
import com.example.bs4bspringbackend.util.JsonParser;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameManager implements MatchmakingListener, RoomListener {
    private static final Logger logger = LogManager.getLogger(GameManager.class);


    private final RoomService roomService;
    private final InputService inputService;
    private final GameEngineService gameEngineService;
    private final MatchmakingService matchmakingService;

    @Autowired
    private RateLimiterService rateLimiterService;

    @Autowired
    public GameManager(MatchmakingService matchmakingService, RoomService roomService, InputService inputService, GameEngineService gameEngineService) {
        this.matchmakingService = matchmakingService;
        this.roomService = roomService;
        this.inputService = inputService;
        this.gameEngineService = gameEngineService;
    }

    @PostConstruct
    public void init() {
        matchmakingService.setMatchmakingListener(this);
        roomService.setRoomListener(this);
        gameEngineService.setRoomListener(this);
    }

    public void onClientConnected(WebSocketSession session) {
        Broadcaster.sendWelcomeDTO(session);
    }

    public void onClientMessage(WebSocketSession session, TextMessage message){
        String payload = message.getPayload();

        if (payload.contains("ClientReadyDTO")) {
            matchmakingService.handleClientReady(session, payload);
        } else {
            InputData input = JsonParser.fromJson(payload, InputData.class);
            inputService.handleInput(session.getId(), input);
        }
    }

    public void onClientDisconnected(WebSocketSession session) {
        matchmakingService.removePlayerFromQueue(session);
        inputService.removeInput(session.getId());
        roomService.removePlayerFromRoom(session);
        rateLimiterService.removeSession(session.getId());
    }

    @Override
    public void onMatchFound(List<WebSocketSession> sessions, String level, List<String> characterIds, List<String> names, List<String> weapons) {
        roomService.createGameRoom(sessions, level, characterIds, names, weapons);
        logger.info("found Match for level {}", level);
    }

    @Override
    public void onRoomCreated(GameRoom room) {
        gameEngineService.startGameLoop(room);
        logger.info("Created room now starting loop");
    }

    @Override
    public void onRoomDestroyed(GameRoom room) {
        gameEngineService.stopGameLoop(room);
        roomService.destroyGameRoom(room.getRoomId());
        // remove input map
        List<String> playerSessionIds = room.getPlayers().stream()
                .map(player -> player.getSessionId())
                .collect(Collectors.toList());

        inputService.removeInputsForPlayers(playerSessionIds);
    }

}
