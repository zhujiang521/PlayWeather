package com.zj.weather.utils

import android.text.format.DateUtils
import android.util.Log
import java.util.*

private const val TAG = "DateUtils"

/**
 * 获取指定年月日的周几
 *
 * @param date 年月日 2013-12-30
 * @return 周几
 */
fun getDateWeekName(date: String): String {
    Log.e(TAG, "getDateWeekName: $date")
    val dateArray = date.split("-")
    val calendar = Calendar.getInstance()
    val todayWeek = calendar.get(Calendar.DAY_OF_WEEK)
    Log.e(TAG, "getDateWeekName1: ${calendar.timeInMillis}")
    calendar.clear()
    calendar.set(dateArray[0].toInt(), dateArray[1].toInt() - 1, dateArray[2].toInt())
    Log.e(TAG, "getDateWeekName2: ${calendar.timeInMillis}")
    val week = calendar.get(Calendar.DAY_OF_WEEK)
    return if (todayWeek == week) {
        "今天"
    } else {
        DateUtils.getDayOfWeekString(week, DateUtils.LENGTH_SHORT).toUpperCase(
            Locale.ROOT
        )
    }
}
