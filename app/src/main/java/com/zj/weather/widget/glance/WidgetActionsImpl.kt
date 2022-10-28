package com.zj.weather.widget.glance

import android.content.Context
import com.zj.model.PlayLoading
import com.zj.model.PlayState
import com.zj.model.weather.WeatherNowBean
import com.zj.utils.XLog
import com.zj.weather.widget.WeatherWidgetUtils
import com.zj.weather.widget.utils.loadCityInfoPref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class WidgetActionsImpl : WidgetActions {

    private var job: Job? = null

    override fun loadWeather(context: Context, id: Int) {
        job?.cancel()

        job = CoroutineScope(Dispatchers.Default).launch {
            updateWidget(context, PlayLoading)
            loadWeatherInfo(context, id)
        }
    }

    private suspend fun loadWeatherInfo(context: Context, id: Int) {
        val cityInfo = loadCityInfoPref(context, id, TODAY_GLANCE_PREFS_NAME)
        XLog.w("cityInfo:$cityInfo")
        val weatherNow = WeatherWidgetUtils.getWeatherNow(context, cityInfo)
        updateWidget(context, weatherNow)
    }

    private suspend fun updateWidget(
        context: Context,
        state: PlayState<WeatherNowBean.NowBaseBean>
    ) {
        context.getGlanceId()?.let {
            TodayGlanceWidget(state).update(context, it)
        }
    }

}