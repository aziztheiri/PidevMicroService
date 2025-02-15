package com.example.pidevmicroservice.services;
import org.springframework.http.*;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.pidevmicroservice.entities.User;
import com.example.pidevmicroservice.entities.VerificationToken;
import com.example.pidevmicroservice.enums.UserRole;
import com.example.pidevmicroservice.repositories.TokenRepository;
import com.example.pidevmicroservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;

    @Value("${keycloak.auth-server-url}")
    private String keycloakUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin-client-id}")
    private String adminClientId;

    @Value("${keycloak.admin-client-secret}")
    private String adminClientSecret;
    private Cloudinary getCloudinaryInstance() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dmwttu9lu",
                "api_key", "974645429429234",
                "api_secret", "XIhfcEzguJ_ZcZ1RDaD9am8r4bU"
        ));
    }
    @Override
    public void logoutFromKeycloak(String userId) {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakUrl)
                .realm("master")  // Use 'master' for admin access
                .clientId("admin-cli")
                .username("admin")
                .password("admin")
                .grantType(OAuth2Constants.PASSWORD)
                .build();

        keycloak.realm(realm).users().get(userId).logout();
    }
    public void createUserInKeycloak(User user) {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakUrl)
                .realm("master")  // Use 'master' realm for admin access
                .clientId("admin-cli")  // Use 'admin-cli' for admin-level actions
                .username("admin")  // Your Keycloak admin username
                .password("admin")  // Your Keycloak admin password
                .grantType(OAuth2Constants.PASSWORD)
                .build();

        UserRepresentation keycloakUser = new UserRepresentation();
        keycloakUser.setEnabled(true);
        keycloakUser.setUsername(user.getEmail());
        keycloakUser.setEmail(user.getEmail());
        keycloakUser.setFirstName(user.getName());
        keycloakUser.setLastName(user.getName());
        keycloakUser.setEmailVerified(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(user.getPassword());
        credential.setTemporary(false);
        keycloakUser.setCredentials(Collections.singletonList(credential));
        keycloakUser.setRequiredActions(new ArrayList<>());

        // Create the user in Keycloak
        Response response = keycloak.realm(realm).users().create(keycloakUser);
        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create user in Keycloak: " + response.getStatus());
        }

        // Retrieve the newly created user's ID
        String userId = CreatedResponseUtil.getCreatedId(response);

        // Assign a role (e.g., "user" or "admin") to the created user
        // Here, we're assigning a realm role named "user"
        RoleRepresentation realmRole = keycloak.realm(realm).roles().get("customer").toRepresentation();
        keycloak.realm(realm).users().get(userId).roles().realmLevel().add(Collections.singletonList(realmRole));
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
        createUserInKeycloak( user);
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
