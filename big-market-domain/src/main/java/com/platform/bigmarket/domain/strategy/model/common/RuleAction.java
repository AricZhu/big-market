package com.platform.bigmarket.domain.strategy.model.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RuleAction {
    ALLOW("ALLOW", "放行"),
    TAKE_OVER("TAKE_OVER", "接管")
    ;
    private String code;
    private String desc;
}
