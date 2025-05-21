package com.rookies3.MySpringbootLab.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // ✅ 공통 오류
    RESOURCE_NOT_FOUND("%s not found with %s: %s", HttpStatus.NOT_FOUND),
    RESOURCE_DUPLICATE("%s already exists with %s: %s", HttpStatus.CONFLICT),
    RESOURCE_ALREADY_EXISTS("%s already exists: %s", HttpStatus.CONFLICT),

    // ✅ 학생 관련 오류
    STUDENT_NUMBER_DUPLICATE("Student already exists with student number: %s", HttpStatus.CONFLICT),

    // ✅ 학생 상세정보 관련 오류
    EMAIL_DUPLICATE("Student detail already exists with email: %s", HttpStatus.CONFLICT),
    PHONE_NUMBER_DUPLICATE("Student detail already exists with phone number: %s", HttpStatus.CONFLICT),

    // ✅ 도서 관련 오류
    ISBN_DUPLICATE("Book already exists with ISBN: %s", HttpStatus.CONFLICT),

    // ✅ 출판사 관련 오류
    PUBLISHER_NOT_FOUND("Publisher not found with %s: %s", HttpStatus.NOT_FOUND),
    PUBLISHER_NAME_DUPLICATE("Publisher already exists with name: %s", HttpStatus.CONFLICT),
    PUBLISHER_HAS_BOOKS("Cannot delete publisher with id: %s. It has %s books", HttpStatus.CONFLICT),

    // ✅ 학과 관련 오류
    DEPARTMENT_CODE_DUPLICATE("Department already exists with code: %s", HttpStatus.CONFLICT),
    DEPARTMENT_NAME_DUPLICATE("Department already exists with name: %s", HttpStatus.CONFLICT),
    DEPARTMENT_HAS_STUDENTS("Cannot delete department with id: %s. It has %s students", HttpStatus.CONFLICT);

    // 🔽 필드 및 생성자
    private final String messageTemplate;
    private final HttpStatus httpStatus;

    ErrorCode(String messageTemplate, HttpStatus httpStatus) {
        this.messageTemplate = messageTemplate;
        this.httpStatus = httpStatus;
    }

    public String formatMessage(Object... args) {
        return String.format(messageTemplate, args);
    }
}
