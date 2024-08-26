package com.zj.weather.widget.glance

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.zj.weather.BaseActivity
import com.zj.weather.widget.today.refreshLocationWeather
import com.zj.weather.widget.utils.ConfigureWidget
import com.zj.model.room.entity.CityInfo
import com.zj.weather.theme.PlayWeatherTheme
import com.zj.weather.view.city.viewmodel.CityListViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * The configuration screen for the AppWidget.
 */
@AndroidEntryPoint
class TodayGlanceWidgetConfigureActivity : BaseActivity() {

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
        refreshLocationWeather(
            context = this,
            cityInfo = cityInfo,
            appWidgetId = appWidgetId,
            prefsName = TODAY_GLANCE_PREFS_NAME
        )

        // Make sure we pass back the original appWidgetId
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }

}

const val TODAY_GLANCE_PREFS_NAME = "com.zj.weather.widget.TodayGlanceWidget"