package com.delichi.delichibackend.controllers.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class UpdateCommentRequest {
    private String date;
    private String content;
    private Integer score;
}
