package com.popple.server.domain.board.service;

import com.popple.server.domain.board.repository.BoardRepository;
import com.popple.server.domain.board.repository.CommentRepository;
import com.popple.server.domain.entity.Comment;
import com.popple.server.domain.entity.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public List<Post> getAllPosts() {
        return boardRepository.findAll();
    }

    public List<Comment> getAllCommentsByPostId(Long postId) {
        return commentRepository.findByPost_Id(postId);
    }

    public Page<Post> getPostsByPage(Pageable pageable) {
        log.info("service접근");
        return boardRepository.findAll(pageable);
    }

    public Post getPostById(Long postId){
        Optional<Post> post = boardRepository.findById(postId);
        if(post.isPresent()){
            return post.get();
        }
        throw new NoSuchElementException("게시물의 id가 존재하지 않습니다.");
    }

}
