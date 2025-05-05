package com.vinicius.ecommerce.records.data;

import java.util.Set;

public record UserDTO(
        String email,
        String password,
        String userName,
        Set<RolesDTO> roles
) {
}
