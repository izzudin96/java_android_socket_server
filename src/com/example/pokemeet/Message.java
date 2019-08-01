package com.example.pokemeet;

import java.io.Serializable;

public class Message implements Serializable {
    private final int id;
    private final User user;
    private final RaidRoom room;
    private final String message;
    private static int count = 0;
    private static final long serialVersionUID = 2147483649L;

    public Message(User user, RaidRoom room, String message) {
        this.user = user;
        this.room = room;
        this.message = message;
        count++;
        this.id = count;
    }
}
