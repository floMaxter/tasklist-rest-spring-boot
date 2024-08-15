package com.projects.tasklist.web.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JwtRequest {

    @NotNull(message = "Username shouldn't be null")
    private String username;

    @NotNull(message = "Password shouldn't be null")
    private String password;
}
