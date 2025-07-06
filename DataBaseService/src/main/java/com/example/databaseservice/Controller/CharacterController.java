package com.example.databaseservice.Controller;

import com.example.databaseservice.Entity.Character;
import com.example.databaseservice.Entity.Player;
import com.example.databaseservice.Service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/character")
@CrossOrigin(origins = "*")
public class CharacterController {

    private final CharacterService characterService;

    @Autowired
    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @PostMapping("/create")
    public Character createCharacter(@RequestBody Character character) {
        return characterService.createCharacter(character);
    }

    @PostMapping("/unlock")
    public Player unlockCharacter(
            @RequestParam String playerName,
            @RequestParam int characterId) {
        return characterService.unlockCharacter(playerName, characterId);
    }


}