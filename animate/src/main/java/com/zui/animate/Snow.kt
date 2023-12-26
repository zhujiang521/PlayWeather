package com.zui.animate

import androidx.annotation.IntDef
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import kotlin.random.Random


/**
 * 定制四种雪，分别对应不同数量的雪花，数值直接表示雪花数量
 */
@IntDef(
    value = [
        Snows.LIGHT_SNOW,
        Snows.MODERATE_SNOW,
        Snows.HEAVY_SNOW,
        Snows.BLIZZARD,
    ],
)
annotation class SnowsValue

object Snows {
    // 小雪
    const val LIGHT_SNOW = 40

    // 中雪
    const val MODERATE_SNOW = 80

    // 大雪
    const val HEAVY_SNOW = 120

    // 暴雪
    const val BLIZZARD = 160
}

@Composable
fun Snow(modifier: Modifier = Modifier, @SnowsValue snowType: Int = Snows.LIGHT_SNOW) {
    val bitmap = ImageBitmap.imageResource(id = R.drawable.ic_snow)
    Layout(
        modifier = modifier,
        content = {
            // 摆放雪花，分别设置不同duration，增加随机性
            repeat(snowType) {
                SnowAnimate(
                    modifier.size(Random.nextInt(8, 20).dp),
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
fun SnowAnimate(modifier: Modifier = Modifier, bitmap: ImageBitmap, durationMillis: Int = 1000) {
    // 循环播放
    val transition = rememberInfiniteTransition(label = "")

    // 下降动画：restart动画
    val animateY by transition.animateFloat(
        initialValue = 0f,
        targetValue = 2.5f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis, easing = LinearEasing),
            RepeatMode.Restart
        ), label = ""
    )

    // 左右飘动
    val animateX by transition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis / 3, easing = LinearEasing),
            RepeatMode.Reverse
        ), label = ""
    )

    // alpha值
    val animateAlpha by transition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis, easing = FastOutSlowInEasing),
        ), label = ""
    )

    Canvas(modifier) {
        // 位置随AnimationState改变，实现雪花飘落的效果
        val topLeft = center.copy(
            x = center.x + center.x * animateX,
            y = center.y + center.y * animateY
        )
        drawImage(bitmap, topLeft = topLeft, alpha = animateAlpha)
    }
}