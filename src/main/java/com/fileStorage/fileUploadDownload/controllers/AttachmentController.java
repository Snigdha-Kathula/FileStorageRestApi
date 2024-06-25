package com.fileStorage.fileUploadDownload.controllers;

import com.fileStorage.fileUploadDownload.dtos.ResponseData;
import com.fileStorage.fileUploadDownload.exceptions.FileStorageException;
import com.fileStorage.fileUploadDownload.models.Attachment;
import com.fileStorage.fileUploadDownload.services.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/file/v1")
public class AttachmentController {

    private AttachmentService fileService;
    @Autowired
    public AttachmentController(AttachmentService fileService) {

        this.fileService = fileService;
    }

    @PostMapping("/upload")
     public ResponseData uploadFile(@RequestParam("File")MultipartFile file) throws Exception {
        Attachment newFile = null;
        String downloadURL = "";
        try{
            newFile = fileService.saveFile(file);
        }catch (FileStorageException e){
            throw new FileStorageException(e.getStatus(), e.getMessage());
        }catch (NullPointerException e){
            throw new NullPointerException(e.getMessage());
        }

        downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/").path(newFile.getId().toString()).toUriString();
        return new ResponseData(newFile.getFileName(), downloadURL, file.getContentType(), file.getSize());
    }
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws Exception {
        Attachment newFile =null;
        newFile = fileService.getFile(fileId);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(newFile.getFileType())).header(HttpHeaders.CONTENT_DISPOSITION, "file; filename=\"" + newFile.getFileName() +"\"").body(new ByteArrayResource(newFile.getData()));
    }

}
