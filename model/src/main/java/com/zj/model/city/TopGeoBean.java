package com.zj.model.city;

import com.zj.model.Refer;

import java.util.List;

public class TopGeoBean {
    private String code;
    private Refer refer;
    private List<GeoBean.LocationBean> topCityList;

    public String getCode() {
        return code;
    }

    public Refer getRefer() {
        return refer;
    }

    public List<GeoBean.LocationBean> getTopCityList() {
        return topCityList;
    }
}