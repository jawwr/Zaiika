package com.project.zaiika.controllers.advice;

import com.project.zaiika.exceptions.PermissionDeniedException;
import com.project.zaiika.models.utils.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerController {
    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<?> handlePermissionDenied(PermissionDeniedException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseMessage(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error("some exception!!! " + e.getMessage());
        return ResponseEntity.badRequest().build();
    }
}
