package com.zj.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.zj.weather.ui.theme.PlayWeatherTheme
import com.zj.weather.utils.setAndroidNativeLightStatusBar
import com.zj.weather.utils.transparentStatusBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setContent {
            PlayWeatherTheme {
                Surface(color = MaterialTheme.colors.background) {
                    NavGraph()
                }
            }
        }
    }

    /**
     * 初始化View
     */
    private fun initView() {
        // 加载动画
        installSplashScreen()
        // 状态栏透明
        transparentStatusBar()
        // 状态栏反色
        setAndroidNativeLightStatusBar()
    }

}