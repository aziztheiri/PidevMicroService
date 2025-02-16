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

    private String createUserInKeycloak(User user) {
        Keycloak keycloak = getKeycloakAdminClient();

        UserRepresentation keycloakUser = new UserRepresentation();
        keycloakUser.setEnabled(false);
        keycloakUser.setUsername(user.getEmail());
        keycloakUser.setEmail(user.getEmail());
        keycloakUser.setFirstName(user.getName());
        keycloakUser.setLastName(user.getName());
        keycloakUser.setEmailVerified(false);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(user.getPassword());
        credential.setTemporary(false);
        keycloakUser.setCredentials(Collections.singletonList(credential));

        // Create user in Keycloak
        Response response = keycloak.realm(realm).users().create(keycloakUser);
        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create user in Keycloak: " + response.getStatus());
        }

        String keycloakUserId = CreatedResponseUtil.getCreatedId(response);

        // Assign role
        RoleRepresentation role = keycloak.realm(realm).roles().get("customer").toRepresentation();
        keycloak.realm(realm).users().get(keycloakUserId).roles().realmLevel().add(Collections.singletonList(role));

        return keycloakUserId;
    }
    private Keycloak getKeycloakAdminClient() {
        return  KeycloakBuilder.builder()
                .serverUrl(keycloakUrl)
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
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(user.getPassword());
        credential.setTemporary(false);
        keycloakUser.setCredentials(Collections.singletonList(credential));
        keycloak.realm(realm).users().get(user.getKeycloakId()).update(keycloakUser);
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
        // Existing validations
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new Exceptions.EmailAlreadyExists("L'email existe déjà.");
        }
        if (userRepository.existsById(user.getCin())) {
            throw new Exceptions.UserAlreadyExistsException("Un utilisateur avec ce CIN existe déjà.");
        }

        // Upload image
        if (image != null && !image.isEmpty()) {
            String imageUrl = uploadImageToCloud(image);
            user.setImage(imageUrl);
        }

        // Create user in Keycloak and get ID
        String keycloakUserId = createUserInKeycloak(user);
        user.setKeycloakId(keycloakUserId);

        // Set user properties
        user.setUserRole(UserRole.CUSTOMER);
        user.setCreationDate(LocalDateTime.now());
        user.setVerified(false);

        // Save to local database
        User savedUser = userRepository.save(user);

        // Generate OTP and send email
        String otp = generateOtp();
        VerificationToken token = new VerificationToken();
        token.setToken(otp);
        token.setUser(savedUser);
        token.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        tokenRepository.save(token);
        emailService.sendOtpEmail(savedUser.getEmail(), otp);

        return savedUser;
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
            updateUserInKeycloak(existingUser); // Sync with Keycloak

            return userRepository.save(existingUser);
        } else {
            throw new Exceptions.UserNotFoundException("User with CIN " + cin + " not found");
        }
    }


    @Override
    public String deleteUser(String cin) {
        User user = userRepository.findById(cin)
                .orElseThrow(() -> new Exceptions.UserNotFoundException("User not found"));

        Keycloak keycloak = getKeycloakAdminClient();
        keycloak.realm(realm).users().delete(user.getKeycloakId());

        userRepository.delete(user);
        return "utilisateur supprimé";
    }

    @Override
    public User activateUser(String cin) {
        User user = userRepository.findById(cin)
                .orElseThrow(() -> new Exceptions.UserNotFoundException("User not found"));
        user.setVerified(true);
        userRepository.save(user);

        Keycloak keycloak = getKeycloakAdminClient();
        UserRepresentation keycloakUser = keycloak.realm(realm).users().get(user.getKeycloakId()).toRepresentation();
        keycloakUser.setEnabled(true);
        keycloak.realm(realm).users().get(user.getKeycloakId()).update(keycloakUser);

        return user;
    }

    @Override
    public User desactivateUser(String cin) {
        User user = userRepository.findById(cin)
                .orElseThrow(() -> new Exceptions.UserNotFoundException("User not found"));
        user.setVerified(false);
        userRepository.save(user);

        Keycloak keycloak = getKeycloakAdminClient();
        UserRepresentation keycloakUser = keycloak.realm(realm).users().get(user.getKeycloakId()).toRepresentation();
        keycloakUser.setEnabled(false);
        keycloak.realm(realm).users().get(user.getKeycloakId()).update(keycloakUser);

        return user;
    }
}
