package com.school.schooldb.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.school.schooldb.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


// Stores user information, which is encapsulated into authentication objects.
// Allows for non-security related user information to be stored
public class UserAdapter implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String emailAddress;

    @JsonIgnore
    private String password;


    private Collection<? extends GrantedAuthority> authorities;

    public UserAdapter(Long id, String email, String password,
                       Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.emailAddress = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserAdapter build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getName().name())
        ).collect(Collectors.toList());

        return new UserAdapter(
                user.getId(),
                user.getEmailAddress(),
                user.getPassword(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return emailAddress;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAdapter user = (UserAdapter) o;
        return Objects.equals(id, user.id);
    }
}
