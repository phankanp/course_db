package com.school.schooldb.web.controller;

import com.school.schooldb.model.Role;
import com.school.schooldb.model.RoleName;
import com.school.schooldb.model.User;
import com.school.schooldb.repository.RoleRepository;
import com.school.schooldb.repository.UserRepository;
import com.school.schooldb.security.JwtProvider;
import com.school.schooldb.security.payload.JwtResponse;
import com.school.schooldb.security.payload.LoginForm;
import com.school.schooldb.security.payload.SignUpForm;
import com.school.schooldb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;


    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository
            roleRepository, UserService userService, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    // User sign and generate token
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginForm) {

        User user = userService.findByEmail(loginForm.getEmail());

        Map<String, String> errors = new HashMap<>();

        if (user == null) {
            if (!userRepository.existsByEmailAddress(loginForm.getEmail())) {
                errors.put("email", "Invalid email");
                return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
            }
        } else if (!passwordEncoder.matches(loginForm.getPassword(), user.getPassword())) {
            errors.put("password", "Invalid password");
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginForm.getEmail(),
                        loginForm.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);

        return ResponseEntity.ok(new JwtResponse(jwt,user.getFirstName(), user.getLastName()));
    }

    // User registration
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignUpForm signUpForm) {

        if (userRepository.existsByEmailAddress(signUpForm.getEmail())) {
            return new ResponseEntity<>("Username is already taken", HttpStatus.BAD_REQUEST);
        }

        User user = new User(signUpForm.getFirstName(), signUpForm.getLastName(), signUpForm.getEmail(), signUpForm
                .getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        Role userRole = new Role(RoleName.ROLE_USER);

        Set<Role> roles = new HashSet<>();

        roles.add(userRole);

        user.setRoles(roles);

        return ResponseEntity.ok().body("User registration complete");
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());

        Map<Object, Object> model = new HashMap<>();
        model.put("firstName", user.getFirstName());
        model.put("lastName", user.getLastName());

        return ResponseEntity.ok(model);
    }
}
