package com.popple.server.domain.board.service;

import com.popple.server.domain.board.dto.*;
import com.popple.server.domain.board.exception.BoardErrorMessages;
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

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
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
    public List<CommentRespDto> getAllCommentsByPostId(Long postId) {
        List<CommentTableProjection> commentProjections = commentRepository.findByPost_id(postId);
        List<CommentRespDto> commentRespDtos = new ArrayList<>();
        for (CommentTableProjection c : commentProjections) {
            Optional<Member> member = memberRepository.findById(c.getMemberId());
            addCommentDto(commentRespDtos, c, member);
        }
        return commentRespDtos;
    }

    private void addCommentDto(List<CommentRespDto> commentRespDtos, CommentTableProjection c, Optional<Member> member) {
        if (member.isPresent()) {
            MemberRespDto memberRespDto = MemberRespDto.of(member.get());
            CommentRespDto commentRespDto = CommentRespDto.builder()
                    .id(c.getId())
                    .content(c.getContent())
                    .createdAt(c.getCreatedAt())
                    .updatedAt(c.getUpdatedAt())
                    .member(memberRespDto)
                    .build();
            commentRespDtos.add(commentRespDto);
            return;
        }
        CommentRespDto commentRespDto = CommentRespDto.builder()
                .id(c.getId())
                .content("작성자 정보가 존재하지 않는 댓글입니다.")
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .member(null)
                .build();
        commentRespDtos.add(commentRespDto);
    }

    @Transactional
    public Page<Post> getPostsByPage(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    @Transactional
    public Post getPostById(Long postId) {
        Optional<Post> post = boardRepository.findById(postId);
        if (post.isPresent()) {
            return post.get();
        }
        throw new EntityNotFoundException(BoardErrorMessages.POST_NOT_FOUND);
    }

    @Transactional
    public Post savePost(Post post) {
        return boardRepository.save(post);
    }

    @Transactional
    public void deletePost(Long postId) throws IllegalArgumentException {
        if (!hasPost(postId)) {
            throw new EntityNotFoundException(BoardErrorMessages.POST_NOT_FOUND);
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
    public CommentRespDto saveComment(Long postId, Member loginMember, CommentReqDto commentReqDto) {
        Post post = boardRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException(BoardErrorMessages.MISSING_POST));
        Comment comment = commentReqDto.toEntity(post, loginMember);
        Comment savedComment = commentRepository.save(comment);
        return CommentRespDto.builder()
                .id(savedComment.getId())
                .content(savedComment.getContent())
                .createdAt(savedComment.getCreatedAt())
                .updatedAt(savedComment.getUpdatedAt())
                .member(MemberRespDto.of(savedComment.getMember()))
                .build();
    }

    @Transactional
    public CommentRespDto updateComment(Long commentId, CommentReqDto commentReqDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException(BoardErrorMessages.COMMENT_NOT_FOUND));
        comment.modifyComment(commentReqDto);
        Comment updatedComment = commentRepository.findById(commentId).orElse(null);
        return CommentRespDto.builder()
                .id(updatedComment.getId())
                .content(updatedComment.getContent())
                .createdAt(updatedComment.getCreatedAt())
                .updatedAt(updatedComment.getUpdatedAt())
                .member(MemberRespDto.of(updatedComment.getMember()))
                .build();
    }

    @Transactional
    public Long getCommentAuthor(Long commentId){
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException(BoardErrorMessages.COMMENT_NOT_FOUND));
        return MemberRespDto.of(comment.getMember()).getId();
    }

    @Transactional
    public Long getPostAuthor(Long postId){
        Post post = boardRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException(BoardErrorMessages.POST_NOT_FOUND));
        return MemberRespDto.of(post.getMember()).getId();
    }
}
