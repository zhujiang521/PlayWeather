package com.zj.weather.ui.view.list.viewmodel

import android.app.Application
import com.google.gson.Gson
import com.qweather.sdk.bean.base.Code
import com.qweather.sdk.bean.base.Lang
import com.qweather.sdk.bean.base.Range
import com.qweather.sdk.bean.geo.GeoBean
import com.qweather.sdk.view.QWeather
import com.zj.weather.R
import com.zj.weather.common.PlayError
import com.zj.weather.common.PlayNoContent
import com.zj.weather.common.PlayState
import com.zj.weather.common.PlaySuccess
import com.zj.weather.room.PlayWeatherDatabase
import com.zj.weather.room.entity.CityInfo
import com.zj.weather.utils.XLog
import com.zj.weather.utils.checkNetConnect
import com.zj.weather.utils.showToast
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume

@ViewModelScoped
class WeatherListRepository @Inject constructor(private val context: Application) {

    private val cityInfoDao = PlayWeatherDatabase.getDatabase(context = context).cityInfoDao()

    /**
     * 根据城市名称进行模糊查询
     *
     * @param cityName 城市名称
     */
    suspend fun getGeoCityLookup(cityName: String = "北京") =
        suspendCancellableCoroutine<PlayState<List<GeoBean.LocationBean>>> { continuation ->
            if (!context.checkNetConnect()) {
                continuation.resume(PlayError(IllegalStateException("无网络链接")))
                return@suspendCancellableCoroutine
            }
            QWeather.getGeoCityLookup(context, cityName, object : QWeather.OnResultGeoListener {
                override fun onError(e: Throwable) {
                    continuation.resume(PlayNoContent(context.getString(R.string.add_location_warn2)))
                    XLog.e("getGeoCityLookup onError: ${e.message}")
                    showToast(context, R.string.add_location_warn2)
                }

                override fun onSuccess(geoBean: GeoBean?) {
                    if (geoBean == null) {
                        continuation.resume(PlayError(NullPointerException("返回值为空")))
                        XLog.d("getGeoCityLookup onError: 返回值为空")
                        return
                    }
                    val json = Gson().toJson(geoBean)
                    XLog.d("getGeoCityLookup onSuccess: $json")
                    // 先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                    if (Code.OK === geoBean.code) {
                        continuation.resume(PlaySuccess(geoBean.locationBean))
                    } else {
                        //在此查看返回数据失败的原因
                        val code: Code = geoBean.code
                        XLog.w("getGeoCityLookup failed code: $code")
                        showToast(context = context, code.txt)
                    }
                }
            })
        }

    /**
     * 热门城市信息查询
     *
     * @param lang 获取的语言
     */
    suspend fun getGeoTopCity(lang: Lang) =
        suspendCancellableCoroutine<PlayState<List<GeoBean.LocationBean>>> { continuation ->
            if (!context.checkNetConnect()) {
                continuation.resume(PlayError(IllegalStateException("无网络链接")))
                return@suspendCancellableCoroutine
            }
            QWeather.getGeoTopCity(context, 20, Range.CN, lang,
                object : QWeather.OnResultGeoListener {
                    override fun onError(e: Throwable) {
                        continuation.resume(PlayNoContent(context.getString(R.string.add_location_warn2)))
                        XLog.e("getGeoTopCity onError: $e")
                        showToast(context, R.string.add_location_warn2)
                    }

                    override fun onSuccess(geoBean: GeoBean?) {
                        if (geoBean == null) {
                            continuation.resume(PlayError(NullPointerException("返回值为空")))
                            XLog.e("getGeoTopCity onError: 返回值为空")
                            return
                        }
                        val json = Gson().toJson(geoBean)
                        XLog.d("getGeoTopCity onSuccess: $json")
                        // 先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                        if (Code.OK === geoBean.code) {
                            continuation.resume(PlaySuccess(geoBean.locationBean))
                        } else {
                            //在此查看返回数据失败的原因
                            val code: Code = geoBean.code
                            XLog.w("getGeoTopCity failed code: $code")
                            continuation.resume(PlayError(NullPointerException(code.txt)))
                            showToast(context, code.txt)
                        }
                    }
                })
        }

    /**
     * 插入城市信息
     */
    suspend fun insertCityInfo(cityInfo: CityInfo) {
        val hasLocation = cityInfoDao.getHasLocation(cityInfo.name)
        if (hasLocation <= 0) {
            val indexList = cityInfoDao.getIndexCity()
            if (indexList.isNotEmpty()) {
                val indexCity = indexList[0]
                indexCity.isIndex = 0
                cityInfoDao.update(indexCity)
            }
            cityInfoDao.insert(cityInfo)
        } else {
            XLog.e("insertCityInfo: hasLocation：$hasLocation")
            withContext(Dispatchers.Main) {
                showToast(context = context, R.string.add_location_warn)
            }
        }
    }

}