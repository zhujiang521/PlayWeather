package com.zj.model.city

import com.zj.model.Refer
import com.zj.model.city.GeoBean.LocationBean

data class TopGeoBean(
    val code: String? = null,
    val refer: Refer? = null,
    val topCityList: List<LocationBean>? = null
)