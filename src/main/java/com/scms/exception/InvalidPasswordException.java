package com.scms.exception;

/**
 * 잘못된 비밀번호
 * 요구사항 ID: CMN-002
 */
public class InvalidPasswordException extends BusinessException {

    public InvalidPasswordException() {
        super(ErrorCode.INVALID_PASSWORD);
    }

    public InvalidPasswordException(String message) {
        super(ErrorCode.INVALID_PASSWORD, message);
    }

}
