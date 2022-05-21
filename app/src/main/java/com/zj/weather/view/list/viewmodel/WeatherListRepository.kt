package com.zj.weather.view.list.viewmodel

import android.app.Application
import com.zj.model.*
import com.zj.model.city.GeoBean
import com.zj.model.room.PlayWeatherDatabase
import com.zj.model.room.entity.CityInfo
import com.zj.network.PlayWeatherNetwork
import com.zj.utils.XLog
import com.zj.utils.view.showToast
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class WeatherListRepository @Inject constructor(private val context: Application) {

    private val cityInfoDao = PlayWeatherDatabase.getDatabase(context = context).cityInfoDao()
    private val network = PlayWeatherNetwork(context.applicationContext)

    /**
     * 根据城市名称进行模糊查询
     *
     * @param cityName 城市名称
     */
    suspend fun getGeoCityLookup(cityName: String = "北京"): PlayState<List<GeoBean.LocationBean>> {
        val cityLookup = network.getCityLookup(cityName)
        val code = cityLookup.code.toInt()
        return if (code == SUCCESSFUL) {
            buildHasLocation(cityLookup.location)
            PlaySuccess(cityLookup.location)
        } else {
            val text = getErrorText(code)
            showToast(context, text)
            XLog.e("code:$code, text:$text")
            PlayError(NullPointerException(text))
        }
    }

    /**
     * 构建当前是否存在此城市
     */
    private suspend fun buildHasLocation(locations: MutableList<GeoBean.LocationBean>) {
        locations.forEach {
            val hasLocation = cityInfoDao.getHasLocation(it.name)
            it.hasLocation = hasLocation > 0
        }
    }

    /**
     * 热门城市信息查询
     *
     */
    suspend fun getGeoTopCity(): PlayState<List<GeoBean.LocationBean>> {
        val cityTop = network.getCityTop()
        val code = cityTop.code.toInt()
        return if (code == SUCCESSFUL) {
            buildHasLocation(cityTop.topCityList)
            PlaySuccess(cityTop.topCityList)
        } else {
            val text = getErrorText(code)
            showToast(context, text)
            XLog.e("code:$code, text:$text")
            PlayError(NullPointerException(text))
        }
    }

    /**
     * 插入城市信息
     */
    suspend fun insertCityInfo(cityInfo: CityInfo) {
        val indexList = cityInfoDao.getIndexCity()
        if (indexList.isNotEmpty()) {
            val indexCity = indexList[0]
            indexCity.isIndex = 0
            cityInfoDao.update(indexCity)
        }
        cityInfoDao.insert(cityInfo)
    }

}