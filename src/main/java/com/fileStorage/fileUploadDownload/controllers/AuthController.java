package com.fileStorage.fileUploadDownload.controllers;

import com.fileStorage.fileUploadDownload.dtos.JWTAuthResponse;
import com.fileStorage.fileUploadDownload.dtos.LoginDto;
import com.fileStorage.fileUploadDownload.dtos.RegisterDto;
import com.fileStorage.fileUploadDownload.exceptions.FileStorageException;
import com.fileStorage.fileUploadDownload.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    private AuthService authService;
    public AuthController(AuthService authService){
        this.authService =authService;
    }
    @PostMapping(value = {"login", "signin"})
    public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginDto loginDto){
        String token = authService.login(loginDto);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);
        return ResponseEntity.ok(jwtAuthResponse);
    }
    @PostMapping(value = {"register", "signup"})
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) throws FileStorageException {
        String response = null;
        try{
            response = authService.register(registerDto);
        }catch(FileStorageException e){
            e.getMessage();
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);

        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
