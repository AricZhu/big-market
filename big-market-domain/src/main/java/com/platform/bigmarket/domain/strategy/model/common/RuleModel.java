package com.platform.bigmarket.domain.strategy.model.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RuleModel {
    BLACK_LIST("rule_blacklist", "黑名单"),
    WEIGHT("rule_weight", "权重"),
    DEFAULT("default", "默认"),
    RULE_LOCK("rule_lock", "x次解锁")
    ;
    private String code;
    private String desc;
}
