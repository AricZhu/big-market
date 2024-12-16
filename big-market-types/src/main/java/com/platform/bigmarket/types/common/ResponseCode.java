package com.platform.bigmarket.types.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    RESPONSE_SUCCESS("1001", "成功"),
    RESPONSE_ERROR("5001", "失败")
    ;
    private String code;
    private String desc;
}
