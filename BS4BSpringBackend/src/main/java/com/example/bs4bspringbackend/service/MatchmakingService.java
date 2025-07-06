package com.example.bs4bspringbackend.service;

import com.example.bs4bspringbackend.interfaces.MatchmakingListener;
import com.example.bs4bspringbackend.model.GameRoom;
import com.example.bs4bspringbackend.network.client.ClientReadyDTO;
import com.example.bs4bspringbackend.network.server.WelcomeDTO;
import com.example.bs4bspringbackend.util.JsonParser;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.*;

@Service
public class MatchmakingService {
    public static final int MAX_PLAYERS = 4;
    private final Set<String> readyClients = ConcurrentHashMap.newKeySet();
    private final Map<String, Queue<WebSocketSession>> levelToQueue = new ConcurrentHashMap<>();
    private final Map<String, ClientReadyDTO> sessionToClientReadyDTO = new ConcurrentHashMap<>();

    @Setter
    private MatchmakingListener matchmakingListener;

    public void handleClientReady(WebSocketSession session, String payload) {
        ClientReadyDTO readyDTO = JsonParser.fromJson(payload, ClientReadyDTO.class);
        String level = readyDTO.getSelectedLevel();

        readyClients.add(session.getId());
        sessionToClientReadyDTO.put(session.getId(), readyDTO);
        levelToQueue.computeIfAbsent(level, k -> new ConcurrentLinkedQueue<>()).add(session);
        tryMatchmake(level);

    }

    private void tryMatchmake(String level) {
        Queue<WebSocketSession> queue = levelToQueue.get(level);
        while (queue != null && queue.size() >= MAX_PLAYERS) {
            List<WebSocketSession> players = new ArrayList<>();
            List<String> characterIds = new ArrayList<>();
            List<String> names = new ArrayList<>();
            List<String> weaponNames = new ArrayList<>();

            for (int i = 0; i < MAX_PLAYERS; i++) {
                WebSocketSession playerSession = queue.poll();
                if (playerSession == null || !playerSession.isOpen()) {
                    continue;
                }
                ClientReadyDTO dto = sessionToClientReadyDTO.get(playerSession.getId());
                players.add(playerSession);
                characterIds.add(dto.getSelectedCharacterId());
                names.add(dto.getPlayerName());
                weaponNames.add(dto.getSelectedWeaponName());
            }
            if (matchmakingListener != null) {
                matchmakingListener.onMatchFound(players, level, characterIds, names, weaponNames);
            }
        }
    }


    public void removePlayerFromQueue(WebSocketSession session) {
        ClientReadyDTO dto = sessionToClientReadyDTO.remove(session.getId());
        if (dto != null) {
            String level = dto.getSelectedLevel();
            Queue<WebSocketSession> queue = levelToQueue.get(level);
            if (queue != null) {
                queue.remove(session);
            }
        }
        readyClients.remove(session.getId());
    }

}
