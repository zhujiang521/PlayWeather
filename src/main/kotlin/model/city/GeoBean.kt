package model.city

import model.Refer
import model.city.GeoBean.LocationBean

class GeoBean {
    val code: String? = null
    val refer: Refer? = null
    val location: List<LocationBean>? = null

    class LocationBean {
        val country: String? = null
        val fxLink: String? = null
        val utcOffset: String? = null
        var adm2: String? = null
        val tz: String? = null
        val isDst: String? = null
        val lon: String? = null
        var adm1: String? = null
        val type: String? = null
        var name: String? = null
        val rank: String? = null
        val id: String? = null
        val lat: String? = null
        var hasLocation = false
        override fun toString(): String {
            return "LocationBean{" +
                    "country='" + country + '\'' +
                    ", fxLink='" + fxLink + '\'' +
                    ", utcOffset='" + utcOffset + '\'' +
                    ", adm2='" + adm2 + '\'' +
                    ", tz='" + tz + '\'' +
                    ", isDst='" + isDst + '\'' +
                    ", lon='" + lon + '\'' +
                    ", adm1='" + adm1 + '\'' +
                    ", type='" + type + '\'' +
                    ", name='" + name + '\'' +
                    ", rank='" + rank + '\'' +
                    ", id='" + id + '\'' +
                    ", lat='" + lat + '\'' +
                    '}'
        }
    }

    override fun toString(): String {
        return "GeoBean{" +
                "code='" + code + '\'' +
                ", refer=" + refer +
                ", location=" + location +
                '}'
    }
}