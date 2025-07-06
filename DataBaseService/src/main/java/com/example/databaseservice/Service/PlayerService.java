package com.example.databaseservice.Service;

import com.example.databaseservice.Entity.Character;
import com.example.databaseservice.Entity.Player;
import com.example.databaseservice.Repository.CharacterRepository;
import com.example.databaseservice.Repository.PlayerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final CharacterRepository characterRepository;

    public PlayerService(PlayerRepository playerRepository, CharacterRepository characterRepository) {
        this.playerRepository = playerRepository;
        this.characterRepository = characterRepository;
    }

    @Transactional
    public Player createPlayer(String playerName) {
        return playerRepository.findByPlayerName(playerName)
                .orElseGet(() -> {
                    Player newPlayer = new Player();
                    newPlayer.setPlayerName(playerName);
                    Player savedPlayer = playerRepository.save(newPlayer);

                    Character defaultCharacter = characterRepository.findByName("Sheriff");
                    if (defaultCharacter != null) {
                        savedPlayer.getUnlockedCharacters().add(defaultCharacter);
                        savedPlayer = playerRepository.save(savedPlayer);
                    }

                    return savedPlayer;
                });
    }


    public Player getPlayerInfo(String playerName) {
        return playerRepository.findByPlayerName(playerName)
                .orElseThrow(() -> new RuntimeException("Player not found"));
    }

    public Player updatePlayerInfo(Player player) {
        return playerRepository.save(player);
    }

    @Transactional
    public Player unlockCharacter(int playerId, int characterId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("Character not found"));

        if (!player.getUnlockedCharacters().contains(character)) {
            player.getUnlockedCharacters().add(character);
            player = playerRepository.save(player);
        }
        return player;
    }
}