package com.vinicius.ecommerce.records.data;

import com.vinicius.ecommerce.model.User;

import java.util.Set;

public record UserDetailsDTO(Long id,
                             String email,
                             String userName,
                             Set<RolesDTO> roles
) {
    public UserDetailsDTO(User user) {
        this(user.getId(), user.getEmail(), user.getUsername(), RolesDTO.fromEntities(user.getRoles()));
    }
}
