package com.example.bs4bspringbackend.model.Weapon;

public class WeaponFactory {

    // Private constructor to prevent instantiation
    private WeaponFactory() {
        throw new UnsupportedOperationException("WeaponFactory is a utility class and cannot be instantiated.");
    }

    public static Weapon createWeapon(String name) {
        return switch (name){
            case "Revolver" -> new Revolver();
            case "Shotgun" -> new Shotgun();
            case "DoubleActionRevolver" -> new DoubleActionRevolver();
            case "CardThrower" -> new CardThrower();
            default -> throw new IllegalArgumentException("Unknown weapon type detected: " + name);
        };
    }
}
