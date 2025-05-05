package com.vinicius.ecommerce.repositories;

import com.vinicius.ecommerce.model.Roles;
import com.vinicius.ecommerce.model.enums.RolesName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {
    Roles findByName(RolesName name);
}
