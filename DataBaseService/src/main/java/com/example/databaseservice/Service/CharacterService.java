package com.example.databaseservice.Service;

import com.example.databaseservice.Entity.Character;
import com.example.databaseservice.Entity.Player;
import com.example.databaseservice.Repository.CharacterRepository;
import com.example.databaseservice.Repository.PlayerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final PlayerRepository playerRepository;

    @Autowired
    public CharacterService(CharacterRepository characterRepository, PlayerRepository playerRepository) {
        this.characterRepository = characterRepository;
        this.playerRepository = playerRepository;
    }

    @Transactional
    public Player unlockCharacter(String playerName, int characterId) {
        Player player = playerRepository.findByPlayerName(playerName)
                .orElseThrow(() -> new RuntimeException("❌ Player '" + playerName + "' not found"));

        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("❌ Character with ID '" + characterId + "' not found"));

        if (player.getUnlockedCharacters().contains(character)) {
            throw new RuntimeException("⚠️ Character already unlocked by this player");
        }

        if (player.getCoins() < character.getPrice()) {
            throw new RuntimeException("💰 Not enough coins to unlock '" + character.getName() + "'");
        }

        player.setCoins(player.getCoins() - character.getPrice());
        player.getUnlockedCharacters().add(character);

        return playerRepository.save(player);
    }


    public Character createCharacter(Character character) {
        return characterRepository.save(character);
    }
}