package com.platform.bigmarket.domain.strategy.model.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NodeLineExpressionType {
    EQUAL("EQUAL", "等于"),
    GT("GT", "大于"),
    LT("LT", "小于"),
    GTE("GTE", "大于等于"),
    LTE("LTE", "小于等于"),
    ENUM("ENUM", "枚举")
    ;
    private String code;
    private String desc;
}
