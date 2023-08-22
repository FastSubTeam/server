package com.popple.server.domain.board.controller;

import com.popple.server.common.dto.APIDataResponse;
import com.popple.server.domain.board.dto.*;
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

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
        Page<Post> postsByPage = boardService.getPostsByPage(pageable);
        List<Post> contents = postsByPage.getContent();
        List<BoardListRespDto> boardListRespDtoList = createListOfBoardListRespDto(contents);
        return BoardAPIDataResponse.of(HttpStatus.OK, boardListRespDtoList, postsByPage.getTotalElements());
    }

    @GetMapping("/{postId}")
    public APIDataResponse<PostRespDto> getPostById(@PathVariable Long postId) {
        try {
            Post post = boardService.getPostById(postId);
            List<CommentDto> commentDtos = boardService.getAllCommentsByPostId(postId);
            PostRespDto postRespDto = PostRespDto.builder()
                    .id(post.getId())
                    .nickname(post.getMember().getNickname())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .comments(commentDtos)
                    .createdAt(post.getCreatedAt())
                    .updatedAt(post.getUpdatedAt())
                    .build();
            return APIDataResponse.of(HttpStatus.OK, postRespDto);
        } catch (NoSuchElementException e) {
            //Error응답
            log.error(e.getMessage());
        }
        return null;
    }

    @PostMapping("/write")
    public APIDataResponse<?> savePost(@RequestBody PostReqDto postReqDto, BindingResult bindingResult, @LoginActor Actor loginMember) {
        validateLoginMember(loginMember);
        Member member = boardService.getMember(loginMember.getId());
        Post post = postReqDto.toEntity(member);
        boardService.savePost(post);
        return APIDataResponse.empty(HttpStatus.OK);
    }

    @PatchMapping("/{postId}")
    public APIDataResponse<?> updatePost(@RequestBody PostReqDto postReqDto, @PathVariable Long postId, @LoginActor Actor loginMember) {
        validateLoginMember(loginMember);
        //todo 로그인된 유저와 게시글의 작성자가 일치하는지 로직 작성
        Member member = boardService.getMember(loginMember.getId());
        boardService.updatePost(postId, postReqDto);
        return APIDataResponse.empty(HttpStatus.OK);
    }

    private List<BoardListRespDto> createListOfBoardListRespDto(List<Post> posts) {
        List<BoardListRespDto> boardListRespDtoList = new ArrayList<>();
        for (Post post : posts) {
            int commentCount = boardService.getAllCommentsByPostId(post.getId()).size();
            BoardListRespDto boardListRespDto = BoardListRespDto.builder()
                    //Todo builder사용하는 코드 -> 객체 내부에 정의해 코드 라인 수 줄이기
                    .id(post.getId())
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
    public APIDataResponse<?> deletePost(@PathVariable Long postId) throws IllegalArgumentException {
        boardService.deletePost(postId);
        return APIDataResponse.empty(HttpStatus.OK);
    }

    @DeleteMapping("/comment/{commentId}")
    public APIDataResponse<?> deleteComment(@PathVariable Long commentId) throws IllegalArgumentException {
        boardService.deleteComment(commentId);
        return APIDataResponse.empty(HttpStatus.OK);
    }

    @PatchMapping("comment/{commentId}")
    public APIDataResponse<?> updateComment(@RequestBody CommentReqDto commentReqDto,
                                            @PathVariable Long commentId,
                                            @LoginActor Actor loginMember) throws IllegalArgumentException {
        checkCommentAuthor(loginMember, commentId);
        validateLoginMember(loginMember);
        Member member = boardService.getMember(loginMember.getId());
        CommentDto commentDto = boardService.updateComment(commentId, commentReqDto);
        return APIDataResponse.of(HttpStatus.OK, commentDto);
    }

    @PostMapping("/{postId}/comment")
    public APIDataResponse<?> saveComment(@RequestBody CommentReqDto commentReqDto,
                                          @PathVariable Long postId,
                                          @LoginActor Actor loginMember){
        validateLoginMember(loginMember);
        Member member = boardService.getMember(loginMember.getId());
        CommentDto commentDto = boardService.saveComment(postId, member, commentReqDto);
        return APIDataResponse.of(HttpStatus.OK, commentDto);
    }

    private void validateLoginMember(Actor loginMember){
        if (loginMember == null || loginMember.getId() == null) {
            throw new IllegalArgumentException("유저정보가 유효하지 않습니다.");
        }
    }

    private void checkCommentAuthor(Actor loginMember, Long commentId) {
        if (!loginMember.getId().equals(boardService.getCommentAuthor(commentId))) {
            throw new IllegalArgumentException("댓글 작성자와 로그인된 멤버와 일치하지 않습니다.");
        }
    }
}
