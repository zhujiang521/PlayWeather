package com.zj.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.zj.weather.ui.theme.PlayWeatherTheme
import com.zj.weather.utils.setAndroidNativeLightStatusBar
import com.zj.weather.utils.transparentStatusBar

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transparentStatusBar()
        setAndroidNativeLightStatusBar()
        mainViewModel.getWeather()
        mainViewModel.getGeoTopCity()
        mainViewModel.getGeoCityLookup()
        setContent {
            PlayWeatherTheme {
                Surface(color = MaterialTheme.colors.background) {
                    // 权限申请
//            FeatureThatRequiresCameraPermissions {
//                startSettingAppPermission(context)
//            }
                    NavGraph(mainViewModel = mainViewModel)
                }
            }
        }
    }
}