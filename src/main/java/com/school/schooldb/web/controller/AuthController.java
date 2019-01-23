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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;


    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository
            roleRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    // User sign and generate token
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginForm) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginForm.getEmail(),
                        loginForm.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);

        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    // User registration
    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignUpForm signUpForm) {

        if (userRepository.existsByEmailAddress(signUpForm.getEmail())) {
            return new ResponseEntity<>("Username is already taken", HttpStatus.BAD_REQUEST);
        }

        User user = new User(signUpForm.getFirstName(), signUpForm.getLastName(), signUpForm.getEmail(), signUpForm
                .getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = new Role(RoleName.ROLE_USER);

        Set<Role> roles = new HashSet<>();

        roles.add(userRole);

        user.setRoles(roles);

        userRepository.save(user);

        return ResponseEntity.ok().body("User registration complete");


    }
}
