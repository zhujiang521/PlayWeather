package com.zj.weather

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.zj.weather.ui.theme.PlayWeatherTheme
import com.zj.weather.utils.setAndroidNativeLightStatusBar
import com.zj.weather.utils.transparentStatusBar

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transparentStatusBar()
        setAndroidNativeLightStatusBar()
//        mainViewModel.searchCityInfo.observe(this) { cityInfo ->
//            Log.e(TAG, "onCreate: $cityInfo")
//            mainViewModel.getWeather(cityInfo)
//        }
        mainViewModel.getGeoTopCity()
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