package com.zj.weather.theme.pager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.*

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Sample() {
    Column(
        Modifier
            .fillMaxWidth()
            .height(200.dp)) {
        val pagerState = rememberPagerState()
        HorizontalPager(
            count = 10,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 32.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) { page ->
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .shadow(2.dp, shape = RoundedCornerShape(10))
                    .fillMaxSize()
                    .background(color = Color.Red)
            ) {
                Text(text = "Page: $page")
            }
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
        )

        VerticalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Preview(name = "Sample", heightDp = 200)
@Composable
fun SamplePreview() {
    Sample()

    val pagerState = rememberPagerState()

//    LaunchedEffect(pagerState) {
//        // Collect from the pager state a snapshotFlow reading the currentPage
//        snapshotFlow { pagerState.currentPage }.collect { page ->
//            AnalyticsService.sendPageSelectedEvent(page)
//        }
//    }

    VerticalPager(
        count = 10,
        state = pagerState,
    ) { page ->
        Text(text = "Page: $page")
    }
}