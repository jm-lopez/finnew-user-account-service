package com.juanmatiaslopez.user_account_service.exception;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String msg) {
        super(msg);
    }
}
