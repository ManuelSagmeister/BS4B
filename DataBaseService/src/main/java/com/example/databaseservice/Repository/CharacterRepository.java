package com.example.databaseservice.Repository;

import com.example.databaseservice.Entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Integer> {
    Character findByName(String name);
}