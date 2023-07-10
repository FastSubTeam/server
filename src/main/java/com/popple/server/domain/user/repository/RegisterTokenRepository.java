package com.popple.server.domain.user.repository;

import com.popple.server.domain.entity.RegisterToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterTokenRepository extends JpaRepository<RegisterToken, Integer> {
    RegisterToken findByRegisterToken(String registerToken);

    RegisterToken findByEmail(String email);
}
