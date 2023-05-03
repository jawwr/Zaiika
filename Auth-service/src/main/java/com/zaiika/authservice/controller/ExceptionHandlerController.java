package com.zaiika.authservice.controller;

import com.zaiika.authservice.model.util.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerController {
//    @ExceptionHandler(PermissionDeniedException.class)
//    public ResponseEntity<?> handlePermissionDenied(PermissionDeniedException e) {
//        log.error(e.getMessage());
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseMessage(e.getMessage()));
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage()));
    }
}
