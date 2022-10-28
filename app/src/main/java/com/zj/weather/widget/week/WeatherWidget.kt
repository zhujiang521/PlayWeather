package com.zj.weather.widget.week

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.RemoteViews
import com.google.gson.Gson
import com.zj.weather.MainActivity.Companion.actionNewStart
import com.zj.weather.R
import com.zj.weather.widget.WeatherWidgetUtils.getCellsForSize
import com.zj.weather.widget.WeatherWidgetUtils.notifyWeatherWidget
import com.zj.weather.widget.utils.deleteCityInfoPref
import com.zj.weather.widget.utils.loadCityInfoPref
import com.zj.utils.XLog


const val CLICK_ITEM_ACTION = "com.zj.weather.widget.CLICK_ITEM_ACTION"
const val CLICK_LOADING_ITEM_ACTION = "com.zj.weather.widget.CLICK_LOADING_ITEM_ACTION"
const val EXTRA_ITEM = "com.zj.weather.widget.EXTRA_ITEM"

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [WeatherWidgetConfigureActivity]
 */
class WeatherWidget : AppWidgetProvider() {

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        XLog.w("onReceive intent.action:${intent.action}")
        val appWidgetId: Int = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
        val cityInfo = loadCityInfoPref(context, appWidgetId, PREFS_NAME)
        when (intent.action) {
            CLICK_ITEM_ACTION -> {
                actionNewStart(context, cityInfo)
            }
            CLICK_LOADING_ITEM_ACTION -> {
                XLog.w("onReceive: action:${intent.action}")
                notifyWeatherWidget(context, cityInfo, appWidgetId)
            }
            else -> {
                XLog.w("onReceive")
            }
        }
    }


    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateWeekAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        for (appWidgetId in appWidgetIds) {
            deleteCityInfoPref(context, appWidgetId, PREFS_NAME)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        // See the dimensions and
        val options = appWidgetManager.getAppWidgetOptions(appWidgetId)
        // Get min width and height.
        val minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
        val minHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)
        val rows: Int = getCellsForSize(minHeight)
        val columns: Int = getCellsForSize(minWidth)
        XLog.w("rows:$rows   columns:$columns")
        updateWeekAppWidget(context, appWidgetManager, appWidgetId, rows, columns)
    }

}

const val WEATHER_WIDGET_ROWS = "weather_widget_rows"
const val WEATHER_WIDGET_COLUMNS = "weather_widget_columns"

internal fun updateWeekAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    rows: Int = 2,
    columns: Int = 3
) {
    val cityInfo = loadCityInfoPref(context, appWidgetId, PREFS_NAME)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.weather_widget)
    // 构建适配器
    val intent = Intent(context, WeatherWidgetService::class.java).apply {
        // Add the app widget ID to the intent extras.
        putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        putExtra(CITY_INFO, Gson().toJson(cityInfo))
        putExtra(WEATHER_WIDGET_ROWS, rows)
        putExtra(WEATHER_WIDGET_COLUMNS, columns)
        data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
    }
    // 实例化应用小部件布局的 RemoteViews 对象。
    views.apply {
        // 设置 RemoteViews 对象以使用 RemoteViews 适配器
        setRemoteAdapter(R.id.stack_view, intent)

        // 当集合没有项目时显示空视图
        setEmptyView(R.id.stack_view, R.id.empty_view)
        val itemLoading = PendingIntent.getBroadcast(
            context, 0,
            Intent(context, WeatherWidget::class.java)
                .setAction(CLICK_LOADING_ITEM_ACTION),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        setOnClickPendingIntent(R.id.empty_view, itemLoading)
        setOnClickPendingIntent(R.id.empty_tv, itemLoading)
    }

    // 点击事件模版
    val clickPendingIntent: PendingIntent = Intent(
        context,
        WeatherWidget::class.java
    ).run {
        action = CLICK_ITEM_ACTION
        putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
        PendingIntent.getBroadcast(
            context,
            0,
            this,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
    views.setPendingIntentTemplate(R.id.stack_view, clickPendingIntent)

    // 小部件管理器更新小部件
    appWidgetManager.updateAppWidget(appWidgetId, views)
}