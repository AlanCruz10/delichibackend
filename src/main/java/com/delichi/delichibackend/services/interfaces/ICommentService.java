package com.delichi.delichibackend.services.interfaces;

import com.delichi.delichibackend.controllers.dtos.request.CreateCommentRequest;
import com.delichi.delichibackend.controllers.dtos.request.UpdateCommentRequest;
import com.delichi.delichibackend.controllers.dtos.responses.BaseResponse;
import com.delichi.delichibackend.controllers.dtos.responses.CommentResponse;
import com.delichi.delichibackend.entities.Comment;

public interface ICommentService {
    BaseResponse get(Long id);

    BaseResponse listAlCommentsByRestaurantId(Long id);

    BaseResponse create(CreateCommentRequest request, Long userId, Long restaurantId);

    BaseResponse update(UpdateCommentRequest request, Long id);

    BaseResponse delete(Long id);

    CommentResponse fromCommentToCommentResponse(Comment comment);
}
