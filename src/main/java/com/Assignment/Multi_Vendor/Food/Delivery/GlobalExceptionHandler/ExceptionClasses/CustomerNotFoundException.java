package com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses;

public class CustomerNotFoundException extends Exception{
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
