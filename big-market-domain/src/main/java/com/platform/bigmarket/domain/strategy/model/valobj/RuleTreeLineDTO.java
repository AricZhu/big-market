package com.platform.bigmarket.domain.strategy.model.valobj;

import com.platform.bigmarket.domain.strategy.model.common.NodeLineExpressionType;
import com.platform.bigmarket.domain.strategy.model.common.RuleAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RuleTreeLineDTO {
    private String treeId;
    private String nodeFrom;
    private String nodeTo;

    /** 运算类型: 等于、大于、小于、大于等于、小于等于、枚举 */
    private String lineExpressionType;

    /** 运算值 */
    private String lineExpressionValue;
}
