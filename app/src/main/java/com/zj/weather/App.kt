package com.zj.weather

import android.app.Application
import com.qweather.sdk.view.HeConfig
import dagger.hilt.android.HiltAndroidApp


/**
 * Application
 *
 * @author jiang zhu on 2021/10/28
 */
@HiltAndroidApp
class App : Application() {

    companion object {
        // 和风天气 Public Id
        private const val WEATHER_PUBLIC_ID = "HE2110282138361578"
        // 和风天气 Key
        private const val WEATHER_KEY = "efe3cd759fa643f4a1507214db523e62"
    }

    override fun onCreate() {
        super.onCreate()
        // 初始化和风天气
        HeConfig.init(WEATHER_PUBLIC_ID, WEATHER_KEY)
        //切换至开发版服务
        HeConfig.switchToDevService()
    }

}