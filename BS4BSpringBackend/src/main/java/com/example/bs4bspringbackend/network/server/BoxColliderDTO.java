package com.example.bs4bspringbackend.network.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoxColliderDTO {
    public String id;
    public float x;
    public float y;
    public float width;
    public float height;
}
