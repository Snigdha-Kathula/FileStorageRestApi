package com.fileStorage.fileUploadDownload.controllers;
import com.fileStorage.fileUploadDownload.exceptions.FileStorageException;
import com.fileStorage.fileUploadDownload.models.Attachment;
import com.fileStorage.fileUploadDownload.services.AttachmentService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AttachmentControllerTest {
    @MockBean
    private AttachmentService fileService;
    @Autowired
    private AttachmentController fileController;
    @Test
    public void testSaveFileSuccess() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.txt");
        when(file.getBytes()).thenReturn("Test content".getBytes());

        Attachment attachment = new Attachment();
        attachment.setId(1L);
        attachment.setFileName("test.txt");

        when(fileService.saveFile(file)).thenReturn(attachment); // Adjust according to your actual logic

        Attachment savedFile = fileService.saveFile(file);

        assertNotNull(savedFile);
        assertEquals("test.txt", savedFile.getFileName());
    }
    @Test
    public void testSaveFileStorageException() throws Exception {
        MultipartFile file = mock(MultipartFile.class);

        when(fileService.saveFile(file)).thenThrow(new FileStorageException(HttpStatus.NOT_FOUND, "Storage error")); // Adjust according to your actual logic

        Exception exception = assertThrows(FileStorageException.class, () -> {
            fileService.saveFile(file);
        });

        assertEquals("Storage error", exception.getMessage());
    }
    @Test
    public void testSaveFileNullPointerException() throws Exception {
        MultipartFile file = mock(MultipartFile.class);

        when(fileService.saveFile(file)).thenThrow(new NullPointerException("Null pointer exception")); // Adjust according to your actual logic

        Exception exception = assertThrows(NullPointerException.class, () -> {
            fileService.saveFile(file);
        });

        assertEquals("Null pointer exception", exception.getMessage());
    }
    @Test
    public void testGetFileSuccess() throws Exception {
        String fileId = "1";
        Attachment attachment = new Attachment();
        attachment.setId(1L);
        attachment.setFileName("test.txt");
        attachment.setFileType("text/plain");
        attachment.setData("Test content".getBytes());

        when(fileService.getFile(fileId)).thenReturn(attachment); // Adjust according to your actual logic

        Attachment retrievedFile = fileService.getFile(fileId);

        assertNotNull(retrievedFile);
        assertEquals("test.txt", retrievedFile.getFileName());
        assertEquals("text/plain", retrievedFile.getFileType());
        assertArrayEquals("Test content".getBytes(), retrievedFile.getData());
    }
    @Test
    public void testGetFileNotFound() throws Exception {
        String fileId = "1";

        when(fileService.getFile(fileId)).thenThrow(new FileNotFoundException("File not found")); // Adjust according to your actual logic

        Exception exception = assertThrows(FileNotFoundException.class, () -> {
            fileService.getFile(fileId);
        });

        assertEquals("File not found", exception.getMessage());
    }

}