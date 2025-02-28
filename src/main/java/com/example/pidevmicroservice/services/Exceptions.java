package com.example.pidevmicroservice.services;

public class Exceptions {
    private Exceptions(){}
    // Custom exception when a user already exists
    public static class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }

    // Custom exception when the user is not found
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
    public static class EmailAlreadyExists extends RuntimeException {
        public EmailAlreadyExists(String message) {
            super(message);
        }
    }
    // Custom exception for image upload errors
    public static class ImageUploadException extends RuntimeException {
        public ImageUploadException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    public static class InvalidPasswordException extends RuntimeException{
        public InvalidPasswordException(String message){
            super(message);
        }
    }

}
