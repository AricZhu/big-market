package com.platform.bigmarket.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleTreeNodeDTO {
    private String treeId;
    private String ruleKey;
    private String ruleValue;
    private String ruleDesc;
    private List<RuleTreeLineDTO> ruleTreeLineDTOList;
}
