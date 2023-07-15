package com.example.myapplication.data.users;

public class User {

    private final long id;
    private final int points;

    public User(long id, int points) {
        this.id = id;
        this.points = points;
    }

    public long getId() {
        return id;
    }

    public int getPoints() {
        return points;
    }
}
