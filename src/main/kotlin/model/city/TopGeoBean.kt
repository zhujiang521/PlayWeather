package model.city

import model.Refer
import model.city.GeoBean.LocationBean

data class TopGeoBean(
    val code: String? = null,
    val refer: Refer? = null,
    val topCityList: List<LocationBean>? = null
)