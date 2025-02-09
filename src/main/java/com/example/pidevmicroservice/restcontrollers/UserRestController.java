package com.example.pidevmicroservice.restcontrollers;

import com.example.pidevmicroservice.entities.User;
import com.example.pidevmicroservice.services.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/users")
public class UserRestController {
    private final IUserService userService;

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
}
