package com.example.bs4bspringbackend.network.client;

import com.example.bs4bspringbackend.model.Physics.Vector2;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InputData {
    private Vector2 moveDirection;
    private Vector2 shootDirection; // shootDir
    private boolean shoot;
}
