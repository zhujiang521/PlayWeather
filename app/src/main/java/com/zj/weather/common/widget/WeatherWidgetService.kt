package com.zj.weather.common.widget

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.util.TypedValue.COMPLEX_UNIT_SP
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.google.gson.Gson
import com.zj.weather.R
import com.zj.weather.room.entity.CityInfo
import com.zj.weather.utils.BitmapFillet.fillet
import com.zj.weather.utils.BitmapFillet.zoomImg
import com.zj.weather.utils.XLog
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

    companion object {
        private var widgetItems: List<WeekWeather> = arrayListOf()

        /**
         * 这块写的不严谨，不应该将这个暴露给外部的，但是目前没有找到更加合适的方法
         * 如果不这样写的话添加完小部件的话会显示不出数据，刷新也不太对。
         */
        fun setWidgetItemList(widgetItems: List<WeekWeather>) {
            this.widgetItems = widgetItems
        }
    }

    private var cityInfo: CityInfo? = null
    private var widgetRows: Int = 2
    private var widgetColumns: Int = 3

    init {
        intent.getStringExtra(CITY_INFO)?.apply {
            cityInfo = Gson().fromJson(this, CityInfo::class.java)
        }
        widgetRows = intent.getIntExtra(WEATHER_WIDGET_ROWS, 2)
        widgetColumns = intent.getIntExtra(WEATHER_WIDGET_COLUMNS, 4)
    }

    override fun onCreate() {
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
            val weather = widgetItems[position]
            XLog.e(TAG, "getViewAt: ${weather.text}")
            XLog.e(TAG, "getViewAt: cityInfo:$cityInfo")
            setTextViewText(R.id.widget_tv_temp, "${weather.min}-${weather.max}℃")
            setTextViewText(
                R.id.widget_tv_city,
                "${cityInfo?.city ?: ""} ${cityInfo?.name ?: "北京"}"
            )
            setImageViewBitmap(
                R.id.widget_iv_bg,
                fillet(context = context, bitmap = zoomImg(context, weather.icon), roundDp = 10)
            )
            layoutAdapter(weather.icon)
            setTextViewText(R.id.widget_tv_date, weather.time)
            setImageViewResource(
                R.id.widget_iv_icon,
                IconUtils.getWeatherIcon(weather.icon)
            )
            // Next, set a fill-intent, which will be used to fill in the pending intent template
            // that is set on the collection view in StackWidgetProvider.
            val fillInIntent = Intent().apply {
                putExtra(EXTRA_ITEM, weather.time)
            }
            // Make it possible to distinguish the individual on-click
            // action of a given item
            XLog.e(TAG, "getViewAt: fillInIntent:${position}")
            setOnClickFillInIntent(R.id.widget_ll_item, fillInIntent)
        }
    }

    /**
     * 小部件不同布局下的适配
     *
     * @param weatherIcon 天气Icon
     */
    private fun RemoteViews.layoutAdapter(weatherIcon: String) {
        if (widgetColumns < 4 || widgetRows < 2) {
            // 小的情况下
            setTextViewTextSize(
                R.id.widget_tv_city,
                COMPLEX_UNIT_SP,
                15f
            )
            setTextViewTextSize(
                R.id.widget_tv_temp,
                COMPLEX_UNIT_SP,
                15f
            )
            setTextViewTextSize(
                R.id.widget_tv_date,
                COMPLEX_UNIT_SP,
                15f
            )
            setImageViewResource(
                R.id.widget_iv_small_icon,
                IconUtils.getWeatherIcon(weatherIcon)
            )
            setViewVisibility(R.id.widget_iv_small_icon, View.VISIBLE)
            setViewVisibility(R.id.widget_iv_icon, View.GONE)
        } else {
            // 大的情况下
            setTextViewTextSize(
                R.id.widget_tv_city,
                COMPLEX_UNIT_SP,
                25f
            )
            setTextViewTextSize(
                R.id.widget_tv_temp,
                COMPLEX_UNIT_SP,
                20f
            )
            setTextViewTextSize(
                R.id.widget_tv_date,
                COMPLEX_UNIT_SP,
                20f
            )
            setImageViewResource(
                R.id.widget_iv_icon,
                IconUtils.getWeatherIcon(weatherIcon)
            )
            setViewVisibility(R.id.widget_iv_small_icon, View.GONE)
            setViewVisibility(R.id.widget_iv_icon, View.VISIBLE)
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