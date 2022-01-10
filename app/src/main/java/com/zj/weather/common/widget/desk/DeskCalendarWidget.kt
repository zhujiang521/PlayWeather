package com.zj.weather.common.widget.desk

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.zj.weather.MainActivity
import com.zj.weather.R
import com.zj.weather.common.widget.WeatherWidgetUtils
import com.zj.weather.common.widget.week.CLICK_ITEM_ACTION
import com.zj.weather.common.widget.week.CLICK_LOADING_ITEM_ACTION
import com.zj.weather.utils.XLog

/**
 * Implementation of App Widget functionality.
 */
class DeskCalendarWidget : AppWidgetProvider() {

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        XLog.e("onReceive: action:${intent?.action}")
        when (intent?.action) {
            Intent.ACTION_DATE_CHANGED, Intent.ACTION_TIMEZONE_CHANGED -> {
                XLog.e("onReceive: action:${intent.action}")
                val appWidgetId: Int = intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID
                )
                DeskCalendarRemoteViewsFactory.notifyDeskWidget(context, appWidgetId)
            }
            else -> {
                XLog.e("onReceive")
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
            updateAppWidget(context, appWidgetManager, appWidgetId)
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
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.desk_calendar_widget)

    // 构建适配器
    val intent = Intent(context, DeskCalendarWidgetService::class.java).apply {
        // Add the app widget ID to the intent extras.
        putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
    }

    // 实例化应用小部件布局的 RemoteViews 对象。
    views.apply {
        // 设置 RemoteViews 对象以使用 RemoteViews 适配器
        setRemoteAdapter(R.id.stack_view, intent)

        // 当集合没有项目时显示空视图
        setEmptyView(R.id.stack_view, R.id.empty_view)
    }

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}