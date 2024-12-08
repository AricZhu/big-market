package com.platform.bigmarket.domain.strategy.model.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NodeLineExpressionType {
    EQUAL("equal", "等于"),
    GT("great_then", "大于"),
    LT("less_then", "小于"),
    GTE("great_equal_then", "大于等于"),
    LTE("less_equal_then", "小于等于"),
    ENUM("enum", "枚举")
    ;
    private String code;
    private String desc;
}
