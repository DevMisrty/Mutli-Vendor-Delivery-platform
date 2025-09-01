package com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses;

public class RestaurantAccessDeniedException extends Exception{
    public RestaurantAccessDeniedException(String message) {
        super(message);
    }
    public RestaurantAccessDeniedException() {
        super("You are not authorized to access this restaurant.");
    }
}
