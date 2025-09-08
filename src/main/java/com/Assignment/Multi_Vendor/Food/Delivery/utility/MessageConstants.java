package com.Assignment.Multi_Vendor.Food.Delivery.utility;

public class MessageConstants {

    // ===================== COMMON SUCCESS MESSAGES =====================
    public static final String SUCCESS = "Request processed successfully";
    public static final String CREATED = "Resource created successfully";
    public static final String UPDATED = "Resource updated successfully";
    public static final String DELETED = "Resource deleted successfully";
    public static final String FETCH_SUCCESS = "Data fetched successfully";

    // ===================== COMMON ERROR MESSAGES =====================
    public static final String INTERNAL_SERVER_ERROR = "Something went wrong. Please try again later";
    public static final String BAD_REQUEST = "Invalid request. Please check your input";
    public static final String UNAUTHORIZED = "You are not authorized to access this resource";
    public static final String FORBIDDEN = "You don't have permission to perform this action";
    public static final String NOT_FOUND = "Resource not found";
    public static final String CONFLICT = "Resource already exists";
    public static final String INVALID_INPUT = "Invalid input provided";

    // ===================== AUTHENTICATION & AUTHORIZATION =====================
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String LOGIN_FAILED = "Invalid username or password";
    public static final String LOGOUT_SUCCESS = "You have been logged out successfully";
    public static final String INVALID_CREDENTIALS = "Invalid login credentials";
    public static final String ACCESS_DENIED = "Access denied";
    public static final String TOKEN_EXPIRED = "Your session has expired. Please login again";
    public static final String TOKEN_INVALID = "Invalid authentication token";

    // ===================== USER MESSAGES =====================
    public static final String USER_CREATED = "User registered successfully";
    public static final String USER_UPDATED = "User details updated successfully";
    public static final String USER_DELETED = "User account deleted successfully";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_ALREADY_EXISTS = "User with this email already exists";

    // ===================== RESTAURANT MESSAGES =====================
    public static final String RESTAURANT_CREATED = "Restaurant added successfully";
    public static final String RESTAURANT_UPDATED = "Restaurant details updated successfully";
    public static final String RESTAURANT_DELETED = "Restaurant deleted successfully";
    public static final String RESTAURANT_APPROVED = "Restaurant approved successfully";
    public static final String RESTAURANT_REJECTED = "Restaurant rejected";
    public static final String RESTAURANT_NOT_FOUND = "No such restaurant found";
    public static final String RESTAURANT_ALREADY_EXISTS = "Restaurant already exists";
    public static final String ALL_RESTAURANTS_TO_APPROVE = "List of All restaurants to approve";
    public static final String NO_RESTAURANTS_TO_APPROVE = "There are ZERO Restaurants to approve";

    // ===================== DISHES / MENU MESSAGES =====================
    public static final String DISH_ADDED = "Dish added to the menu successfully";
    public static final String DISH_UPDATED = "Dish updated successfully";
    public static final String DISH_DELETED = "Dish removed from the menu";
    public static final String DISH_NOT_FOUND = "Dish not found";
    public static final String MENU_FETCH_SUCCESS = "Menu fetched successfully";
    public static final String MENU_EMPTY = "This restaurant currently has no dishes";

    // ===================== ORDER MESSAGES =====================
    public static final String ORDER_PLACED = "Order placed successfully";
    public static final String ORDER_UPDATED = "Order updated successfully";
    public static final String ORDER_CANCELLED = "Order cancelled successfully";
    public static final String ORDER_COMPLETED = "Order completed successfully";
    public static final String ORDER_NOT_FOUND = "Order not found";
    public static final String ORDER_ACCESS_DENIED = "You are not authorized to modify this order";
    public static final String ORDER_DETAILS = "Following are the details related to orders";

    // ===================== PAYMENT MESSAGES =====================
    public static final String PAYMENT_SUCCESS = "Payment completed successfully";
    public static final String PAYMENT_FAILED = "Payment failed";
    public static final String PAYMENT_PENDING = "Payment is pending";
    public static final String INVALID_PAYMENT_METHOD = "Invalid payment method selected";

    // ===================== OTP / EMAIL / NOTIFICATION =====================
    public static final String OTP_SENT = "OTP has been sent to your email";
    public static final String OTP_VERIFIED = "OTP verified successfully";
    public static final String OTP_INVALID = "Invalid OTP entered";
    public static final String OTP_EXPIRED = "OTP has expired";
    public static final String EMAIL_SENT = "Email sent successfully";
    public static final String EMAIL_FAILED = "Failed to send email";

    // ===================== VALIDATION MESSAGES =====================
    public static final String FIELD_REQUIRED = "This field is required";
    public static final String INVALID_EMAIL = "Invalid email format";
    public static final String PASSWORD_TOO_WEAK = "Password must be at least 8 characters long";
    public static final String PASSWORD_MISMATCH = "Passwords do not match";

    // ===================== ADMIN / DASHBOARD MESSAGES =====================
    public static final String ADMIN_ACCESS_GRANTED = "Admin access granted";
    public static final String ADMIN_ACCESS_DENIED = "Admin access denied";
    public static final String DASHBOARD_DATA_FETCHED = "Dashboard data fetched successfully";

    public static final String MENU_ADDED = "New Menu has been Added";
}
