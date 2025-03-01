package com.example.pidevmicroservice.dto;

import lombok.Data;

@Data
public class DeleteUserRequest {
    private String cin;
    private String oldPassword;
}
