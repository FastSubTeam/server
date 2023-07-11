package com.popple.server.domain.board.service;

import com.popple.server.domain.board.repository.BoardRepository;
import com.popple.server.domain.board.repository.CommentRepository;
import com.popple.server.domain.entity.Comment;
import com.popple.server.domain.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public List<Post> getAllPosts() {
        return boardRepository.findAll();
    }

    public List<Comment> getAllCommentsByPostId(Long postId){
        return commentRepository.findByPost_Id(postId);
    }
}
