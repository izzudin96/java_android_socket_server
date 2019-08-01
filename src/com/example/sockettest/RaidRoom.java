package com.example.sockettest;

import java.io.Serializable;
import java.util.ArrayList;

public class RaidRoom implements Serializable {
    private final int id;
    private final String time;
    private static int count = 0;
    private final String location;
    private static final long serialVersionUID = 2147483649L;

    private User admin = null;
    public ArrayList<User> userList = new ArrayList<>();
    public ArrayList<Message> messagesList = new ArrayList<>();

    public RaidRoom(String location, String time) {
        this.location = location;
        this.time = time;
        count++;
        this.id = count;
    }

    public int getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public void addParticipant(User user) {
        userList.add(user);
    }

    public void addMessage(Message message) {
        messagesList.add(message);
    }

    @Override
    public String toString() {
        return "Room ID: " + getId() + " || Location: " + getLocation() + " || Time: " + getTime();
    }
}