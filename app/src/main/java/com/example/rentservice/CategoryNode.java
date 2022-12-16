package com.example.rentservice;

import android.graphics.Bitmap;

public class CategoryNode {
    private Bitmap image;
    private String title;
    private int Id;

    public CategoryNode(int Id, Bitmap image, String title){
        this.Id= Id;
        this.image = image;
        this.title = title;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

