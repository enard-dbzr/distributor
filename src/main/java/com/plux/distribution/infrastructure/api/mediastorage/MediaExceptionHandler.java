package com.plux.distribution.infrastructure.api.mediastorage;

import com.plux.distribution.core.mediastorage.application.exception.MediaNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = MediaStorageController.class)
public class MediaExceptionHandler {

    @ExceptionHandler(MediaNotFoundException.class)
    public ResponseEntity<String> handleMediaNotFound(MediaNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.TEXT_PLAIN)
                .body(e.getMessage());
    }
}