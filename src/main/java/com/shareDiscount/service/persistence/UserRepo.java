package com.shareDiscount.service.persistence;

import com.shareDiscount.service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long>{

    Optional<User> findByUserName(String name);
}
