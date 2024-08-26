package com.projects.tasklist.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Request for login")
public class JwtRequest {

    @Schema(description = "email", example = "kupzovd@gmail.com")
    @NotNull(message = "Username shouldn't be null")
    private String username;

    @Schema(description = "password", example = "12345")
    @NotNull(message = "Password shouldn't be null")
    private String password;
}
