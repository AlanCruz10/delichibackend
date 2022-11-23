package com.delichi.delichibackend.entities.exceptions;

public class InvalidDataReservation extends RuntimeException{
    public InvalidDataReservation(){
        super("Data Reservation Not Valid");
    }
}
