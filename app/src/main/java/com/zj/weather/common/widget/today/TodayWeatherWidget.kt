package com.zj.weather.common.widget.today

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.SizeF
import android.widget.RemoteViews
import com.zj.weather.MainActivity
import com.zj.weather.R
import com.zj.weather.common.widget.WeatherWidgetUtils
import com.zj.weather.common.widget.utils.deleteCityInfoPref
import com.zj.weather.common.widget.utils.loadCityInfoPref
import com.zj.model.room.entity.CityInfo
import com.zj.weather.view.weather.getLocation
import com.zj.utils.XLog
import com.zj.utils.weather.IconUtils

const val CLICK_TODAY_ACTION = "com.zj.weather.common.widget.today.CLICK_TODAY_ACTION"
const val LOCATION_REFRESH = "com.zj.weather.LOCATION_REFRESH"


/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [TodayWeatherWidgetConfigureActivity]
 */
class TodayWeatherWidget : AppWidgetProvider() {

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        XLog.e("onReceive:测试 intent.action:${intent.action}")
        val appWidgetId: Int = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
        val cityInfo = loadCityInfoPref(context, appWidgetId, TODAY_PREFS_NAME)
        XLog.e("cityInfo:$cityInfo")
        if (intent.action == CLICK_TODAY_ACTION) {
            MainActivity.actionNewStart(context, cityInfo)
        } else if (intent.action == LOCATION_REFRESH) {
            // 如果isLocation == 1的话证明当前小部件使用的是当前位置
            // 那么在当前位置发生修改的情况下就需要同时进行修改
            if (cityInfo?.isLocation != 1) return
            refreshLocationWeather(
                context = context,
                cityInfo = cityInfo,
                appWidgetId = appWidgetId
            )
        }
    }


    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        for (appWidgetId in appWidgetIds) {
            deleteCityInfoPref(context, appWidgetId, TODAY_PREFS_NAME)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val cityInfo = loadCityInfoPref(context, appWidgetId, TODAY_PREFS_NAME)
    // 指示小部件管理器更新小部件
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val smallView = RemoteViews(context.packageName, R.layout.today_weather_widget_small)
        val mediumView = RemoteViews(context.packageName, R.layout.today_weather_widget_medium)
        val largeView = RemoteViews(context.packageName, R.layout.today_weather_widget_large)

        val smallLargeView =
            RemoteViews(context.packageName, R.layout.today_weather_widget_small_large)

        val viewMapping: Map<SizeF, RemoteViews> = mapOf(
            SizeF(140f, 40f) to smallView,
            SizeF(170f, 40f) to mediumView,
            SizeF(170f, 70f) to mediumView,
            SizeF(270f, 110f) to largeView,
            SizeF(140f, 110f) to smallLargeView,
            SizeF(170f, 70f) to largeView,
            SizeF(170f, 110f) to largeView
        )
        buildRemoteViews(context, cityInfo, smallView, appWidgetManager, appWidgetId, viewMapping)
        buildRemoteViews(context, cityInfo, mediumView, appWidgetManager, appWidgetId, viewMapping)
        buildRemoteViews(context, cityInfo, largeView, appWidgetManager, appWidgetId, viewMapping)
        buildRemoteViews(
            context,
            cityInfo,
            smallLargeView,
            appWidgetManager,
            appWidgetId,
            viewMapping
        )
    } else {
        val views = RemoteViews(context.packageName, R.layout.today_weather_widget_medium)
        buildRemoteViews(context, cityInfo, views, appWidgetManager, appWidgetId)
    }
}

private fun buildRemoteViews(
    context: Context,
    cityInfo: CityInfo?,
    views: RemoteViews,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    viewMapping: Map<SizeF, RemoteViews>? = null,
) {
    WeatherWidgetUtils.getWeather7Day(
        context = context,
        location = getLocation(cityInfo = cityInfo)
    ) { items ->
        val nowBaseBean = items[0]
        views.setTextViewText(R.id.today_tv_location, "${cityInfo?.city} ${cityInfo?.name}")
        views.setTextViewText(R.id.today_tv_temp, "${nowBaseBean.min}/${nowBaseBean.max}℃")
        views.setImageViewResource(
            R.id.today_iv_icon,
            IconUtils.getWeatherIcon(nowBaseBean.icon)
        )
        views.setTextViewText(R.id.today_tv_text, nowBaseBean.text)
        val tomorrowBaseBean = items[1]
        views.setImageViewResource(
            R.id.today_iv_tomorrow,
            IconUtils.getWeatherIcon(tomorrowBaseBean.icon)
        )
        views.setTextViewText(R.id.today_tv_tomorrow_text, tomorrowBaseBean.text)
        val afterBaseBean = items[2]
        views.setImageViewResource(
            R.id.today_iv_after,
            IconUtils.getWeatherIcon(afterBaseBean.icon)
        )
        views.setTextViewText(R.id.today_tv_after_text, afterBaseBean.text)
        // 构建适配器
        val intent = Intent(context, TodayWeatherWidgetService::class.java).apply {
            // Add the app widget ID to the intent extras.
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            putExtra(LOCATION_INFO, getLocation(cityInfo = cityInfo))
            data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
        }
        // 设置 RemoteViews 对象以使用 RemoteViews 适配器
        views.setRemoteAdapter(R.id.today_list_view, intent)
        val itemClick = PendingIntent.getBroadcast(
            context, 0,
            Intent(context, TodayWeatherWidget::class.java)
                .setAction(CLICK_TODAY_ACTION),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        views.setOnClickPendingIntent(R.id.today_ll, itemClick)
        views.setOnClickPendingIntent(R.id.today_rl, itemClick)
        if (viewMapping != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            appWidgetManager.updateAppWidget(appWidgetId, RemoteViews(viewMapping))
        } else {
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}