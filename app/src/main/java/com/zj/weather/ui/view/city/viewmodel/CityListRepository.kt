package com.zj.weather.ui.view.city.viewmodel

import android.app.Application
import com.zj.weather.room.PlayWeatherDatabase
import com.zj.weather.room.entity.CityInfo
import com.zj.weather.utils.weather.makeDefault
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class CityListRepository @Inject constructor(private val context: Application) {

    private val cityInfoDao = PlayWeatherDatabase.getDatabase(context = context).cityInfoDao()

    suspend fun refreshCityList(): List<CityInfo> {
        var cityInfoList = cityInfoDao.getCityInfoList()
        cityInfoList = makeDefault(context, cityInfoList)
        return cityInfoList
    }

    suspend fun deleteCityInfo(cityInfo: CityInfo) {
        cityInfoDao.delete(cityInfo)
    }

    suspend fun updateCityIsIndex(cityInfo: CityInfo) {
        cityInfo.isIndex = 1
        cityInfoDao.update(cityInfo)
    }

}