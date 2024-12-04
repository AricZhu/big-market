package com.platform.bigmarket.domain.strategy.model.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RuleAction {
    ALLOW("allow", "放行"),
    TAKE_OVER("take_over", "接管")
    ;
    private String code;
    private String desc;
}
