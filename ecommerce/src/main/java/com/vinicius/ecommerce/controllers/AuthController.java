package com.vinicius.ecommerce.controllers;

import com.vinicius.ecommerce.records.data.UserDTO;
import com.vinicius.ecommerce.records.data.UserDetailsDTO;
import com.vinicius.ecommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    UserService service;

    @PostMapping("/register")
    public ResponseEntity<UserDetailsDTO> createdUser(@RequestBody UserDTO data) {
        var userCreated = service.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
}
