package com.delichi.delichibackend.entities.exceptions;

public class ExistingDataConflictException extends RuntimeException{
    public ExistingDataConflictException(){
        super("Exist Data Conflict");
    }
}
