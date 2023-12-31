package com.popple.server.domain.board.repository;

import com.popple.server.domain.board.dto.CommentTableProjection;
import com.popple.server.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<CommentTableProjection> findByPost_id(Long postId);
    void deleteAllByPost_id(Long postId);
}
