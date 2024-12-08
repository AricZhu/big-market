package com.platform.bigmarket.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleTree {
    private Integer treeId;
    private String treeName;
    private String treeDesc;
    private String rootNode;

    private Map<String, RuleTreeNode> treeNodeMap;
}
