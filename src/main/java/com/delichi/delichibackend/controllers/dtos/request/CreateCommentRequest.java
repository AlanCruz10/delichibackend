package com.delichi.delichibackend.controllers.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class CreateCommentRequest {
    private String date;
    private String content;
    private Integer score;
}
