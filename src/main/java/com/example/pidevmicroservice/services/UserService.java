package com.example.pidevmicroservice.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.pidevmicroservice.entities.User;
import com.example.pidevmicroservice.entities.VerificationToken;
import com.example.pidevmicroservice.enums.UserRole;
import com.example.pidevmicroservice.repositories.TokenRepository;
import com.example.pidevmicroservice.repositories.UserRepository;
import com.example.pidevmicroservice.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
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
    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
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
            user.setCreationDate(LocalDateTime.now());
        user.setVerified(false);
        User savedUser = userRepository.save(user);

        // Generate OTP
        String otp = generateOtp();

        // Create token with expiry (e.g., 15 minutes from now)
        VerificationToken token = new VerificationToken();
        token.setToken(otp);
        token.setUser(savedUser);
        token.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        tokenRepository.save(token);

        // Send OTP email
        emailService.sendOtpEmail(savedUser.getEmail(), otp);
       return userRepository.save(user);

    }
    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(String cin) {
        return userRepository.findById(cin).orElse(null);
    }
    @Override
    public User updateUser(String cin, User newUser) {
        if (userRepository.findById(cin).isPresent()) {
            User existingUser = userRepository.findById(cin).get();
            existingUser.setName(newUser.getName());
            existingUser.setEmail(newUser.getEmail());
            existingUser.setLocation(newUser.getLocation());
            existingUser.setPassword(newUser.getPassword());
            existingUser.setVerified(true);

            return userRepository.save(existingUser);
        } else
            return null;
    }

    @Override
    public String deleteUser(String cin) {
        if (userRepository.findById(cin).isPresent()) {
            userRepository.deleteById(cin);
            return "utilisateur supprimé";
        } else
            return "utilisateur non supprimé";
    }
}
