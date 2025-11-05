package com.scms.exception;

/**
 * 사용자를 찾을 수 없음
 * 요구사항 ID: CMN-002
 */
public class UserNotFoundException extends BusinessException {

    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }

    public UserNotFoundException(String message) {
        super(ErrorCode.USER_NOT_FOUND, message);
    }

}
