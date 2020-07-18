package com.zwl.mybossdemo.filter;

/**
 * 筛选
 *
 * @author zwl
 * @date on 2020/7/17
 */
public class FilterBean {
    public final transient static String UNLIMITED = "-1";//不限，这里与后台定义一致

    public String id;
    public String name;

    public FilterBean() {
    }

    public FilterBean(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "FilterBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
