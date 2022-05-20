package com.zj.weather.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import com.zj.network.PlayWeatherNetwork
import com.zj.utils.XLog
import com.zj.utils.weather.getDateWeekName
import com.zj.weather.R
import com.zj.weather.widget.today.TodayWeatherRemoteViewsFactory
import com.zj.weather.widget.week.WeatherRemoteViewsFactory
import com.zj.weather.widget.week.WeekWeather
import com.zj.model.room.entity.CityInfo
import com.zj.weather.view.weather.getLocation
import kotlinx.coroutines.*

object WeatherWidgetUtils : CoroutineScope by MainScope() {

    /**
     * 获取之后一周的天气
     *
     * @param context /
     * @param location 需要获取天气的城市
     * @param onSuccessListener 获取成功的回调
     */
    fun getWeather7Day(
        context: Context,
        location: String?,
        onSuccessListener: (MutableList<WeekWeather>) -> Unit
    ) {
        val network = PlayWeatherNetwork(context)
        launch(Dispatchers.IO) {
            val weatherDailyBean = network.getWeather7Day(location ?: "")
            val items = arrayListOf<WeekWeather>()
            weatherDailyBean.daily.forEach { weather ->
                val fxDate = getDateWeekName(context, weather.fxDate)
                val weekWeather =
                    WeekWeather(
                        weather.textDay,
                        weather.fxDate,
                        weather.iconDay,
                        weather.tempMax,
                        weather.tempMin,
                        fxDate
                    )
                items.add(weekWeather)
            }
            onSuccessListener(items)
        }
    }

    /**
     * 刷新天气List
     */
    fun notifyWeatherWidget(
        context: Context,
        cityInfo: CityInfo?,
        appWidgetId: Int
    ) {
        XLog.e("notifyWeatherWidget: 刷新天气")
        getWeather7Day(context = context, location = getLocation(cityInfo = cityInfo)) { items ->
            WeatherRemoteViewsFactory.setWidgetItemList(items)
            val mgr = AppWidgetManager.getInstance(context)
            mgr.notifyAppWidgetViewDataChanged(
                appWidgetId,
                R.id.stack_view
            )
            XLog.e("notifyWeatherWidget: ${items.size}")
        }
    }

    /**
     * 刷新天气List
     */
    fun notifyWeatherWidget(
        context: Context,
        location: String?,
        appWidgetId: Int
    ) {
        XLog.e("notifyWeatherWidget: 刷新天气:$location")
        getWeather7Day(context = context, location = location) { items ->
            TodayWeatherRemoteViewsFactory.setWidgetItemList(items)
            val mgr = AppWidgetManager.getInstance(context)
            mgr.notifyAppWidgetViewDataChanged(
                appWidgetId,
                R.id.today_list_view
            )
            XLog.e("notifyWeatherWidget: ${items.size}")
        }
    }

    /**
     * 返回给定大小的小部件所需的单元格数。
     *
     * @param size 以 dp 为单位的小部件大小。
     * @return 单元格数量的大小。
     */
    fun getCellsForSize(size: Int): Int {
        var n = 2
        while (70 * n - 30 < size) {
            ++n
        }
        return n - 1
    }

}