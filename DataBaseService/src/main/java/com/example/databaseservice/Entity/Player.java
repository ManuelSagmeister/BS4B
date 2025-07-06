package com.example.databaseservice.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "players")
@Getter
@Setter
@NoArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int playerId;

    @Column(nullable = false, unique = true)
    private String playerName;

    private int winCount;
    private int matchCount;
    private int coins;
    private int xp;

    @ManyToMany
    @JoinTable(
            name = "player_unlocked_characters",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "character_id")
    )
    private Set<Character> unlockedCharacters = new HashSet<>();

    public Player(int playerId, String playerName, int winCount, int matchCount, int coins, int xp) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.winCount = winCount;
        this.matchCount = matchCount;
        this.coins = coins;
        this.xp = xp;
        this.unlockedCharacters = new HashSet<>();
    }
}