package com.example.rentservice.util.callbacks;

import com.example.rentservice.Server.POJO.Place.Order;

import java.util.ArrayList;

public interface GoToRoomCallback {
    public void goToRoom(ArrayList<Order> odata, int room_id);
}
