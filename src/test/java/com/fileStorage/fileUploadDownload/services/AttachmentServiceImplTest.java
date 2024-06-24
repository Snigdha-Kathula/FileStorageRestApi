package com.fileStorage.fileUploadDownload.services;

import com.fileStorage.fileUploadDownload.exceptions.FileStorageException;
import com.fileStorage.fileUploadDownload.models.Attachment;
import com.fileStorage.fileUploadDownload.repositories.AttachmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AttachmentServiceImplTest {
    @MockBean
    private AttachmentRepository attachmentRepository;
    @Autowired
    private AttachmentService attachmentService;
//    @Test
//    public void savesFile_ReturnSavedFile(){
//        Attachment attachment = new Attachment(ramu, txt,)
//    }
    @Test
    public void testSaveFile_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello, World!".getBytes());

        when(attachmentRepository.existsByfileName(file.getOriginalFilename())).thenReturn(false);
        when(attachmentRepository.save(any(Attachment.class))).thenAnswer(i -> i.getArguments()[0]);

        Attachment savedFile = attachmentService.saveFile(file);

        assertNotNull(savedFile);
        assertEquals("test.txt", savedFile.getFileName());
        assertEquals("text/plain", savedFile.getFileType());
        assertArrayEquals("Hello, World!".getBytes(), savedFile.getData());
    }
    @Test
    public void testSaveFile_EmptyFileName() {
        MockMultipartFile file = new MockMultipartFile("file", "", "text/plain", "Hello, World!".getBytes());

        FileStorageException exception = assertThrows(FileStorageException.class, () -> {
            attachmentService.saveFile(file);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("File was not provided", exception.getMessage());
    }
    @Test
    public void testSaveFile_InvalidPathSequence() {
        MockMultipartFile file = new MockMultipartFile("file", "../test.txt", "text/plain", "Hello, World!".getBytes());

        FileStorageException exception = assertThrows(FileStorageException.class, () -> {
            attachmentService.saveFile(file);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Filename contains invalid path sequence ../test.txt", exception.getMessage());
    }
    @Test
    public void testSaveFile_FileNameAlreadyExists() {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello, World!".getBytes());

        when(attachmentRepository.existsByfileName(file.getOriginalFilename())).thenReturn(true);

        FileStorageException exception = assertThrows(FileStorageException.class, () -> {
            attachmentService.saveFile(file);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Filename already Exist. Rename it and upload test.txt", exception.getMessage());
    }

    @Test
    public void testGetFile_Success() throws Exception {
        String fileId = "1";
        Attachment attachment = new Attachment("test.txt", "text/plain", "Hello, World!".getBytes());
        when(attachmentRepository.findById(Long.valueOf(fileId))).thenReturn(Optional.of(attachment));

        Attachment result = attachmentService.getFile(fileId);

        assertNotNull(result);
        assertEquals("test.txt", result.getFileName());
        assertEquals("text/plain", result.getFileType());
        assertArrayEquals("Hello, World!".getBytes(), result.getData());
    }
    @Test
    public void testGetFile_FileNotFound() {
        String fileId = "1";
        when(attachmentRepository.findById(Long.valueOf(fileId))).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            attachmentService.getFile(fileId);
        });

        assertEquals("File not Found with Id " + fileId, exception.getMessage());
    }
}
