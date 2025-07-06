package com.example.bs4bspringbackend.interfaces;

import com.example.bs4bspringbackend.model.Weapon.Weapon;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

public interface MatchmakingListener {
    void onMatchFound(List<WebSocketSession> sessions, String level, List<String> characterIds, List<String> names, List<String> weapons);
}
