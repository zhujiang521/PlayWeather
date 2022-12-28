package com.zj.utils.weather

import android.annotation.SuppressLint
import android.content.Context
import com.zj.utils.R
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("ConstantLocale")
private val format = SimpleDateFormat("E", Locale.getDefault())

@SuppressLint("ConstantLocale")
private val yyyyMMdd = SimpleDateFormat("yyyy-MM-dd'T'HH:mm+08:00", Locale.getDefault())

private val calendar = Calendar.getInstance()

/**
 * 获取指定年月日的周几
 *
 * @param date 年月日 2013-12-30
 * @return 周几
 */
fun getDateWeekName(context: Context, date: String?): String {
    if (date == null) return context.getString(R.string.time_today)
    val dateArray = date.split("-")
    val todayWeek = calendar.get(Calendar.DAY_OF_WEEK)
    calendar.clear()
    calendar.set(dateArray[0].toInt(), dateArray[1].toInt() - 1, dateArray[2].toInt())
    val week = calendar.get(Calendar.DAY_OF_WEEK)
    return if (todayWeek == week) {
        context.getString(R.string.time_today)
    } else {
        val weekString = format.format(Date(calendar.timeInMillis))
        weekString
    }
}

/**
 * 获取指定时间为几点
 *
 * @param time 年月日 2013-12-30T13:00+08:00
 * @return 13时
 */
fun getTimeName(context: Context, time: String?): String {
    if (time == null) return context.getString(R.string.time_now)
    val calendar = Calendar.getInstance()
    val todayHour = calendar.get(Calendar.HOUR_OF_DAY)
    // HH为24小时 hh为12小时
    calendar.time = yyyyMMdd.parse(time) ?: Date()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    return if (todayHour + 1 == hour) {
        context.getString(R.string.time_now)
    } else {
        "$hour${context.getString(R.string.time_hour)}"
    }
}
