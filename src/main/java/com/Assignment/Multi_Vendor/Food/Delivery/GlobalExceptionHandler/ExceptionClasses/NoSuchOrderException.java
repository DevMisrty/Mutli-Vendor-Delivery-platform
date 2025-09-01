package com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses;


public class NoSuchOrderException extends Exception {

    public NoSuchOrderException(String message) {
        super(message);
    }
}
