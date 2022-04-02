package com.zj.weather.common.widget.glance

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.*
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.action.actionStartBroadcastReceiver
import androidx.glance.appwidget.action.actionStartService
import androidx.glance.appwidget.background
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.zj.weather.MainActivity
import com.zj.weather.R

class FirstGlanceWidget : GlanceAppWidget() {

    @Composable
    override fun Content() {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(day = Color.Red, night = Color.Blue)
                .cornerRadius(10.dp)
                .padding(8.dp)
        ) {
            Text(
                text = "First Glance widget",
                modifier = GlanceModifier.fillMaxWidth(),
                style = TextStyle(fontWeight = FontWeight.Bold),
            )

            Spacer(modifier = GlanceModifier.height(5.dp))
            Row {
                LazyColumn(modifier = GlanceModifier.width(150.dp)) {
                    items(4) {
                        Text(text = "哈哈哈")
                    }
                }
                Image(
                    provider = ImageProvider(R.mipmap.back_100d),
                    modifier = GlanceModifier.height(50.dp),
                    contentDescription = ""
                )
            }

            Row {
                Text(text = "横着1")

                Text(text = "横着2 ", modifier = GlanceModifier.padding(10.dp))
            }

            val test = ActionParameters.Key<String>("test")
            val testInt = ActionParameters.Key<Int>("test")
            actionParametersOf(test to "测试", testInt to 123)


            // Activity
            Button(text = "Glance按钮", onClick = actionStartActivity(ComponentName("", "")))
            Button(text = "Glance按钮", onClick = actionStartActivity<MainActivity>())
            Button(text = "Glance按钮", onClick = actionStartActivity(MainActivity::class.java))

            // 回调
            Button(text = "Glance按钮", onClick = actionRunCallback<ActionCallbacks>())
            Button(text = "Glance按钮", onClick = actionRunCallback(ActionCallbacks::class.java))

            // Service
            Button(text = "Glance按钮", onClick = actionStartService<TestService>())
            Button(text = "Glance按钮", onClick = actionStartService(TestService::class.java))

            // val size = LocalSize.current

            // 广播
            Button(
                text = "Glance按钮",
                onClick = actionStartBroadcastReceiver<FirstGlanceWidgetReceiver>()
            )
            Button(
                text = "Glance按钮",
                onClick = actionStartBroadcastReceiver(FirstGlanceWidgetReceiver::class.java)
            )
        }
    }
}

class TestService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}

class ActionCallbacks : ActionCallback {
    override suspend fun onRun(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        // 执行需要的操作
    }
}