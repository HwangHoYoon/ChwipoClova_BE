package com.chwipoClova.user.repository;

import com.chwipoClova.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySnsTypeAndSnsId(Integer SnsType, String snsId);

    Optional<User> findByEmailAndSnsType(String email, Integer SnsType);
}
