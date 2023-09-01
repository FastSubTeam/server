package com.popple.server.domain.board.exception;

public class BoardErrorMessages {
    public static final String COMMENT_AUTHOR_MISMATCH = "댓글 작성자와 로그인된 멤버와 일치하지 않습니다.";
    public static final String POST_AUTHOR_MISMATCH = "게시글 작성자와 로그인된 멤버와 일치하지 않습니다.";
    public static final String USERNAME_NOT_FOUND = "유저정보가 존재하지 않습니다.";
    public static final String POST_NOT_FOUND = "게시물이 존재하지 않습니다.";
    public static final String COMMENT_NOT_FOUND = "댓글이 존재하지 않습니다.";
    public static final String MISSING_POST = "댓글을 추가할 게시물이 존재하지 않습니다.";
}
