package com.example.pidevmicroservice.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.pidevmicroservice.entities.User;
import com.example.pidevmicroservice.entities.VerificationToken;
import com.example.pidevmicroservice.enums.UserRole;
import com.example.pidevmicroservice.repositories.TokenRepository;
import com.example.pidevmicroservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private     Random random = new Random();
    private String generateOtp() {

        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
    @Override
    public User signup(User user, MultipartFile image) throws IOException {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new Exceptions.EmailAlreadyExists("L'email existe déjà.");
        }
        if (userRepository.existsById(user.getCin())) {
            throw new Exceptions.UserAlreadyExistsException("Un utilisateur avec ce CIN existe déjà.");
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
        Optional<User> existingUserOpt = userRepository.findById(cin);
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            existingUser.setName(newUser.getName());
            existingUser.setEmail(newUser.getEmail());
            existingUser.setLocation(newUser.getLocation());
            existingUser.setPassword(newUser.getPassword());
            // Only update the image if a new one is provided
            if (newUser.getImage() != null) {
                existingUser.setImage(newUser.getImage());
            }

            return userRepository.save(existingUser);
        } else {
            throw new Exceptions.UserNotFoundException("User with CIN " + cin + " not found");
        }
    }


    @Override
    public String deleteUser(String cin) {
        if (userRepository.findById(cin).isPresent()) {
            userRepository.deleteById(cin);
            return "utilisateur supprimé";
        } else
            return "utilisateur non supprimé";
    }

    @Override
    public User desactivateUser(String cin) {
      User user = userRepository.findById(cin).orElse(null);
        assert user != null;
        user.setVerified(false);
     return userRepository.save(user);
    }

    @Override
    public User activateUser(String cin) {
        User user = userRepository.findById(cin).orElse(null);
        assert user != null;
        user.setVerified(true);
        return userRepository.save(user);
    }
}
