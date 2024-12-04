package com.platform.bigmarket.domain.strategy.model.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RuleModel {
    BLACK_LIST("rule_blacklist", "黑名单"),
    WEIGHT("rule_weight", "权重")
    ;
    private String code;
    private String desc;
}
