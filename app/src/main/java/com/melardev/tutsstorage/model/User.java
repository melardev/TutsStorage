package com.melardev.tutsstorage.model;

/**
 * Created by melardev on 5/6/2017.
 */

public class User {
    public String name;
    private int id;
    private int time;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTime() {
        return time;
    }
}
