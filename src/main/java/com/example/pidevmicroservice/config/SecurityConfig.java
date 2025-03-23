package com.example.pidevmicroservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Value("${keycloak.auth-server-url}")
    private String keycloakUrl;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.ignoringRequestMatchers(
                        "/h2/**",
                        "/users/login",
                        "/users/signup",
                        "/users/gemini-content/**",
                        "/users/verify",
                        "/users/resend-otp",
                        "/users/forgot-password",
                        "/users/reset-password",
                        "/users/find/**",
                        "/users/run-report"
                )) // Exclude auth endpoints from CSRF protection
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2/**").permitAll()
                        .requestMatchers("/users/login", "/users/signup", "/users/verify", "/users/resend-otp","/users/gemini-content/**","/users/forgot-password","/users/reset-password","/users/find/**",
                                "/users/run-report").permitAll()
                        .anyRequest().authenticated() // Protect other routes
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        // Keep JWT validation for protected endpoints

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation(keycloakUrl+"/realms/pidev-realm");
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}