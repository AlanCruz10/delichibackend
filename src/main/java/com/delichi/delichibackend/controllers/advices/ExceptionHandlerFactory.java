package com.delichi.delichibackend.controllers.advices;

import com.delichi.delichibackend.controllers.dtos.responses.BaseResponse;
import com.delichi.delichibackend.entities.exceptions.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerFactory {

    @ExceptionHandler(DataIntegrityViolationException.class)
    private ResponseEntity<BaseResponse> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        BaseResponse errorResponse = BaseResponse.builder()
                .message(exception.getLocalizedMessage())
                .success(false)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();
        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
    }

    @ExceptionHandler(NotFoundException.class)
    private ResponseEntity<BaseResponse> handleNotFoundException(NotFoundException exception) {
        BaseResponse errorResponse = BaseResponse.builder()
                .message(exception.getLocalizedMessage())
                .success(false)
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
    }

    @ExceptionHandler(ExistingDataConflictException.class)
    private ResponseEntity<BaseResponse> handleExistingDataConflictException(ExistingDataConflictException exception) {
        BaseResponse errorResponse = BaseResponse.builder()
                .message(exception.getLocalizedMessage())
                .success(false)
                .httpStatus(HttpStatus.CONFLICT)
                .build();
        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
    }


    @ExceptionHandler(InternalServerError.class)
    private ResponseEntity<BaseResponse> handleInternalServerError(InternalServerError exception) {
        BaseResponse errorResponse = BaseResponse.builder()
                .message(exception.getLocalizedMessage())
                .success(false)
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
    }

    @ExceptionHandler(NotValidException.class)
    private ResponseEntity<BaseResponse> handleNotValidException(NotValidException exception) {
        BaseResponse errorResponse = BaseResponse.builder()
                .message(exception.getLocalizedMessage())
                .success(false)
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
    }

    @ExceptionHandler(InvalidDataReservation.class)
    private ResponseEntity<BaseResponse> handleInvalidDataReservation(InvalidDataReservation exception) {
        BaseResponse errorResponse = BaseResponse.builder()
                .message(exception.getLocalizedMessage())
                .success(false)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();
        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
    }

    @ExceptionHandler(ExceptionTest.class)
    private ResponseEntity<BaseResponse> handleExceptionTest(ExceptionTest exception) {
        BaseResponse errorResponse = BaseResponse.builder()
                .message(exception.getLocalizedMessage())
                .success(false)
                .httpStatus(HttpStatus.FORBIDDEN)
                .build();
        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
    }

}