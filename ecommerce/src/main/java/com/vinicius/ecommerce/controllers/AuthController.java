package com.vinicius.ecommerce.controllers;

import com.vinicius.ecommerce.records.data.UserDTO;
import com.vinicius.ecommerce.records.data.UserDetailsDTO;
import com.vinicius.ecommerce.records.vo.AccountCredentialsVO;
import com.vinicius.ecommerce.services.AuthService;
import com.vinicius.ecommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    UserService service;

    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserDetailsDTO> createdUser(@RequestBody UserDTO data) {
        var userCreated = service.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/signin")
    public ResponseEntity signIn(@RequestBody AccountCredentialsVO data) {
        if(checkIfParamIsNotNull(data)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client Request!");
        }
        return ResponseEntity.ok(authService.signin(data));
    }

    @SuppressWarnings("rawtypes")
    @PutMapping("/refresh/{email}")
    public ResponseEntity refresh(@PathVariable("email") String email,
                                  @RequestHeader("Authorization") String refreshToken) {
        if(checkIfParamIsNotNull(email, refreshToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }

        var token = authService.refreshToken(email, refreshToken);
        if(token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }

        return ResponseEntity.ok(token);
    }

    private boolean checkIfParamIsNotNull(String email, String refreshToken) {
        return email == null || email.isBlank() || refreshToken == null || refreshToken.isBlank();
    }

    private boolean checkIfParamIsNotNull(AccountCredentialsVO data) {
        return data == null || data.getEmail() == null || data.getEmail().isBlank()
                || data.getPassword() == null || data.getPassword().isBlank();
    }
}
