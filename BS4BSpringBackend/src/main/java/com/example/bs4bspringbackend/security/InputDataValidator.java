package com.example.bs4bspringbackend.security;

import com.example.bs4bspringbackend.network.client.InputData;

public class InputDataValidator {

    public static boolean isValid(InputData input) {
        if (input == null) return false;

        // Validate moveDirection is within [-1,1]
        if (Math.abs(input.getMoveDirection().getX()) > 1.0f ||
                Math.abs(input.getMoveDirection().getY()) > 1.0f) {
            return false;
        }

        // Validate shootDirection is normalized or zero
        float length = input.getShootDirection().Magnitude();
        if (length > 1.01f) return false;

        return true;
    }
}
