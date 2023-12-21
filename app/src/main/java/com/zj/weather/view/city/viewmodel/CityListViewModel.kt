package com.zj.weather.view.city.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.zj.model.room.entity.CityInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
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
    fun deleteCityInfo(cityInfo: CityInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            cityListRepository.deleteCityInfo(cityInfo)
        }
    }

    /**
     * 修改应该显示的城市
     */
    fun updateCityInfoIndex(cityInfo: CityInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            cityListRepository.updateCityIsIndex(cityInfo)
        }
    }

}