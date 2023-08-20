package com.popple.server.domain.user.repository;

import com.popple.server.domain.entity.Member;
import com.popple.server.domain.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<Seller> findByEmail(String email);
}