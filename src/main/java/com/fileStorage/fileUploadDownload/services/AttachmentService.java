package com.fileStorage.fileUploadDownload.services;

import com.fileStorage.fileUploadDownload.models.Attachment;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.InvalidObjectException;
import java.nio.file.FileAlreadyExistsException;

public interface AttachmentService {
    public Attachment saveFile(MultipartFile file) throws Exception;
    public Attachment getFile(String fileId) throws Exception;
}
