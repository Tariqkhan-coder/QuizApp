package com.example.quizapp.models;

public class Category {
    private int id;
    private String name;
    private int imageResId; // To store drawable resource ID

    public Category() {}

    public Category(String name, int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getImageResId() { return imageResId; }
    public void setImageResId(int imageResId) { this.imageResId = imageResId; }
}
