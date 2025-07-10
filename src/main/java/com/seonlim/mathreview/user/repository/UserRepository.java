package com.seonlim.mathreview.user.repository;

import com.seonlim.mathreview.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
