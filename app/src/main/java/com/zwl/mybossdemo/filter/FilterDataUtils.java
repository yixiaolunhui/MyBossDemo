package com.zwl.mybossdemo.filter;

import com.zwl.mybossdemo.filter.flow.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zwl
 * @describe  测试数据
 * @date on 2020/7/18
 */
public class FilterDataUtils {
    /**
     * 这里是测试模拟数据（实际是后台返回）
     *
     * @return
     */
    public static List<FilterGrop> getFilterData() {
        List<FilterGrop> filterGrops = new ArrayList<>();

        //学历要求
        FilterGrop filterGrop = new FilterGrop();
        filterGrop.gropName = "学历要求";
        filterGrop.key="xlyq";
        filterGrop.filters = new ArrayList<>();
        filterGrop.filters.add(new FilterBean(FilterBean.UNLIMITED, "不限"));
        filterGrop.filters.add(new FilterBean("1_1", "初中及以下"));
        filterGrop.filters.add(new FilterBean("1_2", "中专/中技"));
        filterGrop.filters.add(new FilterBean("1_3", "高中"));
        filterGrop.filters.add(new FilterBean("1_4", "大专"));
        filterGrop.filters.add(new FilterBean("1_5", "本科"));
        filterGrop.filters.add(new FilterBean("1_6", "硕士"));
        filterGrop.filters.add(new FilterBean("1_7", "博士"));
        filterGrops.add(filterGrop);


        //薪资待遇
        FilterGrop filterGrop1 = new FilterGrop();
        filterGrop1.gropName = "薪资待遇";
        filterGrop1.key="xzdy";
        filterGrop1.filterType = TagFlowLayout.TAG_MODE_SINGLE;
        filterGrop1.filters = new ArrayList<>();
        filterGrop1.filters.add(new FilterBean(FilterBean.UNLIMITED, "不限"));
        filterGrop1.filters.add(new FilterBean("2_1", "3K以下"));
        filterGrop1.filters.add(new FilterBean("2_2", "3-5K"));
        filterGrop1.filters.add(new FilterBean("2_3", "5-10K"));
        filterGrop1.filters.add(new FilterBean("2_4", "10-20K"));
        filterGrop1.filters.add(new FilterBean("2_5", "20-50K"));
        filterGrop1.filters.add(new FilterBean("2_6", "50K以上"));
        filterGrops.add(filterGrop1);


        //经验要求
        FilterGrop filterGrop2 = new FilterGrop();
        filterGrop2.gropName = "经验要求";
        filterGrop2.key="jyyq";
        filterGrop2.filters = new ArrayList<>();
        filterGrop2.filters.add(new FilterBean(FilterBean.UNLIMITED, "不限"));
        filterGrop2.filters.add(new FilterBean("3_1", "在校生"));
        filterGrop2.filters.add(new FilterBean("3_2", "应届生"));
        filterGrop2.filters.add(new FilterBean("3_3", "1年以内"));
        filterGrop2.filters.add(new FilterBean("3_4", "1-3年"));
        filterGrop2.filters.add(new FilterBean("3_5", "3-5年"));
        filterGrop2.filters.add(new FilterBean("3_6", "5-10年"));
        filterGrop2.filters.add(new FilterBean("3_7", "10年以上"));
        filterGrops.add(filterGrop2);


        //行业分类
        FilterGrop filterGrop3 = new FilterGrop();
        filterGrop3.gropName = "行业分类";
        filterGrop3.key="hyfl";
        filterGrop3.filters = new ArrayList<>();
        filterGrop3.filters.add(new FilterBean(FilterBean.UNLIMITED, "不限"));
        filterGrop3.filters.add(new FilterBean("4_1", "电子商务"));
        filterGrop3.filters.add(new FilterBean("4_2", "游戏"));
        filterGrop3.filters.add(new FilterBean("4_3", "媒体"));
        filterGrop3.filters.add(new FilterBean("4_4", "广告营销"));
        filterGrop3.filters.add(new FilterBean("4_5", "数据服务"));
        filterGrop3.filters.add(new FilterBean("4_6", "医疗健康"));
        filterGrop3.filters.add(new FilterBean("4_7", "生活服务"));
        filterGrop3.filters.add(new FilterBean("4_8", "O2O"));
        filterGrop3.filters.add(new FilterBean("4_9", "旅游"));
        filterGrop3.filters.add(new FilterBean("4_10", "分类信息"));
        filterGrop3.filters.add(new FilterBean("4_11", "音乐/视频/阅读"));
        filterGrop3.filters.add(new FilterBean("4_12", "在线教育"));
        filterGrop3.filters.add(new FilterBean("4_13", "社交网络"));
        filterGrop3.filters.add(new FilterBean("4_14", "人力资源服务"));
        filterGrop3.filters.add(new FilterBean("4_15", "企业服务"));
        filterGrop3.filters.add(new FilterBean("4_16", "信息安全"));
        filterGrop3.filters.add(new FilterBean("4_17", "智能硬件"));
        filterGrop3.filters.add(new FilterBean("4_18", "移动互联网"));
        filterGrop3.filters.add(new FilterBean("4_19", "互联网"));
        filterGrop3.filters.add(new FilterBean("4_20", "计算机软件"));
        filterGrop3.filters.add(new FilterBean("4_21", "通信/网络设备"));
        filterGrop3.filters.add(new FilterBean("4_22", "广告/公关/会展"));
        filterGrop3.filters.add(new FilterBean("4_23", "互联网金融"));
        filterGrop3.filters.add(new FilterBean("4_24", "物流/仓储"));
        filterGrop3.filters.add(new FilterBean("4_25", "贸易/进出口"));
        filterGrop3.filters.add(new FilterBean("4_26", "咨询"));
        filterGrop3.filters.add(new FilterBean("4_27", "工程施工"));
        filterGrop3.filters.add(new FilterBean("4_28", "汽车生产"));
        filterGrop3.filters.add(new FilterBean("4_29", "其他行业"));
        filterGrops.add(filterGrop3);


        //公司规模
        FilterGrop filterGrop4 = new FilterGrop();
        filterGrop4.gropName = "公司规模";
        filterGrop4.key="gsgm";
        filterGrop4.filters = new ArrayList<>();
        filterGrop4.filters.add(new FilterBean(FilterBean.UNLIMITED, "不限"));
        filterGrop4.filters.add(new FilterBean("4_1", "0-20人"));
        filterGrop4.filters.add(new FilterBean("4_2", "20-99人"));
        filterGrop4.filters.add(new FilterBean("4_3", "100-499人"));
        filterGrop4.filters.add(new FilterBean("4_4", "500-999人"));
        filterGrop4.filters.add(new FilterBean("4_5", "1000-9999人"));
        filterGrop4.filters.add(new FilterBean("4_6", "10000人以上"));
        filterGrops.add(filterGrop4);


        //融资阶段
        FilterGrop filterGrop5 = new FilterGrop();
        filterGrop5.gropName = "融资阶段";
        filterGrop5.key="rzjd";
        filterGrop5.filters = new ArrayList<>();
        filterGrop5.filters.add(new FilterBean(FilterBean.UNLIMITED, "不限"));
        filterGrop5.filters.add(new FilterBean("5_1", "未融资"));
        filterGrop5.filters.add(new FilterBean("5_2", "天使轮"));
        filterGrop5.filters.add(new FilterBean("5_3", "A轮"));
        filterGrop5.filters.add(new FilterBean("5_4", "B轮"));
        filterGrop5.filters.add(new FilterBean("5_5", "C轮"));
        filterGrop5.filters.add(new FilterBean("5_6", "D轮及以上"));
        filterGrop5.filters.add(new FilterBean("5_7", "已上市"));
        filterGrop5.filters.add(new FilterBean("5_8", "不需要融资"));
        filterGrops.add(filterGrop5);
        return filterGrops;

    }
}
