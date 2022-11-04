package org.zero.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Zero (cnzeropro@qq.com)
 * @date 2021/7/18 14:24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Province {
    /**
     * 名称
     */
    private String name;
    /**
     * 下属城市url
     */
    private String url;
    /**
     * 下属城市
     */
    private List<City> cities;
}
