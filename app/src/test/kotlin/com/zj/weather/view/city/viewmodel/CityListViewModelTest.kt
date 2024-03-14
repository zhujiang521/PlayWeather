package com.zj.weather.view.city.viewmodel

import android.app.Application
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.collectLatest
import org.junit.Test


class CityListViewModelTest {


    @Test
    fun getCityInfoList() {
        val viewModel = CityListViewModel(
            MockApplication(),
            CityListRepository(MockApplication())
        )
//        val cityInfoList = viewModel.cityInfoList
//        cityInfoList.collectLatest {
//            Log.e("TAG", "getCityInfoList: $it")
//        }
        val result = viewModel.deleteCityInfo(null)
        assert(result)
    }

    @Test
    fun deleteCityInfo() {

    }
}