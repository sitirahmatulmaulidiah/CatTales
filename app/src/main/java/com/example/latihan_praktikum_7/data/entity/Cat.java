package com.example.latihan_praktikum_7.data.entity;

public class Cat {
    private int id;
    private String name;
    private String origin;
    private String description;

    public Cat(int id, String name, String origin, String description) {
        this.id = id;
        this.name = name;
        this.origin = origin;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrigin() { return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
