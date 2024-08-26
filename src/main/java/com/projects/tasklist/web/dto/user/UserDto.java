package com.projects.tasklist.web.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.projects.tasklist.web.dto.validation.OnCreate;
import com.projects.tasklist.web.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(description = "User DTO")
public class UserDto {

    @Schema(description = "User id", example = "1")
    @NotNull(message = "Id shouldn't be null", groups = OnUpdate.class)
    private Long id;

    @Schema(description = "User name", example = "Oleg")
    @NotNull(message = "Name shouldn't be null",
            groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "The length of the name should be less than 255 characters",
            groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @Schema(description = "User email", example = "oleg@gmail.com")
    @NotNull(message = "Username shouldn't be null",
            groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "The length of the username should be less than 255 characters",
            groups = {OnCreate.class, OnUpdate.class})
    private String username;

    @Schema(description = "User encrypted password", example = "12345")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password shouldn't be null",
            groups = {OnCreate.class, OnUpdate.class})
    private String password;

    @Schema(description = "User password confirmation", example = "12345")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password confirmation shouldn't be null",
            groups = {OnCreate.class})
    private String passwordConfirmation;
}
