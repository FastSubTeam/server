package com.popple.server.domain.user.repository;

import com.popple.server.domain.user.jwt.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
