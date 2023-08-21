package com.popple.server.domain.user.repository;

import com.popple.server.domain.entity.Admin;
import com.popple.server.domain.user.dto.LoginRequestDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findByEmail(String email);
}
