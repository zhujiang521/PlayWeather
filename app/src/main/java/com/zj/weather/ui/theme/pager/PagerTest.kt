package com.zj.weather.ui.theme.pager

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

@ExperimentalPagerApi
@Composable
fun PagerTest1() {
    HorizontalPager(count = 10, modifier = Modifier.background(color = Color.Red)) { page ->
        Text(text = "Page: $page")
    }
}

@ExperimentalPagerApi
@Composable
fun PagerTest3() {
    VerticalPager(count = 10, modifier = Modifier.background(color = Color.Red)) { page ->
        Text(
            text = "Page: $page",
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Preview(name = "Test1", heightDp = 170)
@Composable
fun PagerTest1Preview() {
    PagerTest1()
}

@OptIn(ExperimentalPagerApi::class)
@Preview(name = "Test3", heightDp = 170, widthDp = 100)
@Composable
fun PagerTest3Preview() {
    PagerTest3()
}

@ExperimentalPagerApi
@Composable
fun PagerTest2() {
    val pagerState = rememberPagerState()
    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(count = 10) { page ->
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
        VerticalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Preview(name = "Test2", heightDp = 170)
@Composable
fun PagerTest2Preview() {
    PagerTest2()
}