package com.example.bs4bspringbackend.network.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerLostDTO {
    public final String type = "PlayerLost";
    public String sessionId;
    public int xp;
    public int coins;
}