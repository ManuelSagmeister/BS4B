package com.example.bs4bspringbackend.network.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WelcomeDTO {
    private final String type = "WelcomeDTO";
    private String roomId;
    private String sessionId;
}
