package utils


import java.text.SimpleDateFormat
import java.util.*

private val format = SimpleDateFormat("E", Locale.getDefault())

private val yyyyMMdd = SimpleDateFormat("yyyy-MM-dd'T'HH:mm+08:00", Locale.getDefault())
private val hhMM = SimpleDateFormat("HH:mm", Locale.getDefault())

private val calendar = Calendar.getInstance()

/**
 * 获取指定年月日的周几
 *
 * this 年月日 2013-12-30
 * @return 周几
 */
fun String.getDateWeekName(): String {
    val dateArray = split("-")
    val todayWeek = calendar.get(Calendar.DAY_OF_WEEK)
    calendar.clear()
    calendar.set(dateArray[0].toInt(), dateArray[1].toInt() - 1, dateArray[2].toInt())
    val week = calendar.get(Calendar.DAY_OF_WEEK)
    return if (todayWeek == week) {
        "今天"
    } else {
        val weekString = format.format(Date(calendar.timeInMillis))
        weekString
    }
}

/**
 * 获取指定时间为几点
 *
 * time 年月日 2013-12-30T13:00+08:00
 * @return 09:25
 */
fun String?.getTimeNameForObs(): String {
    val calendar = Calendar.getInstance()
    // HH为24小时 hh为12小时
    calendar.time = yyyyMMdd.parse(this) ?: Date()
    return "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"
}

/**
 * 获取指定时间为几点
 *
 * time 年月日 2013-12-30T13:00+08:00
 * @return 13时
 */
fun String.getTimeName(): String {
    val calendar = Calendar.getInstance()
    val todayHour = calendar.get(Calendar.HOUR_OF_DAY)
    // HH为24小时 hh为12小时
    calendar.time = yyyyMMdd.parse(this) ?: Date()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    return if (todayHour + 1 == hour) {
        "现在"
    } else {
        "${hour}时"
    }
}