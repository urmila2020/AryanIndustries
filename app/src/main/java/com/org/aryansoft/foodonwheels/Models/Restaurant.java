package com.org.aryansoft.foodonwheels.Models;

public class Restaurant {
    private String restaurantId;
    private String restaurantName;
    private String restaurantImage;
    private String restaurantDescription;
    private double rating;
    private boolean isVegOnly;
    private boolean isClosed;

    public Restaurant(String restaurantId, String restaurantName, String restaurantImage, String restaurantDescription, double rating, boolean isVegOnly, boolean isClosed) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.restaurantImage = restaurantImage;
        this.restaurantDescription = restaurantDescription;
        this.rating = rating;
        this.isVegOnly = isVegOnly;
        this.isClosed = isClosed;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantImage() {
        return restaurantImage;
    }

    public void setRestaurantImage(String restaurantImage) {
        this.restaurantImage = restaurantImage;
    }

    public String getRestaurantDescription() {
        return restaurantDescription;
    }

    public void setRestaurantDescription(String restaurantDescription) {
        this.restaurantDescription = restaurantDescription;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public boolean isVegOnly() {
        return isVegOnly;
    }

    public void setVegOnly(boolean vegOnly) {
        isVegOnly = vegOnly;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "restaurantId='" + restaurantId + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", restaurantImage='" + restaurantImage + '\'' +
                ", restaurantDescription='" + restaurantDescription + '\'' +
                ", rating=" + rating +
                ", isVegOnly=" + isVegOnly +
                ", isClosed=" + isClosed +
                '}';
    }
}
