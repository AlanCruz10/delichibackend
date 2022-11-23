package com.delichi.delichibackend.controllers.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateReservationRequest {
    private String date;
    private String hour;
    private Integer people;
}
