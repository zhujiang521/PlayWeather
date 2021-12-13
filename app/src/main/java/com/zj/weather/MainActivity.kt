package com.zj.weather

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import com.google.gson.Gson
import com.zj.weather.common.GrayAppAdapter
import com.zj.weather.room.entity.CityInfo
import com.zj.weather.ui.theme.PlayWeatherTheme
import com.zj.weather.utils.XLog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private var defaultCityInfo: CityInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cityInfoString = intent.getStringExtra(WIDGET_CITY_INFO) ?: ""
        if (cityInfoString.isNotEmpty()) {
            defaultCityInfo = Gson().fromJson(cityInfoString, CityInfo::class.java)
        }

        setContent {
            PlayWeatherTheme {
                GrayAppAdapter {
                    NavGraph(defaultCityInfo = defaultCityInfo)
                }
            }
        }
    }

    companion object {

        private const val WIDGET_CITY_INFO = "widget_city_info"

        fun actionNewStart(context: Context, cityInfo: CityInfo?) {
            XLog.e("cityInfo:$cityInfo")
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(WIDGET_CITY_INFO, Gson().toJson(cityInfo))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }
    }

}