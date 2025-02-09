package com.example.pidevmicroservice.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.pidevmicroservice.entities.User;
import com.example.pidevmicroservice.enums.UserRole;
import com.example.pidevmicroservice.repositories.UserRepository;
import com.example.pidevmicroservice.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private Cloudinary getCloudinaryInstance() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dmwttu9lu",
                "api_key", "974645429429234",
                "api_secret", "XIhfcEzguJ_ZcZ1RDaD9am8r4bU"
        ));
    }
    private String uploadImageToCloud(MultipartFile image) throws IOException {
        Cloudinary cloudinary = getCloudinaryInstance();
        Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("url").toString();
    }
    @Override
    public User signup(User user, MultipartFile image) throws IOException {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("L'email existe déjà.");
        }
        if (userRepository.existsById(user.getCin())) {
            throw new RuntimeException("Un utilisateur avec ce CIN existe déjà.");
        }
            if (image != null && !image.isEmpty()) {
                String imageUrl = uploadImageToCloud(image);
                user.setImage(imageUrl);
            }
          user.setUserRole(UserRole.CUSTOMER);
       return userRepository.save(user);

    }
}
