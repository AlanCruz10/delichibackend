package com.delichi.delichibackend.controllers.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class ReservationResponse {
    private Long id;
    private String date;
    private Integer people;
}
