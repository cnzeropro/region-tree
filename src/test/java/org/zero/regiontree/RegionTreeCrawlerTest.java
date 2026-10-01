package org.zero.regiontree;

import com.alibaba.fastjson2.JSON;
import org.junit.Test;
import org.zero.regiontree.model.Province;
import org.zero.regiontree.model.Village;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Zero (cnzeropro@qq.com)
 * @date 2021/7/18 13:59
 */
public class RegionTreeCrawlerTest {
    RegionTreeCrawler crawler = new RegionTreeCrawler();

    @Test
    public void test0() {
        System.out.println(crawler.checkUrl("http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2020/11/01/11/110111002.html"));
    }

    @Test
    public void test1() throws IOException {
        List<Province> provinces = crawler.getProvinces();
        provinces.forEach(province -> {
            System.out.println("省：" + province.getName());
            province.setCities(crawler.getCities(province.getUrl()));
            province.getCities().forEach(city -> {
                System.out.println(" 城：" + city.getName());
                city.setCounties(crawler.getCounties(city.getUrl()));
                city.getCounties().forEach(county -> {
                    System.out.println("  县：" + county.getName());
                    county.setTowns(crawler.getTowns(county.getUrl()));
                    county.getTowns().forEach(town -> {
                        System.out.println("   镇：" + town.getName());
                        town.setVillages(crawler.getVillages(town.getUrl()));
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                });
            });
        });

        String jsonStr = JSON.toJSONString(provinces);
        try (PrintWriter writer = new PrintWriter(Files.newOutputStream(Paths.get("info.json")));) {
            writer.println(jsonStr);
            writer.flush();
        }

        System.out.println("------------------------------------------------------------------------------------------\n");

        provinces.forEach(province -> {
            System.out.println("省：" + province.getName());
            System.out.println("网址：" + province.getUrl());
            province.getCities().forEach(city -> {
                System.out.println("\t统计用区划代码：" + city.getCode());
                System.out.println("\t城：" + city.getName());
                System.out.println("\t网址：" + city.getUrl());
                city.getCounties().forEach(county -> {
                    System.out.println("\t\t统计用区划代码：" + county.getCode());
                    System.out.println("\t\t县：" + county.getName());
                    System.out.println("\t\t网址：" + county.getUrl());
                    county.getTowns().forEach(town -> {
                        System.out.println("\t\t\t统计用区划代码：" + town.getCode());
                        System.out.println("\t\t\t镇：" + town.getName());
                        System.out.println("\t\t\t网址：" + town.getUrl());
                        town.getVillages().forEach(village -> {
                            System.out.println("\t\t\t\t统计用区划代码：" + village.getCode());
                            System.out.println("\t\t\t\t城乡分类代码：" + village.getCode2());
                            System.out.println("\t\t\t\t乡：" + village.getName());
                            System.out.println("******************************************************************************************************************");
                        });
                    });
                });
            });
        });
    }

    @Test
    public void test2() throws IOException {
        List<Village> villages = crawler.getVillages("http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2020/11/01/11/110111002.html");
        villages.forEach(System.out::println);
        String jsonStr = JSON.toJSONString(villages);
        try (PrintWriter writer = new PrintWriter(Files.newOutputStream(Paths.get("info.json")));) {
            writer.println(jsonStr);
            writer.flush();
        }
    }
}