package com.fileStorage.fileUploadDownload.services.impls;

import com.fileStorage.fileUploadDownload.dtos.LoginDto;
import com.fileStorage.fileUploadDownload.dtos.RegisterDto;
import com.fileStorage.fileUploadDownload.exceptions.FileStorageException;
import com.fileStorage.fileUploadDownload.models.Role;
import com.fileStorage.fileUploadDownload.models.User;
import com.fileStorage.fileUploadDownload.repositories.RoleRepository;
import com.fileStorage.fileUploadDownload.repositories.UserRepository;
import com.fileStorage.fileUploadDownload.security.JwtTokenProvider;
import com.fileStorage.fileUploadDownload.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String login(LoginDto loginDto) {
        Authentication authenticate =authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String token = jwtTokenProvider.generateToken(authenticate);

        return token;
    }

    @Override
    public String register(RegisterDto registerDto) throws FileStorageException {
        if(userRepository.existsByUsername(registerDto.getUsername())){
            throw new FileStorageException(HttpStatus.BAD_REQUEST, "Username Already Exist");
        }
        if(userRepository.existsByEmail(registerDto.getEmail())){
            throw new FileStorageException(HttpStatus.BAD_REQUEST,"Email Already Exists!");
        }
        User user = new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(registerDto.getRoleName()).get();
        roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);

        return "User Registered Successfully";
    }
}
