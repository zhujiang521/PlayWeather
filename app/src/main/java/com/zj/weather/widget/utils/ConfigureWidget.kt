package com.zj.weather.widget.utils

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.google.gson.Gson
import com.zj.weather.R
import com.zj.weather.widget.week.PREF_PREFIX_KEY
import com.zj.model.room.entity.CityInfo
import com.zj.weather.view.city.viewmodel.CityListViewModel

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ConfigureWidget(
    viewModel: CityListViewModel,
    onCancelListener: () -> Unit,
    onConfirmListener: (CityInfo) -> Unit
) {
    val cityList by viewModel.cityInfoList.observeAsState(arrayListOf())
    val buttonHeight = 45.dp
    val pagerState = rememberPagerState()
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(80.dp))
        Text(
            text = stringResource(id = R.string.widget_city_choose),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 26.sp,
            color = Color(red = 53, green = 128, blue = 186)
        )
        Box(modifier = Modifier.weight(1f)) {
            HorizontalPager(
                state = pagerState,
                count = cityList.size,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    backgroundColor = MaterialTheme.colors.onSecondary,
                    modifier = Modifier.size(300.dp)
                ) {
                    val cityInfo = cityList[page]
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(text = cityInfo.name, fontSize = 30.sp)
                    }
                }
            }
            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
            )
        }
        Spacer(modifier = Modifier.height(50.dp))
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
        )
        Row {
            TextButton(
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight),
                onClick = {
                    onCancelListener()
                }
            ) {
                Text(
                    text = stringResource(id = R.string.city_dialog_cancel),
                    fontSize = 16.sp,
                    color = Color(red = 53, green = 128, blue = 186)
                )
            }
            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .height(buttonHeight)
            )
            TextButton(
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight),
                onClick = {
                    onConfirmListener(cityList[pagerState.currentPage])
                }
            ) {
                Text(
                    text = stringResource(id = R.string.city_dialog_confirm),
                    fontSize = 16.sp,
                    color = Color(red = 53, green = 128, blue = 186)
                )
            }
        }
    }
}

// Write the prefix to the SharedPreferences object for this widget
internal fun saveCityInfoPref(
    context: Context,
    appWidgetId: Int,
    cityInfo: CityInfo,
    prefsName: String
) {
    context.getSharedPreferences(prefsName, 0).edit {
        putString(PREF_PREFIX_KEY + appWidgetId, Gson().toJson(cityInfo))
    }
}

// Read the prefix from the SharedPreferences object for this widget.
// If there is no preference saved, get the default from a resource
internal fun loadCityInfoPref(context: Context, appWidgetId: Int, prefsName: String): CityInfo? {
    val prefs = context.getSharedPreferences(prefsName, 0)
    val cityString = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null) ?: ""
    if (cityString.isEmpty()) return null
    return Gson().fromJson(cityString, CityInfo::class.java)
}

internal fun deleteCityInfoPref(context: Context, appWidgetId: Int, prefsName: String) {
    val prefs = context.getSharedPreferences(prefsName, 0).edit()
    prefs.remove(PREF_PREFIX_KEY + appWidgetId)
    prefs.apply()
}