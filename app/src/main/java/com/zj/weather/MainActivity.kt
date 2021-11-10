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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

@AndroidEntryPoint
class MainActivity : ComponentActivity(), CoroutineScope by MainScope() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        transparentStatusBar()
        setAndroidNativeLightStatusBar()
        setContent {
            PlayWeatherTheme {
                Surface(color = MaterialTheme.colors.background) {
                    NavGraph()
                }
            }
        }
    }

}