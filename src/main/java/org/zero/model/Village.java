package org.zero.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zero (cnzeropro@qq.com)
 * @date 2021/7/18 14:31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Village {
    /**
     * 统计用区划代码
     */
    private String code;
    /**
     * 城乡分类代码
     */
    private String code2;
    /**
     * 名称
     */
    private String name;
}
