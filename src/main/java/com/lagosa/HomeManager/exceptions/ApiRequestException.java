package com.lagosa.HomeManager.exceptions;

public class ApiRequestException extends RuntimeException{

    public ApiRequestException(String message){
        super(message);
    }

}
