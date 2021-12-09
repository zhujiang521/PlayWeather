package com.zj.weather.common.widget.today

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.zj.weather.R
import com.zj.weather.common.widget.WeatherWidgetUtils.notifyWeatherWidget
import com.zj.weather.common.widget.week.WeekWeather
import com.zj.weather.utils.BitmapFillet.fillet
import com.zj.weather.utils.BitmapFillet.zoomImg
import com.zj.weather.utils.XLog
import com.zj.weather.utils.weather.IconUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

class TodayWeatherWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return TodayWeatherRemoteViewsFactory(this.applicationContext, intent)
    }

}

private const val WEEK_COUNT = 7
const val LOCATION_INFO = "location_info"

class TodayWeatherRemoteViewsFactory(private val context: Context, intent: Intent) :
    RemoteViewsService.RemoteViewsFactory, CoroutineScope by MainScope() {

    companion object {
        private var widgetItems: List<WeekWeather> = arrayListOf()

        /**
         * 这块写的不严谨，不应该将这个暴露给外部的，但是目前没有找到更加合适的方法
         * 如果不这样写的话添加完小部件的话会显示不出数据，刷新也不太对。
         */
        fun setWidgetItemList(widgetItems: List<WeekWeather>) {
            Companion.widgetItems = widgetItems
        }

    }


    private var locationInfo = intent.getStringExtra(LOCATION_INFO) ?: ""
    private var mAppWidgetId = intent.getIntExtra(
        AppWidgetManager.EXTRA_APPWIDGET_ID,
        AppWidgetManager.INVALID_APPWIDGET_ID
    )

    override fun onCreate() {
        if (widgetItems.size != WEEK_COUNT) {
            notifyWeatherWidget(context, locationInfo, mAppWidgetId)
        }
    }

    override fun onDataSetChanged() {
        XLog.e("onDataSetChanged: ")
    }

    override fun onDestroy() {
        XLog.d("onDestroy: ")
    }

    override fun getCount(): Int {
        return widgetItems.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        XLog.e("getViewAt:${widgetItems.size}")
        if (widgetItems.size != WEEK_COUNT) {
            return RemoteViews(context.packageName, R.layout.weather_widget_loading)
        }
        return RemoteViews(context.packageName, R.layout.today_widget_item).apply {
            val weather = widgetItems[position]
            XLog.e("getViewAt: ${weather.text}")
            setTextViewText(R.id.today_tv_one_temp, "${weather.min}-${weather.max}℃")
            setTextViewText(R.id.today_tv_one, weather.text)
            setImageViewBitmap(
                R.id.widget_iv_bg,
                fillet(context = context, bitmap = zoomImg(context, weather.icon), roundDp = 10)
            )
            setTextViewText(R.id.today_tv_one_date, weather.week)
            setImageViewResource(
                R.id.today_iv_one,
                IconUtils.getWeatherIcon(weather.icon)
            )
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