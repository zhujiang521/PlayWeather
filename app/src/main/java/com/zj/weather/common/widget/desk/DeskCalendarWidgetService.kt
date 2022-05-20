package com.zj.weather.common.widget.desk

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.zj.weather.R
import com.zj.weather.common.widget.utils.ProgrammerCalendar
import com.zj.utils.Lunar
import com.zj.utils.XLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import java.util.*

class DeskCalendarWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return DeskCalendarRemoteViewsFactory(this.applicationContext)
    }

}

class DeskCalendarRemoteViewsFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory, CoroutineScope by MainScope() {

    private var widgetItems = arrayListOf<DeskCalendar>()

    /**
     * 刷新List
     */
    private fun notifyDeskWidget() {
        XLog.e("notifyWeatherWidget: 刷新cal")
        // 刷新数据
        widgetItems.clear()
        val calendar = Calendar.getInstance()
        for (index in 0..6) {
            calendar.add(Calendar.DAY_OF_MONTH, if (index == 0) 0 else 1)
            val hl = ProgrammerCalendar(calendar.timeInMillis)
            widgetItems.add(
                DeskCalendar(
                    day = calendar.get(Calendar.DAY_OF_MONTH),
                    lunar = Lunar(cal = calendar).toString(),
                    programmer = hl,
                    mills = calendar.timeInMillis
                )
            )
        }
    }

    override fun onCreate() {
        notifyDeskWidget()
    }

    override fun onDataSetChanged() {
        notifyDeskWidget()
        XLog.e("刷新了，刷新了，$widgetItems")
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int {
        return widgetItems.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        return RemoteViews(context.packageName, R.layout.widget_desk_item).apply {
            val deskCalendar = widgetItems[position]
            val hl = deskCalendar.programmer
            val pickTodayLuck = hl.pickTodayLuck()
            setTextViewText(R.id.deskTvLunar, deskCalendar.lunar)
            setTextViewText(R.id.deskTvDay, deskCalendar.day.toString())
            setTextViewText(R.id.deskTvYiDetail, pickTodayLuck[0])
            setTextViewText(R.id.deskTvJiDetail, pickTodayLuck[1])
            setTextViewText(
                R.id.deskTvSeatDetail,
                hl.directions[hl.random(hl.iday, 2) % hl.directions.size]
            )
            setTextViewText(R.id.deskTvDrinkDetail, hl.pickRandomDrinks(2).toString())
            setTextViewText(R.id.deskTvCodeDetail, hl.star(hl.random(hl.iday, 6) % 5 + 1))
            // 可以区分给定项目的单个点击操作
            val fillInIntent = Intent().apply {
                putExtra(CLICK_DESK_ACTION_VALUES, deskCalendar.mills)
            }
            setOnClickFillInIntent(R.id.desk_ll_item, fillInIntent)
        }
    }

    override fun getLoadingView(): RemoteViews {
        return RemoteViews(context.packageName, R.layout.widget_loading)
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

/**
 * 桌面台历数据类
 *
 * @param day 天
 * @param lunar 农历
 * @param programmer 黄历
 * @param mills 时间戳
 */
data class DeskCalendar(
    val day: Int,
    val lunar: String,
    val programmer: ProgrammerCalendar,
    val mills: Long
)