package com.school.schooldb.repository;

import com.school.schooldb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //Optional<User> findByUsername(String username);
    User findByEmailAddress(String emailAddress);

    //Boolean existsByUsername(String username);
    Boolean existsByEmailAddress(String email);
}
