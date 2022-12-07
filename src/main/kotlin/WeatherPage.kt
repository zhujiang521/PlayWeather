import AppViewModel.Companion.DEFAULT_CITY_ID
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import model.WeatherModel
import view.LeftInformation
import view.RightInformation

@Composable
fun WeatherPage(appViewModel: AppViewModel) {
    val weatherModel by appViewModel.weatherModelData.collectAsState(WeatherModel())
    val currentCityId by appViewModel.currentCityId.collectAsState(DEFAULT_CITY_ID)
    LaunchedEffect(currentCityId) {
        appViewModel.getWeather(currentCityId)
    }

    Row(
        modifier = Modifier.fillMaxSize().padding(10.dp),
    ) {
        // 信息区域（左边）
        LeftInformation(appViewModel, weatherModel.nowBaseBean, currentCityId)

        // 天气展示区域（右边）
        val modifier = Modifier.weight(1f).width(500.dp)
        RightInformation(modifier, weatherModel)
    }

}

