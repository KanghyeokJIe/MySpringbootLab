package com.rookies3.MySpringbootLab.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final HttpStatus httpStatus;

    /**
     * 기본 메시지로 예외를 생성 (417 Expectation Failed)
     */
    public BusinessException(String message) {
        this(message, HttpStatus.EXPECTATION_FAILED);
    }

    /**
     * 메시지와 HTTP 상태 코드를 지정하여 예외 생성
     */
    public BusinessException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    /**
     * ErrorCode를 사용하여 메시지 템플릿을 포맷하고 예외 생성
     */
    public BusinessException(ErrorCode errorCode, Object... args) {
        super(errorCode.formatMessage(args));
        this.httpStatus = errorCode.getHttpStatus();
    }
}