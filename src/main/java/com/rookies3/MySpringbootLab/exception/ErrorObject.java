package com.rookies3.MySpringbootLab.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorObject {
    private String error;
    private String message;
}
