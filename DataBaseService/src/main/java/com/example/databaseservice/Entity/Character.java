package com.example.databaseservice.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int characterId;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;
    private int price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Character)) return false;
        Character that = (Character) o;
        return characterId == that.characterId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(characterId);
    }
}