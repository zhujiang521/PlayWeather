package com.zj.weather.common.widget.desk

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.zj.weather.R
import com.zj.weather.utils.XLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import java.util.*
import kotlin.collections.ArrayList

class DeskCalendarWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return DeskCalendarRemoteViewsFactory(this.applicationContext, intent)
    }

}

class DeskCalendarRemoteViewsFactory(private val context: Context, intent: Intent) :
    RemoteViewsService.RemoteViewsFactory, CoroutineScope by MainScope() {

    private var mAppWidgetId = intent.getIntExtra(
        AppWidgetManager.EXTRA_APPWIDGET_ID,
        AppWidgetManager.INVALID_APPWIDGET_ID
    )


    companion object {
        private var widgetItems = arrayListOf<Int>()

        /**
         * 这块写的不严谨，不应该将这个暴露给外部的，但是目前没有找到更加合适的方法
         * 如果不这样写的话添加完小部件的话会显示不出数据，刷新也不太对。
         */
        private fun setWidgetItemList(widgetItems: ArrayList<Int>) {
            Companion.widgetItems = widgetItems
        }

        /**
         * 刷新List
         */
        fun notifyDeskWidget(
            context: Context?,
            appWidgetId: Int
        ) {
            XLog.e("notifyWeatherWidget: 刷新cal")
            // 刷新数据
            widgetItems.clear()
            val calendar = Calendar.getInstance()
            for (index in 0..6) {
                calendar.add(Calendar.DAY_OF_MONTH, if (index == 0) 0 else 1)
                widgetItems.add(calendar.get(Calendar.DAY_OF_MONTH))
            }
            setWidgetItemList(widgetItems)
            val mgr = AppWidgetManager.getInstance(context)
            mgr.notifyAppWidgetViewDataChanged(
                appWidgetId,
                R.id.stack_view
            )
        }

    }

    override fun onCreate() {
        notifyDeskWidget(context, mAppWidgetId)
    }

    override fun onDataSetChanged() {

    }

    override fun onDestroy() {

    }

    override fun getCount(): Int {
        return widgetItems.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        return RemoteViews(context.packageName, R.layout.widget_desk_item).apply {
            val weather = widgetItems[position]

            setTextViewText(R.id.deskTvDay, weather.toString())
//            setTextViewText(R.id.widget_tv_temp, "${weather.min}-${weather.max}℃")
//            setTextViewText(
//                R.id.widget_tv_city,
//                "${cityInfo?.city ?: ""} ${cityInfo?.name ?: "北京"}"
//            )
//            setImageViewBitmap(
//                R.id.widget_iv_bg,
//                fillet(context = context, bitmap = zoomImg(context, weather.icon), roundDp = 10)
//            )
//            setTextViewText(R.id.widget_tv_date, weather.time)
//            // 可以区分给定项目的单个点击操作
//            val fillInIntent = Intent().apply {
//                putExtra(EXTRA_ITEM, weather.time)
//            }
//            setOnClickFillInIntent(R.id.widget_ll_item, fillInIntent)
        }
    }

    override fun getLoadingView(): RemoteViews {
        return RemoteViews(context.packageName, R.layout.weather_desk_loading)
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