package com.example.food_delivery_app.model;

import java.io.Serializable;

public class Rating implements Serializable {
    private String foodName;
    private String imageUrl;
    private float rating;
    private String comment;
    private String reviewer;
    private String createAt;

    public Rating() {
    }

    public Rating(String foodName, String imageUrl, float rating, String comment, String reviewer, String createAt) {
        this.foodName = foodName;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.comment = comment;
        this.reviewer = reviewer;
        this.createAt = createAt;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }
}
