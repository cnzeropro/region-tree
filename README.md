# region-tree

国家统计局**统计用区划代码和城乡划分代码**的采集与数据模型，输出 省 → 市 → 县 → 镇 → 村 五级区划树。

Maven 坐标：`org.zero:region-tree:1.0.0`

## 数据来源

| 项 | 值 |
| --- | --- |
| 站点 | 国家统计局 <http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/> |
| 默认年份 | 2021（`new RegionTreeCrawler(year)` 可指定其它年份） |
| 编码口径 | 统计用区划代码（市/县 12 位，村 14 位），村级附带城乡分类代码 |

> 采集器只允许访问上述站点（见 `RegionTreeCrawler.URL_PATTERN`）；上游页面结构随时可能调整，年份越旧可用性越低。

## 数据模型

| 层级 | 类 | 字段 |
| --- | --- | --- |
| 省 | `Province` | name、url、cities |
| 市 | `City` | code、name、url、counties |
| 县 | `County` | code、name、url、towns |
| 镇 | `Town` | code、name、url、villages |
| 村 | `Village` | code、code2（城乡分类代码）、name |

层级类位于 `org.zero.regiontree.model`，自顶向下持有子级集合，可直接序列化为 JSON。

## 使用

```java
RegionTreeCrawler crawler = new RegionTreeCrawler();   // 默认 2021 年
List<Province> provinces = crawler.getProvinces();

for (Province province : provinces) {
    province.setCities(crawler.getCities(province.getUrl()));
    // 继续下钻：getCounties(city.getUrl()) → getTowns(county.getUrl()) → getVillages(town.getUrl())
}
```

完整示例见 [`RegionTreeCrawlerTest`](src/test/java/org/zero/regiontree/RegionTreeCrawlerTest.java)，抓取结果会序列化为 `info.json`。

## 构建与测试

```bash
mvn -DskipTests package
```

`RegionTreeCrawlerTest` 中的用例会真实请求外网并逐级抓取（含 1.5s 限速），`mvn test` 会长时间运行，请按需执行。
