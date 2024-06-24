package com.fileStorage.fileUploadDownload.services;

import com.fileStorage.fileUploadDownload.dtos.LoginDto;
import com.fileStorage.fileUploadDownload.dtos.RegisterDto;
import com.fileStorage.fileUploadDownload.exceptions.FileStorageException;

public interface AuthService {
    String login(LoginDto loginDto);
    String register(RegisterDto registerDto) throws FileStorageException;
}