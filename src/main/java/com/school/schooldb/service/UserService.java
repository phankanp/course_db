package com.school.schooldb.service;

import com.school.schooldb.model.User;
import com.school.schooldb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmail(String username) {
        return userRepository.findByEmailAddress(username);
    }

    public void save(User user) {
        userRepository.save(user);
    }
}
