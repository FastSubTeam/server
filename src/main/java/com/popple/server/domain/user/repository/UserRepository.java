package com.popple.server.domain.user.repository;

import com.popple.server.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}