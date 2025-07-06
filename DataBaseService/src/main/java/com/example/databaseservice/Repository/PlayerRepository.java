package com.example.databaseservice.Repository;

import com.example.databaseservice.Entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
    Optional<Player> findByPlayerName(String playerName);
}