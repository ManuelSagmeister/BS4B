package com.example.bs4bspringbackend.network.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameOverDTO {
    public final String type = "GameOver";
    public String winnerSessionId;
    public int xp;
    public int coins;

}
