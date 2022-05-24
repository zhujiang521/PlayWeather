package com.zj.weather.widget.glance

import android.content.Context
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
import com.zj.model.*
import com.zj.model.weather.WeatherNowBean
import com.zj.utils.XLog
import com.zj.utils.weather.IconUtils.getWeatherBack
import com.zj.utils.weather.IconUtils.getWeatherIcon
import com.zj.weather.MainActivity
import com.zj.weather.R

class TodayGlanceWidget(private val state: PlayState<WeatherNowBean.NowBaseBean>) :
    GlanceAppWidget() {

    @Composable
    override fun Content() {
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

    @Composable
    private fun WarnWeather() {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(4.dp)
                .cornerRadius(12.dp)
                .background(ImageProvider(R.mipmap.back_100d))
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
                text = data.city,
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
                        text = data.text,
                        style = TextStyle(fontSize = 11.sp, color = ColorProvider(Color.White))
                    )
                    Text(
                        text = data.windDir,
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