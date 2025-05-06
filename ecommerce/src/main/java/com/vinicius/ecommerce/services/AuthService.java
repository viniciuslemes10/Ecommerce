package com.vinicius.ecommerce.services;

import com.vinicius.ecommerce.model.Roles;
import com.vinicius.ecommerce.model.User;
import com.vinicius.ecommerce.records.vo.AccountCredentialsVO;
import com.vinicius.ecommerce.records.vo.TokenVO;
import com.vinicius.ecommerce.repositories.UserRepository;
import com.vinicius.ecommerce.token.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    public TokenVO signin(AccountCredentialsVO data) {
        try {
            var email = data.getEmail();
            var password = data.getPassword();

            User user = userRepository.findByEmail(email);

            if(user == null) {
                throw new UsernameNotFoundException("User not found with username: " + email);
            }

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            var roles = extractRoleNames(user.getRoles());

            return tokenProvider.createAccessToken(email, roles);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid userName/password supplied!");
        }
    }

    private List<String> extractRoleNames(Set<Roles> roles) {
        return roles.stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());
    }
}
