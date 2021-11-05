package com.zj.weather.common

import com.zj.weather.model.WeatherModel

sealed class PlayState
object PlayLoading : PlayState()
data class PlaySuccess(val data: WeatherModel) : PlayState()
data class PlayError(val e: Throwable) : PlayState()