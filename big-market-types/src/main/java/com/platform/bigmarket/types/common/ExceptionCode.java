package com.platform.bigmarket.types.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {
    ILLEGAL_PARAMS("1001", "参数错误"),
    NULL_POINTER("1002", "返回为空")
    ;
    private String code;
    private String desc;
}
