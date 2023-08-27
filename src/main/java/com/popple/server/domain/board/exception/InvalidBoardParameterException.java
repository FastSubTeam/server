package com.popple.server.domain.board.exception;

public class InvalidBoardParameterException extends RuntimeException{
    public InvalidBoardParameterException(String message) {
        super(message);
    }
}
