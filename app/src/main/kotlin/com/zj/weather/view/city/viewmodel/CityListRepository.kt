package com.zj.weather.view.city.viewmodel

import android.app.Application
import com.zj.model.room.PlayWeatherDatabase
import com.zj.model.room.entity.CityInfo
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class CityListRepository @Inject constructor(context: Application) {

    private val cityInfoDao = PlayWeatherDatabase.getDatabase(context = context).cityInfoDao()

    fun refreshCityList(): Flow<List<CityInfo>> = cityInfoDao.getCityInfoList()

    suspend fun deleteCityInfo(cityInfo: CityInfo) {
        cityInfoDao.delete(cityInfo)
    }

}