package com.vinicius.ecommerce.repositories;

import com.vinicius.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);

    User findByEmail(String email);
}
