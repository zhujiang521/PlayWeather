package com.zj.weather.common.widget.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import com.zj.weather.utils.XLog

private const val ZUI_CALENDAR = "com.zui.calendar"
private const val ZUI_CALENDAR_ACTIVITY = "com.zui.calendar.LaunchActivity"
private const val ZUI_CALENDAR_ACTIVE_ICON = "android.intent.category.ACTIVE_ICON"
private const val ZUI_CALENDAR_ENTER_FROM = "EnterFromWidget"

/**
 * 跳转日历
 */
fun startCalendar(context: Context, timeMills: Long = System.currentTimeMillis()) {
    XLog.e("调用了嘛？")
    // 日历
    val intentCal = Intent()
    intentCal.action = ZUI_CALENDAR_ACTIVE_ICON
    intentCal.addCategory(Intent.CATEGORY_DEFAULT)
    intentCal.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
    intentCal.component = ComponentName(ZUI_CALENDAR, ZUI_CALENDAR_ACTIVITY)
    intentCal.putExtra(ZUI_CALENDAR_ENTER_FROM, true)
    if (timeMills > 0) {
        intentCal.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, timeMills)
    }
    return context.startActivity(intentCal)
}