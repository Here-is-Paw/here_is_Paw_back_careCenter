package com.ll.here_is_paw_back_careCenter.global.exception;

import com.ll.here_is_paw_back_careCenter.global.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
}
