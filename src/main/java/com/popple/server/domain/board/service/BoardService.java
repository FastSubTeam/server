package com.popple.server.domain.board.service;

import com.popple.server.domain.board.dto.*;
import com.popple.server.domain.board.repository.BoardRepository;
import com.popple.server.domain.board.repository.CommentRepository;
import com.popple.server.domain.entity.Comment;
import com.popple.server.domain.entity.Member;
import com.popple.server.domain.entity.Post;
import com.popple.server.domain.user.repository.MemberRepository;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public List<Post> getAllPosts() {
        return boardRepository.findAll();
    }


    @Transactional
    public List<CommentDto> getAllCommentsByPostId(Long postId) {
        List<CommentTableProjection> commentProjections = commentRepository.findByPost_id(postId);
        List<CommentDto> commentDtos = new ArrayList<>();
        for (CommentTableProjection c : commentProjections) {
            Optional<Member> member = memberRepository.findById(c.getMemberId());
            addCommentDto(commentDtos, c, member);
        }
        return commentDtos;
    }

    private void addCommentDto(List<CommentDto> commentDtos, CommentTableProjection c, Optional<Member> member) {
        if (member.isPresent()) {
            MemberRespDto memberRespDto = MemberRespDto.of(member.get());
            CommentDto commentDto = CommentDto.builder()
                    .id(c.getId())
                    .content(c.getContent())
                    .createdAt(c.getCreatedAt())
                    .updatedAt(c.getUpdatedAt())
                    .member(memberRespDto)
                    .build();
            commentDtos.add(commentDto);
            return;
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

    @Transactional
    public Page<Post> getPostsByPage(Pageable pageable) {
        log.info("service접근");
        return boardRepository.findAll(pageable);
    }

    @Transactional
    public Post getPostById(Long postId) {
        Optional<Post> post = boardRepository.findById(postId);
        if (post.isPresent()) {
            return post.get();
        }
        throw new NoSuchElementException("게시물의 id가 존재하지 않습니다.");
    }

    @Transactional
    public Post savePost(Post post) {
        return boardRepository.save(post);
    }

    @Transactional
    public void deletePost(Long postId) throws IllegalArgumentException {
        if (!hasPost(postId)) {
            throw new NoSuchElementException("해당 게시물이 존재하지 않습니다.");
        }
        commentRepository.deleteAllByPost_id(postId);
        boardRepository.deleteById(postId);
    }

    @Transactional
    public boolean hasPost(Long postId) {
        Optional<Post> post = boardRepository.findById(postId);
        return post.isPresent();
    }

    @Transactional
    public void deleteComment(Long commentId) throws IllegalArgumentException {
        commentRepository.deleteById(commentId);
    }

    @Transactional
    public void updatePost(Long postId, PostReqDto postReqDto) {
        Post post = getPostById(postId);
        post.modifyPost(postReqDto);
    }

    @Transactional
    public Member getMember(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    @Transactional
    public CommentDto saveComment(Long postId, Member loginMember, CommentReqDto commentReqDto) {
        Post post = boardRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));
        Comment comment = commentReqDto.toEntity(post, loginMember);
        Comment savedComment = commentRepository.save(comment);
        return CommentDto.builder()
                .id(savedComment.getId())
                .content(savedComment.getContent())
                .createdAt(savedComment.getCreatedAt())
                .updatedAt(savedComment.getUpdatedAt())
                .member(MemberRespDto.of(savedComment.getMember()))
                .build();
    }

    @Transactional
    public CommentDto updateComment(Long commentId, CommentReqDto commentReqDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        comment.modifyComment(commentReqDto);
        Comment updatedComment = commentRepository.findById(commentId).orElse(null);
        return CommentDto.builder()
                .id(updatedComment.getId())
                .content(updatedComment.getContent())
                .createdAt(updatedComment.getCreatedAt())
                .updatedAt(updatedComment.getUpdatedAt())
                .member(MemberRespDto.of(updatedComment.getMember()))
                .build();
    }

    @Transactional
    public Long getCommentAuthor(Long commentId){
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        return MemberRespDto.of(comment.getMember()).getId();
    }

    @Transactional
    public Long getPostAuthor(Long postId){
        Post post = boardRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        return MemberRespDto.of(post.getMember()).getId();
    }
}
