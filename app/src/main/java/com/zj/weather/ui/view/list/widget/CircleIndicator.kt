package com.zj.weather.ui.view.list

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.zj.weather.R
import com.zj.weather.ui.view.list.BannerGravity.BottomCenter
import com.zj.weather.ui.view.list.BannerGravity.BottomLeft
import com.zj.weather.ui.view.list.BannerGravity.BottomRight

/**
 * 圆形指示器 eg：。。. 。。
 *
 * @param indicatorColor 指示器默认颜色
 * @param selectIndicatorColor 指示器选中颜色
 * @param indicatorDistance 指示器之间的距离
 * @param indicatorSize 指示器默认圆大小
 * @param selectIndicatorSize 指示器选中圆大小
 * @param gravity 指示器位置
 */
@ExperimentalPagerApi
@Composable
fun DrawIndicator(
    pagerState: PagerState,
    gravity: Int = BottomCenter,
    hasCurrentPosition: Boolean = false,
    indicatorColor: Color = MaterialTheme.colors.onPrimary,
    selectIndicatorColor: Color = MaterialTheme.colors.primary,
    indicatorDistance: Int = 55,
    indicatorSize: Float = 12f,
    selectIndicatorSize: Float = 12f,
) {
    val image = ImageBitmap.imageResource(id = R.mipmap.icon_location)
    for (pageIndex in 0 until pagerState.pageCount) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val color: Color
            val inSize: Float
            if (pageIndex == pagerState.currentPage) {
                color = selectIndicatorColor
                inSize = selectIndicatorSize
            } else {
                color = indicatorColor
                inSize = indicatorSize
            }
            val start = when (gravity) {
                BottomCenter -> {
                    val width = canvasWidth - pagerState.pageCount * indicatorDistance
                    width / 2
                }
                BottomLeft -> {
                    100f
                }
                BottomRight -> {
                    canvasWidth - pagerState.pageCount * indicatorDistance - 100f
                }
                else -> 100f
            }
            if (hasCurrentPosition) {
                if (pageIndex == 0) {
                    drawImage(
                        image = image,
                        colorFilter = ColorFilter.tint(color),
                        topLeft = Offset(
                            start + pageIndex * indicatorDistance - 20f,
                            canvasHeight - 50f - image.height / 2
                        ),
                    )
                } else {
                    drawCircle(
                        color,
                        inSize,
                        center = Offset(start + pageIndex * indicatorDistance, canvasHeight - 50f)
                    )
                }
            } else {
                drawCircle(
                    color,
                    inSize,
                    center = Offset(start + pageIndex * indicatorDistance, canvasHeight - 50f)
                )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Preview(showBackground = false, name = "底部指示器")
@Composable
fun DrawIndicatorPreview() {
    val pagerState = rememberPagerState(initialPage = 1)
    DrawIndicator(pagerState)
}
