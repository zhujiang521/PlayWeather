package com.zj.weather

import android.app.Application
import com.zj.utils.DataStoreUtils
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
        XLog.d()
        DataStoreUtils.init(this)
    }

}