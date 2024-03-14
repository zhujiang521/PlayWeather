package com.zj.weather.view.city.viewmodel

import android.app.Application
import androidx.annotation.VisibleForTesting
import com.zj.model.room.PlayWeatherDatabase
import com.zj.model.room.entity.CityInfo
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class CityListRepository @Inject constructor(context: Application) {

    private val cityInfoDao = PlayWeatherDatabase.getDatabase(context = context).cityInfoDao()

    fun refreshCityList(): Flow<List<CityInfo>> = cityInfoDao.getCityInfoList()

    @VisibleForTesting
    suspend fun deleteCityInfo(cityInfo: CityInfo): Boolean {
        val delete = cityInfoDao.delete(cityInfo)
        return delete > 0
    }

}