package com.example.bs4bspringbackend.interfaces;

import com.example.bs4bspringbackend.model.GameRoom;

public interface RoomListener {
    void onRoomCreated(GameRoom room);

    void onRoomDestroyed(GameRoom room);
}