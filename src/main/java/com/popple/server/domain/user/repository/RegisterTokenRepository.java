package com.popple.server.domain.user.repository;

import com.popple.server.domain.entity.RegisterToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterTokenRepository extends JpaRepository<RegisterToken, Integer> {
    RegisterToken findByEmailAndRegisterToken(String email, String registerToken);

    RegisterToken findByEmail(String email);

    boolean existsByEmail(String email);
}
