package com.projects.tasklist.service;

import com.projects.tasklist.web.dto.auth.JwtRequest;
import com.projects.tasklist.web.dto.auth.JwtResponse;

public interface AuthService {

    JwtResponse login(JwtRequest loginRequest);

    JwtResponse refresh(String refreshToken);
}
