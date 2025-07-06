package com.example.bs4bspringbackend.network.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientReadyDTO {
    private final String type = "ClientReadyDTO";
    private String playerName;
    private String sessionId;
    private String roomId;
    private String selectedLevel;
    private String selectedCharacterId;
    private String selectedWeaponName;
}
