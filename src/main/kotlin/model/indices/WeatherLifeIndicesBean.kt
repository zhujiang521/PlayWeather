package model.indices

import model.Refer
import utils.lifePrefix

data class WeatherLifeIndicesBean(
    val fxLink: String? = null,
    val code: String? = null,
    val refer: Refer? = null,
    val daily: List<WeatherLifeIndicesItem>? = null,
    val updateTime: String? = null,
) {

    data class WeatherLifeIndicesItem(
        val date: String? = null,
        val level: String? = null,
        val name: String? = null,
        val text: String? = null,
        val type: String? = null,
        val category: String? = null,
        var imgRes: String = "${lifePrefix}ic_life_sport.svg",
    )
}