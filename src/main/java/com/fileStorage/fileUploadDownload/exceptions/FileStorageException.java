package com.fileStorage.fileUploadDownload.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class FileStorageException extends Exception{
    private HttpStatus status;
    private String message;

    public FileStorageException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
