package com.popple.server.domain.board.repository;

import com.popple.server.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Post, Long> {
}
