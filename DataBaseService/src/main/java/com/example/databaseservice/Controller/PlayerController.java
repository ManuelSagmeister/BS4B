package com.example.databaseservice.Controller;

import com.example.databaseservice.Entity.Player;
import com.example.databaseservice.Exception.PlayerNotFoundException;
import com.example.databaseservice.Service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class PlayerController {
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/createPlayer")
    public Player createPlayer(@RequestBody Map<String, String> request) {
        String playerName = request.get("playerName");
        return playerService.createPlayer(playerName);
    }


    @CrossOrigin(origins = "*")
    @GetMapping("/getPlayerInfo/{playerName}")
    public Player getPlayerInfo(@PathVariable("playerName") String playerName) {
        Player player = playerService.getPlayerInfo(playerName);
        if (player != null) {
            return player;
        } else {
            throw new PlayerNotFoundException(playerName);
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/updatePlayer")
    public Player updatePlayerInfo(@RequestBody Player player) {
        return playerService.updatePlayerInfo(player);
    }
}