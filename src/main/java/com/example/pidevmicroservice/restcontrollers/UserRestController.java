package com.example.pidevmicroservice.restcontrollers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.pidevmicroservice.dto.DeleteUserRequest;
import com.example.pidevmicroservice.dto.OtpVerificationRequest;
import com.example.pidevmicroservice.dto.PasswordUpdateRequest;
import com.example.pidevmicroservice.entities.PasswordResetToken;
import com.example.pidevmicroservice.entities.User;
import com.example.pidevmicroservice.entities.VerificationToken;
import com.example.pidevmicroservice.repositories.PasswordResetTokenRepository;
import com.example.pidevmicroservice.repositories.TokenRepository;
import com.example.pidevmicroservice.repositories.UserRepository;
import com.example.pidevmicroservice.services.EmailService;
import com.example.pidevmicroservice.services.Exceptions;
import com.example.pidevmicroservice.services.IUserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.UserRepresentation;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserRestController {
    private final IUserService userService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final RestTemplate restTemplate;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private  static  String gemini = "AIzaSyAIG9UME50dL8NQpcMfCVQXXRZymWxBkRA";

    @Value("${keycloak.realm}")
    private String realm;
    private String tokenUrl="http://localhost:8180/realms/pidev-realm/protocol/openid-connect/token";
    private Cloudinary getCloudinaryInstance() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dmwttu9lu",
                "api_key", "974645429429234",
                "api_secret", "XIhfcEzguJ_ZcZ1RDaD9am8r4bU"
        ));
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        // Prepare the form data for Keycloak
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", "pidev-client");
        body.add("username", username);
        body.add("password", password);
        body.add("grant_type", "password");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
            // Successful login: reset the failure counter
            userService.loginSucceeded(username);
            // Return the token response to the Angular client
            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException e) {
            // Increment the failed login attempt
            userService.loginFailed(username);
            // If the user has exceeded max attempts, deactivate the account
            if(userRepository.findByEmail(username).isVerified() == true) {
                if (userService.hasExceededMaxAttempts(username)) {
                    userService.desactivateUser(userRepository.findByEmail(username).getCin());
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body("Account has been deactivated due to multiple failed login attempts.");
                }
            }
            // Return the error from Keycloak
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
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
    @GetMapping(value = "/user/getemail/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> getUserByEmail(@PathVariable(value = "email") String email) {
        return new ResponseEntity<>(userRepository.findByEmail(email), HttpStatus.OK);
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
    private Keycloak getKeycloakAdminClient() {
        return  KeycloakBuilder.builder()
                .serverUrl("http://localhost:8180")
                .realm("master")  // Use 'master' realm for admin access
                .clientId("admin-cli")  // Use 'admin-cli' for admin-level actions
                .username("admin")  // Your Keycloak admin username
                .password("admin")  // Your Keycloak admin password
                .grantType(OAuth2Constants.PASSWORD)
                .build();
    }
    private void updateUserInKeycloak(User user) {
        Keycloak keycloak = getKeycloakAdminClient();
        UserRepresentation keycloakUser = keycloak.realm(realm).users().get(user.getKeycloakId()).toRepresentation();

        keycloakUser.setEmail(user.getEmail());
        keycloakUser.setUsername(user.getEmail());
        keycloakUser.setFirstName(user.getName());
        keycloakUser.setLastName(user.getName());
        keycloakUser.setEnabled(user.isVerified());
        keycloakUser.setEmailVerified(user.isVerified());
        keycloak.realm(realm).users().get(user.getKeycloakId()).update(keycloakUser);
    }
    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerificationRequest request) {
        VerificationToken token = tokenRepository.findByUserEmail(request.getEmail());
        if (token != null &&
                token.getToken().equals(request.getOtp()) &&
                token.getExpiryDate().isAfter(LocalDateTime.now())) {
            User user = token.getUser();
            user.setVerified(true);
            updateUserInKeycloak(user);
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
    @PutMapping("/user/{cin}")
    public ResponseEntity<Object> updateNotAdminUser(@PathVariable String cin,
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
    @PutMapping("/user/password/{cin}")
    public ResponseEntity<?> updatePassword(@PathVariable("cin") String cin,
                                            @RequestBody PasswordUpdateRequest request) {
        userService.updatePassword(cin, request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.ok("Password updated successfully.");
    }
    @PostMapping("/gemini-content")
    public ResponseEntity<?> getGeminiContent() {
        RestTemplate restTemplate = new RestTemplate();
        String geminiApiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + gemini;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = "{ \"contents\": [{ \"parts\": [{ \"text\": \"Provide me with today's key updates on the insurance industry in Tunisia and globally. Format the response in HTML using semantic markup (headings, paragraphs, and lists) for a clean presentation.And please do not include any comments or anything just title tunisia or global industry with information , all the thinking you do in background and irrelevant messages remove them\" }] }] }";

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(geminiApiUrl, HttpMethod.POST, request, String.class);

            // Parse the response to extract text
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            String text = root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();

            // Return text as JSON object
            Map<String, String> response1 = new HashMap<>();
            response1.put("text", text);
            return ResponseEntity.ok(response1); // Return JSO

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
    @PostMapping("/user/delete-user")
    public ResponseEntity<String> deleteUser(@RequestBody DeleteUserRequest request) {
        Optional<User> existingUserOpt = userRepository.findById(request.getCin());
        if (!existingUserOpt.isPresent()) {
            throw new Exceptions.UserNotFoundException("User with CIN " + request.getCin() + " not found");
        }
        User existingUser = existingUserOpt.get();
        if (!BCrypt.checkpw(request.getOldPassword(), existingUser.getPassword())) {
            throw new Exceptions.InvalidPasswordException("Old password is incorrect.");
        }
        Keycloak keycloak = getKeycloakAdminClient();
        keycloak.realm(realm).users().delete(existingUser.getKeycloakId());
        userRepository.delete(existingUser);
        return ResponseEntity.ok("User deleted successfully.");
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> processForgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        User user = userRepository.findByEmail(email);
        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);
        emailService.sendResetTokenEmail(user, token);
        return ResponseEntity.ok("Reset password email sent");
    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");
        String tokenValidationResult = userService.validatePasswordResetToken(token);
        if (tokenValidationResult != null) {
            return ResponseEntity.badRequest().body(tokenValidationResult);
        }
       PasswordResetToken passToken = passwordResetTokenRepository.findByToken(token).orElse(null);
        if (passToken == null) {
            return ResponseEntity.badRequest().body("Invalid token");
        }
        User user = passToken.getUser();
        userService.changeUserPassword(user, newPassword);
        passwordResetTokenRepository.delete(passToken);
        return ResponseEntity.ok("Password reset successfully");
    }
}
