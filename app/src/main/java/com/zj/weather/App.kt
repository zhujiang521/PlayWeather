package com.zj.weather

import android.app.Application
import com.amap.api.location.AMapLocationClient
import com.zj.utils.XLog
import dagger.hilt.android.HiltAndroidApp


/**
 * Application
 *
 * @author jiang zhu on 2021/10/28
 */
@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initGaoDeMap()
        XLog.d()
    }

    /**
     * 初始化高德地图
     */
    private fun initGaoDeMap() {
        // 确保调用SDK任何接口前先调用更新隐私合规updatePrivacyShow、updatePrivacyAgree两个接口并且参数值都为true，若未正确设置有崩溃风险
        AMapLocationClient.updatePrivacyShow(this, true, true)
        AMapLocationClient.updatePrivacyAgree(this, true)
    }

}