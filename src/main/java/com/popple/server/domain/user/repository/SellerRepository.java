package com.popple.server.domain.user.repository;

import com.popple.server.domain.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}