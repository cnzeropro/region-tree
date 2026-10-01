package org.zero.regiontree;

import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.zero.regiontree.model.City;
import org.zero.regiontree.model.County;
import org.zero.regiontree.model.Province;
import org.zero.regiontree.model.Town;
import org.zero.regiontree.model.Village;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 国家统计局「统计用区划代码和城乡划分代码」采集器，按 省 → 市 → 县 → 镇 → 村 逐级解析。
 *
 * @author Zero
 */
public class RegionTreeCrawler {
    public static final String BASE_URL = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/";
    private static final Pattern URL_PATTERN = Pattern.compile("http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/\\d+/?[\\w&@#/%+=~\\-_|.]*");

    private String url;

    public RegionTreeCrawler() {
        this(2021);
    }

    public RegionTreeCrawler(int year) {
        this(BASE_URL + year);
    }

    public RegionTreeCrawler(String url) {
        this.url = url;
    }

    public List<Province> getProvinces() {
        return getProvinces(url);
    }

    public List<Province> getProvinces(String fromUrl) {
        // url为空，返回空列表
        if (StringUtil.isBlank(fromUrl)) {
            return Collections.emptyList();
        }
        // 尝试获取省份信息
        try {
            List<Province> provinces = new ArrayList<>();
            Document document = getDocument(fromUrl);
            Elements provinceTrs = document.select("tr .provincetr");
            Elements provinceLinks = provinceTrs.select("a[href]");

            for (Element provinceLink : provinceLinks) {
                String toUrl = fromUrl + "/" + provinceLink.attr("href");
                provinces.add(Province.builder()
                        .name(provinceLink.text())
                        .url(toUrl)
//                        .cities(getCities(url))
                        .build());
            }
            return provinces;
        } catch (Exception e) {
            throw new RuntimeException("获取省份信息失败", e);
        }
    }

    public List<City> getCities(String fromUrl) {
        // url为空，返回空列表
        if (StringUtil.isBlank(fromUrl)) {
            return Collections.emptyList();
        }
        // 尝试获取城市信息
        try {
            List<City> cities = new ArrayList<>();
            Document document = getDocument(fromUrl);
            Elements cityTrs = document.select("tr .citytr");
            for (Element cityTr : cityTrs) {
                Elements cityLinks = cityTr.select("a[href]");

                String code = cityLinks.first().text();
                String name = cityLinks.last().text();
                String toUrl = fromUrl.substring(0, fromUrl.lastIndexOf('/') + 1) + cityLinks.attr("href");

                cities.add(City.builder()
                        .code(code)
                        .name(name)
                        .url(toUrl)
//                        .counties(getCounties(url))
                        .build());
            }
            return cities;
        } catch (Exception e) {
            throw new RuntimeException("获取城市信息失败", e);
        }
    }

    public List<County> getCounties(String fromUrl) {
        // url为空，返回空列表
        if (StringUtil.isBlank(fromUrl)) {
            return Collections.emptyList();
        }
        // 尝试获取区县信息
        try {
            List<County> counties = new ArrayList<>();
            Document document = getDocument(fromUrl);
            Elements countyTrs = document.select("tr .countytr");
            countyTrs.forEach(countyTr -> {
                String[] texts = countyTr.text().split(" ");
                String attribute = countyTr.select("a[href]").attr("href");
                String toUrl = null;
                if (!StringUtil.isBlank(attribute)) {
                    toUrl = fromUrl.substring(0, fromUrl.lastIndexOf('/') + 1) + attribute;
                }

                counties.add(County.builder()
                        .code(texts[0])
                        .name(texts[1])
                        .url(toUrl)
//                        .towns(getTowns(url))
                        .build());
            });
            return counties;
        } catch (Exception e) {
            throw new RuntimeException("获取区县信息失败", e);
        }
    }

    public List<Town> getTowns(String fromUrl) {
        // url为空，返回空列表
        if (StringUtil.isBlank(fromUrl)) {
            return Collections.emptyList();
        }
        // 尝试获取城镇信息
        try {
            List<Town> towns = new ArrayList<>();
            Document document = getDocument(fromUrl);
            Elements townTrs = document.select("tr .towntr");
            townTrs.forEach(townTr -> {
                Elements townLinks = townTr.select("a[href]");
                String code = townLinks.first().text();
                String name = townLinks.last().text();
                String toUrl = fromUrl.substring(0, fromUrl.lastIndexOf('/') + 1) + townLinks.attr("href");
                towns.add(Town.builder()
                        .code(code)
                        .name(name)
                        .url(toUrl)
//                        .villages(getVillages(url))
                        .build());
            });
            return towns;
        } catch (Exception e) {
            throw new RuntimeException("获取城镇信息失败", e);
        }
    }

    public List<Village> getVillages(String fromUrl) {
        // url为空，返回空列表
        if (StringUtil.isBlank(fromUrl)) {
            return Collections.emptyList();
        }
        // 尝试获取乡村信息
        try {
            List<Village> villages = new ArrayList<>();
            Document document = getDocument(fromUrl);
            Elements villageTrs = document.select("tr .villagetr");
            villageTrs.forEach(villageTr -> {
                String[] texts = villageTr.text().split(" ");
                villages.add(Village.builder()
                        .code(texts[0])
                        .code2(texts[1])
                        .name(texts[2])
                        .build());
            });
            return villages;
        } catch (Exception e) {
            throw new RuntimeException("获取乡村信息失败", e);
        }
    }

    private Document getDocument(String url) throws Exception {
        if (!checkUrl(url)) {
            throw new RuntimeException("[" + url + "]错误网址");
        }
        return Jsoup.connect(url)
                .userAgent(HttpConnection.DEFAULT_UA)
                .timeout(0)
                .get();
    }

    public boolean checkUrl(String url) {
        return URL_PATTERN.matcher(url).matches();
    }
}
