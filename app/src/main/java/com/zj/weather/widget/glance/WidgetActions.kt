package com.zj.weather.widget.glance

import android.content.Context

interface WidgetActions {
    fun loadWeather(context: Context, id: Int)
}
