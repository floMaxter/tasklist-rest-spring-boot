package com.projects.tasklist.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ExceptionBody {

    private final String message;

    private Map<String, String> errors;

    public ExceptionBody(final String message) {
        this.message = message;
    }
}
