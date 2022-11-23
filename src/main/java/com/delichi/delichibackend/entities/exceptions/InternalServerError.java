package com.delichi.delichibackend.entities.exceptions;

public class InternalServerError extends RuntimeException{
    public InternalServerError(){
        super("Internal Server Error");
    }
}
