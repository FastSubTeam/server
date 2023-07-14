package com.popple.server.domain.board.service;

import com.popple.server.domain.board.dto.CommentDto;
import com.popple.server.domain.board.dto.CommentTableProjection;
import com.popple.server.domain.board.repository.BoardRepository;
import com.popple.server.domain.board.repository.CommentRepository;
import com.popple.server.domain.entity.Member;
import com.popple.server.domain.entity.Post;
import com.popple.server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private final UserRepository userRepository;

    public List<Post> getAllPosts() {
        return boardRepository.findAll();
    }

    public List<CommentDto> getAllCommentsByPostId(Long postId) {
        List<CommentTableProjection> commentProjections = commentRepository.findByPost_id(postId);
        List<CommentDto> commentDtos = new ArrayList<>();
        for (CommentTableProjection c : commentProjections) {
            Optional<Member> member = userRepository.findById(c.getMemberId());
            if (member.isPresent()) {
                CommentDto commentDto = CommentDto.builder()
                        .id(c.getId())
                        .content(c.getContent())
                        .createdAt(c.getCreatedAt())
                        .updatedAt(c.getUpdatedAt())
                        .member(member.get())
                        .build();
                commentDtos.add(commentDto);
                continue;
            }
            CommentDto commentDto = CommentDto.builder()
                    .id(c.getId())
                    .content(c.getContent())
                    .createdAt(c.getCreatedAt())
                    .updatedAt(c.getUpdatedAt())
                    .member(null)// Todo 존재하지 않는 사용자에 대한 예외처리
                    .build();
            commentDtos.add(commentDto);
            log.error("comment_id = {}의 Member정보가 존재하지 않습니다.", c.getId());
        }
        return commentDtos;
    }

    public Page<Post> getPostsByPage(Pageable pageable) {
        log.info("service접근");
        return boardRepository.findAll(pageable);
    }

    public Post getPostById(Long postId) {
        Optional<Post> post = boardRepository.findById(postId);
        if (post.isPresent()) {
            return post.get();
        }
        throw new NoSuchElementException("게시물의 id가 존재하지 않습니다.");
    }

}
