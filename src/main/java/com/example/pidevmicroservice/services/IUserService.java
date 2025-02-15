package com.example.pidevmicroservice.services;

import com.example.pidevmicroservice.entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IUserService {
    public User signup(User user, MultipartFile image) throws IOException;
    public List<User> getAllUser();
    public User getUserById(String cin );
    public User updateUser(String cin, User newUser);
    public String deleteUser(String cin);
    public User desactivateUser(String cin);
    public User activateUser(String cin);
  public void  logoutFromKeycloak(String userId);
}
