package com.fullstack.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorResponse {
    private String field;
    private Object rejectedValue;
    private String message;

    public ValidationErrorResponse(String field, String message){
        this.field = field;
        this.message= message;
        this.rejectedValue = null;
    }

    public static ValidationErrorResponse of (String field, String message){
        return new ValidationErrorResponse(field, message);
    }

    public static ValidationErrorResponse of (String field, Object rejectedValue, String message){
        return new ValidationErrorResponse(field, rejectedValue, message);
    }

}
