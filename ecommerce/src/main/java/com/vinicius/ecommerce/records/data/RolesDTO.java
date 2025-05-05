package com.vinicius.ecommerce.records.data;

import com.vinicius.ecommerce.model.Roles;
import com.vinicius.ecommerce.model.enums.RolesName;

import java.util.Set;
import java.util.stream.Collectors;

public record RolesDTO(
        Long id,
        RolesName name
) {
    public static Set<RolesDTO> fromEntities(Set<Roles> roles) {
        return roles.stream()
                .map(role -> new RolesDTO(role.getId(), role.getName()))
                .collect(Collectors.toSet());
    }
}
