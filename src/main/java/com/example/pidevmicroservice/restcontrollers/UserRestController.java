package com.example.pidevmicroservice.restcontrollers;

import com.example.pidevmicroservice.DTO.OtpVerificationRequest;
import com.example.pidevmicroservice.entities.User;
import com.example.pidevmicroservice.entities.VerificationToken;
import com.example.pidevmicroservice.repositories.TokenRepository;
import com.example.pidevmicroservice.repositories.UserRepository;
import com.example.pidevmicroservice.services.EmailService;
import com.example.pidevmicroservice.services.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
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
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
    }
    @GetMapping(value = "/{cin}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> getUserById(@PathVariable(value = "cin") String cin) {
        return new ResponseEntity<>(userService.getUserById(cin), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<?> signup(
            @RequestParam("user") String userJson,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        ObjectMapper mapper = new ObjectMapper();
        try {
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
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerificationRequest request) {
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
    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestBody OtpVerificationRequest request) {
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
    @PutMapping(value = "/{cin}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") String cin, @RequestBody User user){
        return new ResponseEntity<>(userService.updateUser(cin, user),
                HttpStatus.OK);
    }
    @DeleteMapping(value = "/{cin}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteUser(@PathVariable(value = "cin") String cin){
        return new ResponseEntity<>(userService.deleteUser(cin), HttpStatus.OK);
    }


}
