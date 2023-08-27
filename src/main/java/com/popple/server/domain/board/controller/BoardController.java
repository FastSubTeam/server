package com.popple.server.domain.board.controller;

import com.popple.server.common.dto.APIDataResponse;
import com.popple.server.domain.board.dto.*;
import com.popple.server.domain.board.exception.UnauthorizedAccessException;
import com.popple.server.domain.board.service.BoardService;
import com.popple.server.domain.entity.Member;
import com.popple.server.domain.entity.Post;
import com.popple.server.domain.user.annotation.LoginActor;
import com.popple.server.domain.user.vo.Actor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api/board")
@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    //전체 게시글
    @GetMapping("/all")
    public BoardAPIDataResponse<List<BoardListRespDto>> getAllPosts() {
        //서비스 메서드 호출
        List<Post> posts = boardService.getAllPosts();
        List<BoardListRespDto> boardListRespDtoList = createListOfBoardListRespDto(posts);
        return BoardAPIDataResponse.of(HttpStatus.OK, boardListRespDtoList, (long) posts.size());
    }

    @GetMapping()
    public BoardAPIDataResponse<List<BoardListRespDto>> getPostsByPage(@PageableDefault Pageable pageable) {
        log.info("페이지네이션 컨트롤러 진입");
        Page<Post> postsByPage = boardService.getPostsByPage(pageable);
        List<Post> contents = postsByPage.getContent();
        log.info("contetns: {}", contents);
        List<BoardListRespDto> boardListRespDtoList = createListOfBoardListRespDto(contents);
        return BoardAPIDataResponse.of(HttpStatus.OK, boardListRespDtoList, postsByPage.getTotalElements());
    }

    @GetMapping("/{postId}")
    public APIDataResponse<PostRespDto> getPostById(@PathVariable Long postId) {
        Post post = boardService.getPostById(postId);
        return APIDataResponse.of(HttpStatus.OK, buildPostRespDto(post));
    }

    @PostMapping("/write")
    public APIDataResponse<?> savePost(@RequestBody @Valid PostReqDto postReqDto,
                                       BindingResult bindingResult,
                                       @LoginActor Actor loginMember) {

        validateLoginMember(loginMember);
        checkValidationError(bindingResult);
        Member member = boardService.getMember(loginMember.getId());
        Post post = postReqDto.toEntity(member);
        Post savedPost = boardService.savePost(post);
        return APIDataResponse.of(HttpStatus.OK, buildPostRespDto(savedPost));
    }

    private PostRespDto buildPostRespDto(Post post) {
        return PostRespDto.builder()
                .id(post.getId())
                .email(post.getMember().getEmail())
                .nickname(post.getMember().getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .comments(boardService.getAllCommentsByPostId(post.getId()))
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    @PatchMapping("/{postId}")
    public APIDataResponse<?> updatePost(@RequestBody @Valid PostReqDto postReqDto,
                                         BindingResult bindingResult,
                                         @PathVariable Long postId,
                                         @LoginActor Actor loginMember) {
        checkPostAuthor(loginMember, postId);
        validateLoginMember(loginMember);
        checkValidationError(bindingResult);
        boardService.updatePost(postId, postReqDto);
        Post post = boardService.getPostById(postId);
        return APIDataResponse.of(HttpStatus.OK, buildPostRespDto(post));
    }

    private List<BoardListRespDto> createListOfBoardListRespDto(List<Post> posts) {
        List<BoardListRespDto> boardListRespDtoList = new ArrayList<>();
        for (Post post : posts) {
            int commentCount = boardService.getAllCommentsByPostId(post.getId()).size();
            BoardListRespDto boardListRespDto = BoardListRespDto.builder()
                    //Todo builder사용하는 코드 -> 객체 내부에 정의해 코드 라인 수 줄이기
                    .id(post.getId())
                    .email(post.getMember().getEmail())
                    .nickname(post.getMember().getNickname())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .commentCount(commentCount)
                    .createdAt(post.getCreatedAt())
                    .updatedAt(post.getUpdatedAt())
                    .build();
            boardListRespDtoList.add(boardListRespDto);
        }
        return boardListRespDtoList;
    }

    @DeleteMapping("/{postId}")
    public APIDataResponse<?> deletePost(@PathVariable Long postId,
                                         @LoginActor Actor loginMember) throws IllegalArgumentException {
        checkPostAuthor(loginMember, postId);
        boardService.deletePost(postId);
        return APIDataResponse.empty(HttpStatus.OK);
    }

    @DeleteMapping("/comment/{commentId}")
    public APIDataResponse<?> deleteComment(@PathVariable Long commentId,
                                            @LoginActor Actor loginMember) throws IllegalArgumentException {
        checkCommentAuthor(loginMember, commentId);
        validateLoginMember(loginMember);
        boardService.deleteComment(commentId);
        return APIDataResponse.empty(HttpStatus.OK);
    }

    @PatchMapping("comment/{commentId}")
    public APIDataResponse<?> updateComment(@RequestBody @Valid CommentReqDto commentReqDto,
                                            BindingResult bindingResult,
                                            @PathVariable Long commentId,
                                            @LoginActor Actor loginMember) throws IllegalArgumentException {
        checkCommentAuthor(loginMember, commentId);
        validateLoginMember(loginMember);
        checkValidationError(bindingResult);
        Member member = boardService.getMember(loginMember.getId());
        CommentRespDto commentRespDto = boardService.updateComment(commentId, commentReqDto);
        return APIDataResponse.of(HttpStatus.OK, commentRespDto);
    }

    @PostMapping("/{postId}/comment")
    public APIDataResponse<?> saveComment(@RequestBody @Valid CommentReqDto commentReqDto,
                                          BindingResult bindingResult,
                                          @PathVariable Long postId,
                                          @LoginActor Actor loginMember) {
        validateLoginMember(loginMember);
        checkValidationError(bindingResult);
        Member member = boardService.getMember(loginMember.getId());
        CommentRespDto commentRespDto = boardService.saveComment(postId, member, commentReqDto);
        return APIDataResponse.of(HttpStatus.OK, commentRespDto);
    }

    private void validateLoginMember(Actor loginMember) {
        if (loginMember == null || loginMember.getId() == null) {
            throw new IllegalArgumentException("유저정보가 유효하지 않습니다.");
        }
    }

    private void checkCommentAuthor(Actor loginMember, Long commentId) {
        if (!loginMember.getId().equals(boardService.getCommentAuthor(commentId))) {
            throw new UnauthorizedAccessException("댓글 작성자와 로그인된 멤버와 일치하지 않습니다.");
        }
    }

    private void checkPostAuthor(Actor loginMember, Long postId) {
        if (!loginMember.getId().equals(boardService.getPostAuthor(postId))) {
            throw new UnauthorizedAccessException("게시글 작성자와 로그인된 멤버와 일치하지 않습니다.");
        }
    }

    private void checkValidationError(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("유효하지 않은 요청 파라미터입니다.");
        }
    }
}