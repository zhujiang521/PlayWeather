package com.zj.weather.widget.glance

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.zj.model.PlayLoading
import com.zj.utils.XLog

private var widgetActions: WidgetActions? = null

class TodayGlanceReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = TodayGlanceWidget(PlayLoading)

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        widgetActions = WidgetActionsImpl()
    }


    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        appWidgetIds.forEach {
            updateTodayWeather(context, it)
        }
    }

}

fun updateTodayWeather(context: Context, appWidgetIds: Int) {
    XLog.e("updateTodayWeather:$appWidgetIds   widgetActions:$widgetActions")
    if (widgetActions == null) {
        widgetActions = WidgetActionsImpl()
    }
    widgetActions?.loadWeather(context, appWidgetIds)
}