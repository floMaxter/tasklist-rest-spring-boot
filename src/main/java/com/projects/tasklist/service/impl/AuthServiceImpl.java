package com.projects.tasklist.service.impl;

import com.projects.tasklist.service.AuthService;
import com.projects.tasklist.web.dto.auth.JwtRequest;
import com.projects.tasklist.web.dto.auth.JwtResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        return null;
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        return null;
    }
}
