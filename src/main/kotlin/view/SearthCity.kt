package view

import AppViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import buildPainter
import kotlinx.coroutines.launch
import model.city.GeoBean

@Composable
fun SearchCity(modifier: Modifier, appViewModel: AppViewModel, backClick: () -> Unit) {
    var inputText by rememberSaveable() { mutableStateOf("") }

    LaunchedEffect(inputText) {
        if (inputText.isNotBlank()) {
            appViewModel.searchCity(inputText)
        } else {
            appViewModel.searchCity()
        }
    }
    val scope = rememberCoroutineScope()

    val locationListData by appViewModel.locationListData.collectAsState(arrayListOf())
    val topLocationListData by appViewModel.topLocationListData.collectAsState(arrayListOf())
    Column(modifier = modifier) {

        Row(
            modifier = Modifier.fillMaxWidth().padding(end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = backClick) {
                Icon(Icons.Sharp.ArrowBack, "")
            }

            Box(
                modifier = Modifier.weight(1f).padding(end = 10.dp).height(30.dp)
                    .background(color = Color.LightGray, shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center,
            ) {
                BasicTextField(
                    value = inputText,
                    onValueChange = {
                        inputText = it
                    },
                    modifier = Modifier.fillMaxWidth().padding(5.dp),
                    textStyle = TextStyle(fontSize = 14.sp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {

                    }),
                    maxLines = 1,
                )
            }

            Text(
                if (inputText.isNotBlank()) "取消" else "编辑",
                fontSize = 15.sp,
                modifier = Modifier.clickable {
                    if (inputText.isNotBlank()) {
                        inputText = ""
                        appViewModel.clearSearchCity()
                    }
                })

        }

        LazyColumn {

            if (locationListData.isNotEmpty()) {
                items(locationListData) { location ->
                    SearchCityItem(location) {
                        backClick()
                        scope.launch {
                            appViewModel.getWeather(it)
                        }
                        appViewModel.clearSearchCity()
                    }
                }
            } else {
                items(topLocationListData) { location ->
                    CityItem(location) {
                        backClick()
                        scope.launch {
                            appViewModel.getWeather(it)
                        }
                    }
                }
            }

        }

    }
}

@Composable
fun CityItem(locationBean: GeoBean.LocationBean, onChooseCity: (GeoBean.LocationBean) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().clickable { onChooseCity(locationBean) }) {

        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Text("${locationBean.name}", fontSize = 15.sp, maxLines = 1)

            Text("${locationBean.adm1}${locationBean.adm2}", fontSize = 15.sp, maxLines = 1)

            Image(painter = buildPainter("drawable/100.svg"), "")

        }

        Divider(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp))
    }
}

@Composable
fun SearchCityItem(
    locationBean: GeoBean.LocationBean,
    onChooseCity: (GeoBean.LocationBean) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().clickable { onChooseCity(locationBean) }) {
        val name = if (locationBean.adm2 == locationBean.name) locationBean.name else {
            "${locationBean.adm2}${locationBean.name}"
        }
        Text(
            "${locationBean.adm1}$name",
            fontSize = 15.sp,
            modifier = Modifier.padding(10.dp),
            maxLines = 1
        )

        Divider(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp))
    }
}
