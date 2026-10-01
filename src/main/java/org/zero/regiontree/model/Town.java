package org.zero.regiontree.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Zero (cnzeropro@qq.com)
 * @date 2021/7/18 14:30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Town {
    /**
     * 统计用区划代码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 下属乡村url
     */
    private String url;
    /**
     * 下属乡村
     */
    private List<Village> villages;
}
