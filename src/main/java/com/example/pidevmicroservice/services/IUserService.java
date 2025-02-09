package com.example.pidevmicroservice.services;

import com.example.pidevmicroservice.entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IUserService {
    public User signup(User user, MultipartFile image) throws IOException;
}
