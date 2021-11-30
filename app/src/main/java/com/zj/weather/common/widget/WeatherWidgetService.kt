package com.zj.weather.common.widget

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.google.gson.Gson
import com.zj.weather.R
import com.zj.weather.room.entity.CityInfo
import com.zj.weather.utils.NetCheckUtil
import com.zj.weather.utils.XLog
import com.zj.weather.utils.showToast
import com.zj.weather.utils.weather.IconUtils
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
        WeatherWidgetUtils.getWeather7Day(context = context, cityInfo = cityInfo) { items ->
            widgetItems.clear()
            widgetItems = items
        }
        XLog.e(TAG, "init: $widgetItems")
    }

    override fun onDataSetChanged() {
        XLog.d(TAG, "onDataSetChanged: ")
    }

    override fun onDestroy() {
        XLog.d(TAG, "onDestroy: ")
    }

    override fun getCount(): Int {
        return widgetItems.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        if (widgetItems.size != WEEK_COUNT) {
            return RemoteViews(context.packageName, R.layout.weather_widget_loading)
        }
        return RemoteViews(context.packageName, R.layout.widget_item).apply {
            XLog.e(TAG, "getViewAt: ${widgetItems.size}")
            if (position < widgetItems.size) {
                val weather = widgetItems[position]
                XLog.e(
                    TAG,
                    "getViewAt: ${weather.text}   ${weather.max}    ${weather.min}"
                )
                setTextViewText(R.id.widget_tv_temp, "${weather.min}-${weather.max}℃")
                setTextViewText(
                    R.id.widget_tv_city,
                    "${cityInfo?.city ?: ""} ${cityInfo?.name ?: "北京"}"
                )
                setTextViewText(R.id.widget_tv_date, weather.time)
                XLog.e(TAG, "getViewAt: cityInfo:$cityInfo")
                setImageViewResource(
                    R.id.widget_iv_icon,
                    IconUtils.getWeatherIcon(weather.icon)
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
            setOnClickFillInIntent(R.id.widget_ll_item, fillInIntent)
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