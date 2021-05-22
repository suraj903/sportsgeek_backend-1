package com.project.sportsgeek.exception;

import com.project.sportsgeek.response.ResponseMessage;

public class ResponseException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    ResponseMessage responseMessage;

    public ResponseException(ResponseMessage responseMessage){
        super(responseMessage.getMessage());
        this.responseMessage = responseMessage;
    }

    public ResponseMessage getResponseMessage() {
        return responseMessage;
    }
}
