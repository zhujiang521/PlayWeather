package com.zj.weather.widget.glance

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.*
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.zj.model.PlayError
import com.zj.model.PlayLoading
import com.zj.model.PlayNoContent
import com.zj.model.PlaySuccess
import com.zj.utils.XLog
import com.zj.weather.MainActivity
import com.zj.weather.R
import com.zj.weather.widget.WeatherWidgetUtils
import com.zj.weather.widget.utils.loadCityInfoPref

class TodayGlanceWidget : GlanceAppWidget() {

    @Composable
    override fun Content() {
        LoadData()
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(4.dp)
                .cornerRadius(12.dp)
                .background(ImageProvider(R.mipmap.back_100d))
                .padding(12.dp)
                .clickable(actionStartActivity<MainActivity>()),
            horizontalAlignment = Alignment.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = GlanceModifier.padding(top = 5.dp),
                text = "昌平区",
                style = TextStyle(fontSize = 13.sp, color = ColorProvider(Color.White))
            )
            Text(
                modifier = GlanceModifier.padding(top = 5.dp),
                text = "29 ℃",
                style = TextStyle(fontSize = 30.sp, color = ColorProvider(Color.White))
            )
            Row(
                modifier = GlanceModifier.fillMaxWidth().defaultWeight(),
                horizontalAlignment = Alignment.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    provider = ImageProvider(R.drawable.day_icon),
                    contentDescription = "",
                    modifier = GlanceModifier.size(40.dp)
                )
                Column(modifier = GlanceModifier.padding(start = 10.dp)) {
                    Text(
                        text = "晴",
                        style = TextStyle(fontSize = 11.sp, color = ColorProvider(Color.White))
                    )
                    Text(
                        text = "17度/36度",
                        style = TextStyle(fontSize = 11.sp, color = ColorProvider(Color.White))
                    )
                }
            }
        }
    }

    @Composable
    private fun LoadData() {
        val context = LocalContext.current
        val instance = AppWidgetManager.getInstance(context)
        val appWidgetIds = instance.getAppWidgetIds(
            ComponentName(
                context,
                TodayGlanceReceiver::class.java
            )
        )
        appWidgetIds.forEach { appWidgetId ->
            val cityInfo = loadCityInfoPref(context, appWidgetId, TODAY_GLANCE_PREFS_NAME)
            WeatherWidgetUtils.getWeatherNow(context, cityInfo?.location) {
                when (it) {
                    is PlaySuccess -> {
                        XLog.w("it.data:${it.data}")
                    }
                    PlayLoading -> {
                        XLog.w("PlayLoading")
                    }
                    is PlayNoContent -> {
                        XLog.w("PlayNoContent")
                    }
                    is PlayError -> {
                        XLog.w("PlayError:${it.error}")
                    }
                }
            }
        }
    }
}