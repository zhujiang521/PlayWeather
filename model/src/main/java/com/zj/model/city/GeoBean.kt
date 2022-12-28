package com.zj.model.city

import com.zj.model.Refer

data class GeoBean(
    val code: String? = null,
    val refer: Refer? = null,
    val location: List<LocationBean>? = null,
) {

    data class LocationBean(
        val country: String? = null,
        val fxLink: String? = null,
        val utcOffset: String? = null,
        var adm2: String? = null,
        val tz: String? = null,
        val isDst: String? = null,
        val lon: String? = null,
        var adm1: String? = null,
        val type: String? = null,
        var name: String? = null,
        val rank: String? = null,
        val id: String? = null,
        val lat: String? = null,
        var hasLocation: Boolean = false
    )
}