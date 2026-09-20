package com.cravedash.restaurantservice.dto;

public class RestaurantAvailabilityDTO {

    private Long restaurantId;
    private boolean available;

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}