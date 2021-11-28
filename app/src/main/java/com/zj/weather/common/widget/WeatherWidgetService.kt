package com.zj.weather.common.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.google.gson.Gson
import com.qweather.sdk.bean.base.Code
import com.qweather.sdk.bean.base.Lang
import com.qweather.sdk.bean.base.Unit
import com.qweather.sdk.bean.weather.WeatherDailyBean
import com.qweather.sdk.view.QWeather
import com.zj.weather.R
import com.zj.weather.room.entity.CityInfo
import com.zj.weather.ui.view.weather.getLocation
import com.zj.weather.utils.NetCheckUtil
import com.zj.weather.utils.XLog
import com.zj.weather.utils.getDefaultLocale
import com.zj.weather.utils.showToast
import com.zj.weather.utils.weather.IconUtils
import com.zj.weather.utils.weather.getDateWeekName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.parcelize.Parcelize

class WeatherWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return WeatherRemoteViewsFactory(this.applicationContext, intent)
    }

}

private const val TAG = "WeatherWidgetService"
private const val WEEK_COUNT = 7
const val CITY_INFO = "city_info"

class WeatherRemoteViewsFactory(private val context: Context, intent: Intent) :
    RemoteViewsService.RemoteViewsFactory, CoroutineScope by MainScope() {

    private var widgetItems: MutableList<WeekWeather> = arrayListOf()
    private val appWidgetId: Int = intent.getIntExtra(
        AppWidgetManager.EXTRA_APPWIDGET_ID,
        AppWidgetManager.INVALID_APPWIDGET_ID
    )
    private var cityInfo: CityInfo? = null

    init {
        intent.getStringExtra(CITY_INFO)?.apply {
            cityInfo = Gson().fromJson(this, CityInfo::class.java)
        }
    }

    override fun onCreate() {
        if (!NetCheckUtil.checkNet(context = context)) {
            showToast(context, R.string.bad_network_view_tip)
        }
        widgetItems.clear()
        getWeather7Day(getDefaultLocale(context))
        Log.e(TAG, "init: $widgetItems")
    }

    private fun getWeather7Day(lang: Lang) {
        QWeather.getWeather7D(context, getLocation(cityInfo = cityInfo), lang, Unit.METRIC,
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
                        widgetItems = items
                    } else {
                        //在此查看返回数据失败的原因
                        val code: Code? = weatherDailyBean?.code
                        XLog.w("getWeather7Day1 failed code: $code")
                    }
                }
            })
    }

    override fun onDataSetChanged() {
        Log.d(TAG, "onDataSetChanged: ")
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
    }

    override fun getCount(): Int {
        return widgetItems.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        if (widgetItems.size == WEEK_COUNT) {
            return RemoteViews(context.packageName, R.layout.widget_item).apply {
                Log.e(TAG, "getViewAt: ${widgetItems.size}")
                if (position < widgetItems.size) {
                    val weather = widgetItems[position]
                    Log.e(
                        TAG,
                        "getViewAt: ${weather.text}   ${weather.max}    ${weather.min}"
                    )
                    setTextViewText(R.id.widget_item, "${weather.min}-${weather.max}℃")
                    setTextViewText(
                        R.id.widget_tv_city,
                        "${cityInfo?.city ?: ""} ${cityInfo?.name ?: "北京"}"
                    )
                    setTextViewText(R.id.widget_tv_date, weather.time)
                    Log.e(TAG, "getViewAt: cityInfo:$cityInfo")
                    setImageViewResource(
                        R.id.widget_iv_icon,
                        IconUtils.getWeatherIcon(context, weather.icon)
                    )
                }
                // Next, set a fill-intent, which will be used to fill in the pending intent template
                // that is set on the collection view in StackWidgetProvider.
                val fillInIntent = Intent().apply {
                    Bundle().also { extras ->
                        extras.putInt(EXTRA_ITEM, position)
                        putExtras(extras)
                    }
                }
                // Make it possible to distinguish the individual on-click
                // action of a given item
                setOnClickFillInIntent(R.id.widget_item, fillInIntent)
            }
        } else {
            return RemoteViews(context.packageName, R.layout.weather_widget_loading)
        }
    }

    override fun getLoadingView(): RemoteViews {
        return RemoteViews(context.packageName, R.layout.weather_widget_loading)
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

}

@Parcelize
data class WeekWeather(
    val text: String,
    val time: String,
    val icon: String,
    val max: String,
    val min: String
) : Parcelable