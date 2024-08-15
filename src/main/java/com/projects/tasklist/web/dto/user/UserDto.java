package com.projects.tasklist.web.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.projects.tasklist.web.dto.validation.OnCreate;
import com.projects.tasklist.web.dto.validation.OnUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserDto {

    @NotNull(message = "Id shouldn't be null", groups = OnUpdate.class)
    private Long id;


    @NotNull(message = "Name shouldn't be null",
            groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "The length of the name should be less than 255 characters",
            groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotNull(message = "Username shouldn't be null",
            groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "The length of the username should be less than 255 characters",
            groups = {OnCreate.class, OnUpdate.class})
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password shouldn't be null",
            groups = {OnCreate.class, OnUpdate.class})
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password shouldn't be null",
            groups = {OnCreate.class})
    private String passwordInformation;
}
