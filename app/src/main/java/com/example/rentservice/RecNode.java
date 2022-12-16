package com.example.rentservice;

import android.graphics.Bitmap;

import androidx.constraintlayout.widget.ConstraintLayout;

public class RecNode {
    private Bitmap image;
    private String title, subtitle, description;
    private Boolean isExpanded;
    ConstraintLayout.LayoutParams params;

    public RecNode (Bitmap image, String title, String subtitle, String description){
        this.description = description;
        this.image = image;
        this.isExpanded = false;
        this.subtitle = subtitle;
        this.title = title;
    }

    public void setParams(ConstraintLayout.LayoutParams params) {
        this.params = params;
    }

    public ConstraintLayout.LayoutParams getParams() {
        return params;
    }

    public String getTitle() {
        return title;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setExpanded(Boolean expanded) {
        isExpanded = expanded;
    }

    public Boolean isExpanded() {
        return isExpanded;
    }
}
