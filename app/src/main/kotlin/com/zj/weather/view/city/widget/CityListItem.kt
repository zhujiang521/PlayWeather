package com.zj.weather.view.city.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.rememberSwipeableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zj.model.room.entity.CityInfo
import com.zj.utils.swipe.SwipeDeleteLayout
import com.zj.utils.view.ImageLoader
import com.zj.weather.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CityListItem(
    cityInfo: CityInfo,
    isShowDelete: Boolean = true,
    toWeatherDetails: (CityInfo) -> Unit,
    onDeleteListener: (CityInfo) -> Unit
) {
    val swipeState = rememberSwipeableState(0)

    val coroutineScope = rememberCoroutineScope()
    SwipeDeleteLayout(swipeState = swipeState, isShowChild = isShowDelete, childContent = {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 6.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(0.dp, 5.dp, 5.dp, 0.dp),
                backgroundColor = MaterialTheme.colors.primaryVariant
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(color = Color.Red)
                        .clickable {
                            onDeleteListener(cityInfo)
                            coroutineScope.launch {
                                swipeState.animateTo(0)
                            }
                        }
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.city_delete),
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }
        }
    }) {
        Card(
            shape = RoundedCornerShape(5.dp), modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)
                .clickable {
                    toWeatherDetails(cityInfo)
                }
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp, vertical = 10.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = cityInfo.name,
                        color = Color.Unspecified, fontSize = 18.sp
                    )
                    if (cityInfo.isLocation == 1) {
                        ImageLoader(
                            data = R.drawable.ic_baseline_location_on_24,
                            modifier = Modifier.wrapContentWidth(Alignment.End)
                        )
                    }
                }
                Text(
                    text = "${cityInfo.province} ${cityInfo.city}",
                    modifier = Modifier.padding(top = 3.dp),
                    color = Color.Unspecified
                )
            }
        }

    }
}

@Preview(showBackground = false, name = "城市item")
@Composable
fun CityItemPreview() {
    val cityInfo = CityInfo(
        name = "朱江",
        province = "微子国",
        city = "南街"
    )
    CityListItem(cityInfo = cityInfo, true, {}, {})
}
