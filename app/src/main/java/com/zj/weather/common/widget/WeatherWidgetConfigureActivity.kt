package com.zj.weather.common.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.gson.Gson
import com.zj.weather.BaseActivity
import com.zj.weather.R
import com.zj.weather.room.entity.CityInfo
import com.zj.weather.ui.theme.PlayWeatherTheme
import com.zj.weather.ui.view.city.viewmodel.CityListViewModel
import com.zj.weather.ui.view.list.widget.DrawIndicator
import com.zj.weather.utils.XLog
import dagger.hilt.android.AndroidEntryPoint

/**
 * The configuration screen for the [WeatherWidget] AppWidget.
 */
@AndroidEntryPoint
class WeatherWidgetConfigureActivity : BaseActivity() {

    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private val viewModel by viewModels<CityListViewModel>()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED)
        // Find the widget id from the intent.
        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            appWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }
        viewModel.refreshCityList()
        setContent {
            PlayWeatherTheme {
                Surface(color = MaterialTheme.colors.background) {
                    ConfigureWidget(
                        viewModel,
                        onCancelListener = {
                            setResult(RESULT_CANCELED)
                            finish()
                        }) { cityInfo ->
                        onConfirm(cityInfo)
                    }
                }
            }
        }
    }

    private fun onConfirm(cityInfo: CityInfo) {
        XLog.e("onConfirm:${cityInfo}")
        val context = this@WeatherWidgetConfigureActivity
        saveTitlePref(context, appWidgetId, cityInfo)

        // It is the responsibility of the configuration activity to update the app widget
        val appWidgetManager = AppWidgetManager.getInstance(context)
        updateAppWidget(context, appWidgetManager, appWidgetId)

        // Make sure we pass back the original appWidgetId
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }

}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ConfigureWidget(
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
            text = "小部件城市选择",
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
            DrawIndicator(pagerState = pagerState)
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

private const val PREFS_NAME = "com.zj.weather.common.widget.WeatherWidget"
private const val PREF_PREFIX_KEY = "appwidget_"

// Write the prefix to the SharedPreferences object for this widget
internal fun saveTitlePref(context: Context, appWidgetId: Int, cityInfo: CityInfo) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.putString(PREF_PREFIX_KEY + appWidgetId, Gson().toJson(cityInfo))
    prefs.apply()
}

// Read the prefix from the SharedPreferences object for this widget.
// If there is no preference saved, get the default from a resource
internal fun loadTitlePref(context: Context, appWidgetId: Int): CityInfo? {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    val cityString = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null) ?: ""
    if (cityString.isEmpty()) return null
    return Gson().fromJson(cityString, CityInfo::class.java)
}

internal fun deleteTitlePref(context: Context, appWidgetId: Int) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.remove(PREF_PREFIX_KEY + appWidgetId)
    prefs.apply()
}