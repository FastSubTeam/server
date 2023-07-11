package com.popple.server.domain.board.controller;

import com.popple.server.common.dto.APIDataResponse;
import com.popple.server.domain.board.dto.BoardListRespDto;
import com.popple.server.domain.board.service.BoardService;
import com.popple.server.domain.entity.Comment;
import com.popple.server.domain.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    //전체 게시글
    @RequestMapping("/board")
    public APIDataResponse<List<BoardListRespDto>> getAllPosts() {
        List<BoardListRespDto> boardListRespDtoList = new ArrayList<>();

        //서비스 메서드 호출
        List<Post> posts = boardService.getAllPosts();
        for (Post post : posts) {
            List<Comment> comments = boardService.getAllCommentsByPostId(post.getId());
            int commentCount = comments.size();
            BoardListRespDto boardListRespDto = new BoardListRespDto(
                    post.getUser().getNickname(),
                    post.getTitle(),
                    post.getContent(),
                    post.getCreatedAt(),
                    post.getUpdatedAt(),
                    commentCount);
            boardListRespDtoList.add(boardListRespDto);
        }
        return APIDataResponse.of(HttpStatus.OK, boardListRespDtoList);
    }
}
