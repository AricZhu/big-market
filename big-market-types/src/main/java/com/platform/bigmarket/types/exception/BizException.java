package com.platform.bigmarket.types.exception;

import com.platform.bigmarket.types.common.ExceptionCode;

public class BizException extends RuntimeException {
    private final String code;
    private final String desc;

    public BizException(String code) {
        this.code = code;
        this.desc = "";
    }

    public BizException(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public BizException(ExceptionCode exceptionCode) {
        this.code = exceptionCode.getCode();
        this.desc = exceptionCode.getDesc();
    }

    @Override
    public String toString() {
        return "BizException{" +
                "code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
