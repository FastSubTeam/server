package com.popple.server.domain.board.controller;

import com.popple.server.common.dto.APIDataResponse;
import com.popple.server.domain.board.dto.BoardAPIDataResponse;
import com.popple.server.domain.board.dto.BoardListRespDto;
import com.popple.server.domain.board.service.BoardService;
import com.popple.server.domain.entity.Comment;
import com.popple.server.domain.entity.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    //전체 게시글
    @RequestMapping("/board/all")
    public BoardAPIDataResponse<List<BoardListRespDto>> getAllPosts() {
        //서비스 메서드 호출
        List<Post> posts = boardService.getAllPosts();
        List<BoardListRespDto> boardListRespDtoList = createListOfBoardListRespDto(posts);
        return BoardAPIDataResponse.of(HttpStatus.OK, boardListRespDtoList, posts.size());
    }

    @RequestMapping("/board")
    public BoardAPIDataResponse<List<BoardListRespDto>> getPostsByPage(Pageable pageable) {
        Page<Post> postsByPage = boardService.getPostsByPage(pageable);
        List<Post> contents = postsByPage.getContent();
        List<BoardListRespDto> boardListRespDtoList = createListOfBoardListRespDto(contents);
        return BoardAPIDataResponse.of(HttpStatus.OK, boardListRespDtoList, (int)postsByPage.getTotalElements());
    }

    private List<BoardListRespDto> createListOfBoardListRespDto(List<Post> posts){
        List<BoardListRespDto> boardListRespDtoList = new ArrayList<>();
        for (Post post : posts) {
            List<Comment> comments = boardService.getAllCommentsByPostId(post.getId());
            int commentCount = comments.size();
            BoardListRespDto boardListRespDto = BoardListRespDto.builder()
                    .id(post.getId())
                    .nickname(post.getMember().getNickname())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .createdAt(post.getCreatedAt())
                    .updatedAt(post.getUpdatedAt())
                    .commentCount(commentCount)
                    .build();
            boardListRespDtoList.add(boardListRespDto);
        }
        return boardListRespDtoList;
    }
}
