package org.zero.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Zero (cnzeropro@qq.com)
 * @date 2021/7/18 14:29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class County {
    /**
     * 统计用区划代码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 下属城镇url
     */
    private String url;
    /**
     * 下属城镇
     */
    private List<Town> towns;
}
