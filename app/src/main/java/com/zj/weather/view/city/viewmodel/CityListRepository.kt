package com.zj.weather.view.city.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.zj.model.room.PlayWeatherDatabase
import com.zj.model.room.entity.CityInfo
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class CityListRepository @Inject constructor(private val context: Application) {

    private val cityInfoDao = PlayWeatherDatabase.getDatabase(context = context).cityInfoDao()

    fun refreshCityList(): LiveData<List<CityInfo>> = cityInfoDao.getCityInfoList()

    suspend fun deleteCityInfo(cityInfo: CityInfo) {
        cityInfoDao.delete(cityInfo)
    }

    suspend fun updateCityIsIndex(cityInfo: CityInfo) {
        val indexList = cityInfoDao.getIndexCity()
        if (indexList.isNotEmpty()) {
            val indexCity = indexList[0]
            indexCity.isIndex = 0
            cityInfoDao.update(indexCity)
        }
        cityInfo.isIndex = 1
        cityInfoDao.update(cityInfo)
    }

}