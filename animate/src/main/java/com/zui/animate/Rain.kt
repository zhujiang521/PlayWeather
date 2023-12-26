package com.zui.animate

import androidx.annotation.IntDef
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import kotlin.random.Random

/**
 * 定制四种雨，分别对应不同数量的雨滴，数值直接表示雨滴数量
 */
@IntDef(
    value = [
        Rains.LIGHT_RAIN,
        Rains.MODERATE_RAIN,
        Rains.HEAVY_RAIN,
        Rains.RAINSTORM,
    ],
)
annotation class RainsValue

object Rains {
    // 小雨
    const val LIGHT_RAIN = 100

    // 中雨
    const val MODERATE_RAIN = 150

    // 大雨
    const val HEAVY_RAIN = 200

    // 暴雨
    const val RAINSTORM = 300
}

@Composable
fun Rain(modifier: Modifier = Modifier, @RainsValue rainType: Int = Rains.LIGHT_RAIN) {
    val bitmap = ImageBitmap.imageResource(id = R.drawable.ic_rain)

    Layout(
        modifier = modifier,
        content = {
            // 摆放雪花，分别设置不同duration，增加随机性
            repeat(rainType) {
                RainDrop(
                    modifier.size(1.dp, Random.nextInt(8, 20).dp),
                    bitmap,
                    Random.nextInt(1000, 2500)
                )
            }
        }
    ) { measures, constraints ->
        val placeableList = measures.mapIndexed { _, measurable ->
            constraints.maxHeight * Random.nextFloat()
            val height = constraints.maxHeight * Random.nextFloat()
            measurable.measure(
                constraints.copy(
                    minWidth = 0,
                    minHeight = 0,
                    maxWidth = 10.dp.toPx().toInt(), // snowdrop width
                    maxHeight = height.roundToInt(),
                )
            )
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            var xPosition = constraints.maxWidth / ((placeableList.size + 1))

            placeableList.forEachIndexed { _, placeable ->
                placeable.place(x = xPosition, y = -(constraints.maxHeight * 0.2).roundToInt())

                xPosition += (constraints.maxWidth / ((placeableList.size + 1) * 0.9f)).roundToInt()
            }
        }
    }
}

@Composable
fun RainDrop(modifier: Modifier = Modifier, bitmap: ImageBitmap, durationMillis: Int = 100) {
    // 循环播放
    val transition = rememberInfiniteTransition(label = "")

    // 下降动画：restart动画
    val animateY by transition.animateFloat(
        initialValue = 0f,
        targetValue = 18f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis, easing = FastOutLinearInEasing),
            RepeatMode.Restart
        ), label = ""
    )

    Canvas(modifier) {
        // 位置随AnimationState改变，实现雪花飘落的效果
        val topLeft = center.copy(
            x = center.x,
            y = center.y + center.y * animateY
        )
        drawImage(bitmap, topLeft = topLeft)
    }
}
