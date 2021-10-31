package com.zj.weather

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.zj.weather.room.PlayWeatherDatabase
import com.zj.weather.room.entity.CityInfo
import com.zj.weather.ui.theme.PlayWeatherTheme
import com.zj.weather.utils.setAndroidNativeLightStatusBar
import com.zj.weather.utils.transparentStatusBar
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transparentStatusBar()
        setAndroidNativeLightStatusBar()
        val cityInfoDao = PlayWeatherDatabase.getDatabase(this).cityInfoDao()
//        mainViewModel.searchCityInfo.observe(this) { cityInfo ->
//            Log.e(TAG, "onCreate: $cityInfo")
//            mainViewModel.getWeather(cityInfo)
//        }
        var cityInfoList = runBlocking { cityInfoDao.getCityInfoList() }
        if (cityInfoList.isNullOrEmpty()) {
            cityInfoList = listOf(
                CityInfo(
                    location = "CN101010100",
                    name = "北京"
                )
            )
        }
        mainViewModel.getWeather(cityInfoList[0].location)
        mainViewModel.getGeoTopCity()
        setContent {
            PlayWeatherTheme {
                Surface(color = MaterialTheme.colors.background) {
                    // 权限申请
//            FeatureThatRequiresCameraPermissions {
//                startSettingAppPermission(context)
//            }
                    NavGraph(cityList = cityInfoList, mainViewModel = mainViewModel)
                }
            }
        }
    }
}