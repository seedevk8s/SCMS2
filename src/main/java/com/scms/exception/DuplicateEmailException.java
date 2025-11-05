package com.scms.exception;

/**
 * 중복된 이메일
 * 요구사항 ID: CMN-002
 */
public class DuplicateEmailException extends BusinessException {

    public DuplicateEmailException() {
        super(ErrorCode.DUPLICATE_EMAIL);
    }

    public DuplicateEmailException(String message) {
        super(ErrorCode.DUPLICATE_EMAIL, message);
    }

}
