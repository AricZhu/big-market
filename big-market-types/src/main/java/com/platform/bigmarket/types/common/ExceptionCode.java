package com.platform.bigmarket.types.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ExceptionCode {
    ILLEGAL_PARAMS("1001", "参数错误"),
    NULL_POINTER("1002", "返回为空"),
    ERROR_TREE_RULE_KEY("1003", "规则树节点key配置错误"),
    NOT_FOUND_RULE_TREE("1004", "找不到规则树")
    ;
    private String code;
    private String desc;
}
