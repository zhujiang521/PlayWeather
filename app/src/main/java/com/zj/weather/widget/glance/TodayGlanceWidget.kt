package com.zj.weather.widget.glance

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.zj.weather.MainActivity
import com.zj.weather.R

class TodayGlanceWidget : GlanceAppWidget() {

    @Composable
    override fun Content() {
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
}