package com.delichi.delichibackend.controllers.dtos.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;

@Setter
@Getter
public class CreateReservationRequest {
    @NotNull
    private String date;
    @NotNull
    private String hour;
    @NotNull
    private Integer people;
}
