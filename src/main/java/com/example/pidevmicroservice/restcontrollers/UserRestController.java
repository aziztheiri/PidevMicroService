package com.example.pidevmicroservice.restcontrollers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.pidevmicroservice.dto.OtpVerificationRequest;
import com.example.pidevmicroservice.entities.User;
import com.example.pidevmicroservice.entities.VerificationToken;
import com.example.pidevmicroservice.repositories.TokenRepository;
import com.example.pidevmicroservice.repositories.UserRepository;
import com.example.pidevmicroservice.services.EmailService;
import com.example.pidevmicroservice.services.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/users")
public class UserRestController {
    private final IUserService userService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
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
    @GetMapping("/admin")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
    }
    @GetMapping(value = "/admin/{cin}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> getUserById(@PathVariable(value = "cin") String cin) {
        return new ResponseEntity<>(userService.getUserById(cin), HttpStatus.OK);
    }
    @PostMapping("/signup")
    public ResponseEntity<Object> signup(
            @RequestParam("user") String userJson,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.registerModule(new JavaTimeModule());
            // Optionally, disable writing dates as timestamps for a better format.
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            // Conversion du JSON en objet User
            User user = mapper.readValue(userJson, User.class);
            // Appel du service pour l'inscription
            User createdUser = userService.signup(user, image);

            if (createdUser != null) {
                return ResponseEntity.status(HttpStatus.OK).body(createdUser);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Erreur lors de la création de l'utilisateur.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur lors du traitement de la requête.");
        }
    }
    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerificationRequest request) {
        VerificationToken token = tokenRepository.findByUserEmail(request.getEmail());
        if (token != null &&
                token.getToken().equals(request.getOtp()) &&
                token.getExpiryDate().isAfter(LocalDateTime.now())) {
            User user = token.getUser();
            user.setVerified(true);
            userRepository.save(user);
            // Optionally, delete the token now
            tokenRepository.delete(token);
            return ResponseEntity.ok("User verified successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired OTP");
        }
    }
    private Random random = new Random();
    private String generateOtp() {
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(@RequestBody OtpVerificationRequest request) {
        // Check if the user exists
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        // Generate new OTP
        String otp = generateOtp();

        // Delete the old verification token if exists
        VerificationToken existingToken = tokenRepository.findByUserEmail(request.getEmail());
        if (existingToken != null) {
            tokenRepository.delete(existingToken);
        }

        // Create new token with expiry (15 minutes)
        VerificationToken newToken = new VerificationToken();
        newToken.setToken(otp);
        newToken.setUser(user);
        newToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        tokenRepository.save(newToken);

        // Send OTP email
        emailService.sendOtpEmail(user.getEmail(), otp);

        return ResponseEntity.ok("OTP has been resent successfully");
    }
    @PutMapping("/admin/{cin}")
    public ResponseEntity<Object> updateUser(@PathVariable String cin,
                                        @RequestParam("user") String userJson,
                                        @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // Register the JavaTimeModule BEFORE deserialization!
            mapper.registerModule(new JavaTimeModule());
            // Optionally, disable writing dates as timestamps for a better format.
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            // Now deserialize the JSON string into a User object.
            User user = mapper.readValue(userJson, User.class);

            if (image != null && !image.isEmpty()) {
                String imageUrl = uploadImageToCloud(image);
                user.setImage(imageUrl); // Set the new image URL
            }

            User existingUser = userRepository.findById(cin).orElse(null);
            if (existingUser == null) {
                return ResponseEntity.notFound().build();
            }

            // Preserve the original creation date
            user.setCreationDate(existingUser.getCreationDate());
            User updatedUser = userService.updateUser(cin, user);

            if (updatedUser != null) {
                return ResponseEntity.ok(updatedUser);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user.");
        }
    }
    @DeleteMapping("/logout/{userId}")
    public ResponseEntity<String> logout(@PathVariable String userId) {
        userService.logoutFromKeycloak(userId);
        return ResponseEntity.ok("User logged out successfully");
    }


    @DeleteMapping(value = "/admin/{cin}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteUser(@PathVariable(value = "cin") String cin){
        return new ResponseEntity<>(userService.deleteUser(cin), HttpStatus.OK);
    }
    @PostMapping(value = "/admin/desactivate/{cin}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> desactivateUser(@PathVariable(value = "cin") String cin){
        return new ResponseEntity<>(userService.desactivateUser(cin), HttpStatus.OK);
    }
    @PostMapping(value = "/admin/activate/{cin}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> activateUser(@PathVariable(value = "cin") String cin){
        return new ResponseEntity<>(userService.activateUser(cin), HttpStatus.OK);
    }
}
