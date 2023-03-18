package com.project.zaiika.exceptions;

public class PermissionDeniedException extends RuntimeException{
    public PermissionDeniedException() {
        super("Permission denied");
    }

    public PermissionDeniedException(Throwable cause) {
        super("Permission denied", cause);
    }
}
