package com.juanmatiaslopez.user_account_service.exception;

public class BadRequestException extends RuntimeException{

    public BadRequestException (String msg) {
        super(msg);
    }
}
