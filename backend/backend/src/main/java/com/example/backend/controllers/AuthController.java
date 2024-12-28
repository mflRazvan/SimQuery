package com.example.backend.controllers;

import com.example.backend.jwt.JwtUtils;
import com.example.backend.models.Role;
import com.example.backend.models.User;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.UserDetailsImpl;
import com.example.backend.utils.dtos.request.LoginRequest;
import com.example.backend.utils.dtos.request.SignupRequest;
import com.example.backend.utils.dtos.response.LoginResponse;
import com.example.backend.utils.dtos.response.SignupResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        // Check username
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already taken!");
        }

        // Check email
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already in use!");
        }

        // Create new user's account
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));

        // Set role
        String strRole = signUpRequest.getRole();
        if (strRole != null) {
            try {
                Role role = Role.valueOf(strRole.toUpperCase());
                user.setRole(role);
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role specified");
            }
        } else {
            user.setRole(Role.BASIC_USER);
        }

        userRepository.save(user);

        return ResponseEntity.ok(new SignupResponse(
                user.getEmail(),
                user.getUsername(),
                "User registered successfully!"
        ));
    }

    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            return ResponseEntity.ok(new LoginResponse(
                    jwt,
                    userDetails.getUsername(),
                    userDetails.getEmail()
            ));
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
    }
}