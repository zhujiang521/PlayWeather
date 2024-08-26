package com.zj.weather.widget.glance

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.zj.model.PlaySuccess
import com.zj.model.weather.WeatherNowBean
import com.zj.utils.XLog
import com.zj.utils.weather.IconUtils.getWeatherBack
import com.zj.utils.weather.IconUtils.getWeatherIcon
import com.zj.weather.MainActivity
import com.zj.weather.widget.WeatherWidgetUtils
import com.zj.weather.widget.utils.loadCityInfoPref

class TodayGlanceWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val appWidgetId = GlanceAppWidgetManager(context).getAppWidgetId(id)
        val cityInfo = loadCityInfoPref(context, appWidgetId, TODAY_GLANCE_PREFS_NAME)
        val state = WeatherWidgetUtils.getWeatherNow(context, cityInfo)

        provideContent {
            when (state) {
                is PlaySuccess -> {
                    SuccessWeather(state.data)
                }

                else -> {
                    WarnWeather()
                    XLog.w("Loading")
                }
            }
        }
    }

    override fun onCompositionError(
        context: Context,
        glanceId: GlanceId,
        appWidgetId: Int,
        throwable: Throwable
    ) {
        super.onCompositionError(context, glanceId, appWidgetId, throwable)
        XLog.w("glanceId:$glanceId appWidgetId:$appWidgetId throwable:$throwable")
    }


    @Composable
    private fun WarnWeather() {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(4.dp)
                .cornerRadius(12.dp)
                .background(ImageProvider(com.zj.utils.R.mipmap.back_100d))
                .clickable(actionStartActivity<MainActivity>()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "NoWeather",
                style = TextStyle(color = ColorProvider(Color.White), fontSize = 15.sp)
            )
        }
    }

    @Composable
    private fun SuccessWeather(data: WeatherNowBean.NowBaseBean) {
        val context = LocalContext.current
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(4.dp)
                .cornerRadius(12.dp)
                .background(ImageProvider(getWeatherBack(context, data.icon)))
                .padding(12.dp)
                .clickable(actionStartActivity<MainActivity>()),
            horizontalAlignment = Alignment.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = GlanceModifier.padding(top = 5.dp),
                text = data.city ?: "北京",
                style = TextStyle(fontSize = 13.sp, color = ColorProvider(Color.White))
            )
            Text(
                modifier = GlanceModifier.padding(top = 5.dp),
                text = "${data.temp} ℃",
                style = TextStyle(fontSize = 30.sp, color = ColorProvider(Color.White))
            )
            Row(
                modifier = GlanceModifier.fillMaxWidth().defaultWeight(),
                horizontalAlignment = Alignment.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    provider = ImageProvider(getWeatherIcon(data.icon)),
                    contentDescription = "",
                    modifier = GlanceModifier.size(65.dp),
                    contentScale = ContentScale.FillBounds
                )
                Column {
                    Text(
                        text = data.text ?: "今天",
                        style = TextStyle(fontSize = 11.sp, color = ColorProvider(Color.White))
                    )
                    Text(
                        text = data.windDir ?: "东北风",
                        style = TextStyle(fontSize = 11.sp, color = ColorProvider(Color.White))
                    )
                }
            }
        }
    }

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        super.onDelete(context, glanceId)
    }

}