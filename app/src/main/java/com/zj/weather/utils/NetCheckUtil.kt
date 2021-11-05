package com.zj.weather.utils

import android.content.Context
import android.net.ConnectivityManager


object NetCheckUtil {
    fun checkNet(context: Context): Boolean {
        // 判断是否具有可以用于通信渠道
        val mobileConnection = isMobileConnection(context)
        val wifiConnection = isWIFIConnection(context)
        return !(!mobileConnection && !wifiConnection)
    }

    /**
     * 判断手机接入点（APN）是否处于可以使用的状态
     *
     * @param context
     * @return
     */
    private fun isMobileConnection(context: Context): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        return networkInfo != null && networkInfo.isConnected
    }

    /**
     * 判断当前wifi是否是处于可以使用状态
     *
     * @param context
     * @return
     */
    private fun isWIFIConnection(context: Context): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        return networkInfo != null && networkInfo.isConnected
    }
}

