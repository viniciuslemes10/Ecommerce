package com.vinicius.ecommerce.services;

import com.vinicius.ecommerce.model.Roles;
import com.vinicius.ecommerce.model.User;
import com.vinicius.ecommerce.records.data.RolesDTO;
import com.vinicius.ecommerce.records.data.UserDTO;
import com.vinicius.ecommerce.records.data.UserDetailsDTO;
import com.vinicius.ecommerce.repositories.RolesRepository;
import com.vinicius.ecommerce.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {
    Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    RolesRepository rolesRepository;

    public UserDetailsDTO create(UserDTO data) {
        var user = new User(data);
        encodeAndSetPassword(user);

        user.setRoles(addRoles(data));
        User userCreated = userRepository.save(user);

        return new UserDetailsDTO(userCreated);
    }

    private Set<Roles> addRoles(UserDTO data) {
        Set<Roles> roles = new HashSet<>();
        for (RolesDTO dto : data.roles()) {
            Roles role = rolesRepository.findByName(dto.name());
            if(role != null) {
                roles.add(role);
            }
        }
        log.info("");
        log.info("");
        log.info("**************************");
        log.info("ROLES: " + roles.toString());
        return roles;
    }

    private void encodeAndSetPassword(User user) {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder(
                "", 8, 185000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);

        encoders.put("pbkdf2", pbkdf2PasswordEncoder);
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2PasswordEncoder);
        String newSenha = passwordEncoder.encode(user.getPassword());

        if(newSenha.contains("{pbkdf2}")) {
            newSenha = newSenha.substring("{pbkdf2}".length());
        }
        user.setPassword(newSenha);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUserName(username);
        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("Username " + username + " not found!");
        }
    }
}