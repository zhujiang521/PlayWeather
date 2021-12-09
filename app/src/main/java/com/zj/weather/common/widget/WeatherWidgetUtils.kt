package com.zj.weather.common.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import com.google.gson.Gson
import com.qweather.sdk.bean.base.Code
import com.qweather.sdk.bean.base.Unit
import com.qweather.sdk.bean.weather.WeatherDailyBean
import com.qweather.sdk.bean.weather.WeatherNowBean
import com.qweather.sdk.view.QWeather
import com.zj.weather.R
import com.zj.weather.common.widget.today.TodayWeatherRemoteViewsFactory
import com.zj.weather.common.widget.week.WeatherRemoteViewsFactory
import com.zj.weather.common.widget.week.WeekWeather
import com.zj.weather.room.entity.CityInfo
import com.zj.weather.ui.view.weather.getLocation
import com.zj.weather.utils.XLog
import com.zj.weather.utils.getDefaultLocale
import com.zj.weather.utils.showToast

object WeatherWidgetUtils {

    /**
     * 获取之后一周的天气
     *
     * @param context /
     * @param cityInfo 需要获取天气的城市
     * @param onSuccessListener 获取成功的回调
     */
    fun getWeather7Day(
        context: Context,
        location: String?,
        onSuccessListener: (MutableList<WeekWeather>) -> kotlin.Unit
    ) {
        QWeather.getWeather7D(context, location,
            getDefaultLocale(context), Unit.METRIC,
            object : QWeather.OnResultWeatherDailyListener {
                override fun onError(e: Throwable) {
                    XLog.e("getWeather7Day1 onError: $e")
                    showToast(context, e.message)
                }

                override fun onSuccess(weatherDailyBean: WeatherDailyBean?) {
                    XLog.d(
                        "getWeather7Day1 onSuccess: " + Gson().toJson(weatherDailyBean)
                    )
                    //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                    if (Code.OK === weatherDailyBean?.code) {
                        val items = arrayListOf<WeekWeather>()
                        weatherDailyBean.daily.forEach { weather ->
                            val weekWeather =
                                WeekWeather(
                                    weather.textDay,
                                    weather.fxDate,
                                    weather.iconDay,
                                    weather.tempMax,
                                    weather.tempMin
                                )
                            items.add(weekWeather)
                        }
                        onSuccessListener(items)
                    } else {
                        //在此查看返回数据失败的原因
                        val code: Code? = weatherDailyBean?.code
                        XLog.w("getWeather7Day1 failed code: $code")
                    }
                }
            })
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