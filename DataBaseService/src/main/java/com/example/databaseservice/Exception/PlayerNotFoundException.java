package com.example.databaseservice.Exception;

public class PlayerNotFoundException extends RuntimeException {
  public PlayerNotFoundException(String playerName) {
    super("Player not found: " + playerName);
  }
}

