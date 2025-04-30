package com.vinicius.ecommerce.model;

import com.vinicius.ecommerce.model.enums.RolesName;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_roles")
@Data
@EqualsAndHashCode(of = "id")
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private RolesName name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();
}
