package com.fileStorage.fileUploadDownload.services;

import com.fileStorage.fileUploadDownload.exceptions.FileStorageException;
import com.fileStorage.fileUploadDownload.models.Attachment;
import com.fileStorage.fileUploadDownload.repositories.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
public class AttachmentServiceImpl implements AttachmentService{
    private AttachmentRepository fileRepository;
   @Autowired
    public AttachmentServiceImpl(AttachmentRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override

    public Attachment saveFile(MultipartFile file) throws FileStorageException, IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (fileName.contains("..")) {
            throw new FileStorageException(HttpStatus.BAD_REQUEST, "Filename contains invalid path sequence " + fileName);
        }
        if(fileRepository.existsByfileName(fileName)){
            throw new FileStorageException(HttpStatus.BAD_REQUEST, "Filename already Exist. Rename it and upload "+fileName);
        }
//        long maxValueSize=10*1024*1024;
//        if(file.getSize() > maxValueSize){
//            throw new MaxUploadSizeExceededException(HttpStatus.)
//        }
        Attachment newFile = new Attachment(fileName, file.getContentType(), file.getBytes());
        return fileRepository.save(newFile);
    }

    @Override
    public Attachment getFile(String fileId) throws Exception {
        return fileRepository.findById(Long.valueOf(fileId)).orElseThrow(()->new Exception("File not Found with Id "+fileId));

    }


}