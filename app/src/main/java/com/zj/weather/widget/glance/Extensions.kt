package com.zj.weather.widget.glance

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidgetManager

suspend fun Context.getGlanceId(): GlanceId? {
    return GlanceAppWidgetManager(this).getGlanceIds(TodayGlanceWidget::class.java).firstOrNull()
}