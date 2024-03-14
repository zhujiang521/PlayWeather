package com.zj.weather.view.city.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zj.model.room.entity.CityInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class CityListViewModel @Inject constructor(
    application: Application,
    private val cityListRepository: CityListRepository
) : AndroidViewModel(application) {

    val cityInfoList: Flow<List<CityInfo>> = cityListRepository.refreshCityList()

    /**
     * 删除相应的城市信息
     */
    fun deleteCityInfo(cityInfo: CityInfo?): Boolean {
        if (cityInfo == null) {
            return false
        }
        val result = runBlocking {
            cityListRepository.deleteCityInfo(cityInfo)
        }
        return result
    }

}