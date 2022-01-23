package com.zj.weather.ui.view.tiger

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun TigerWidget() {
    var isBig by remember { mutableStateOf(true) }
    val bigSize by animateFloatAsState(
        if (isBig) 300f else 150f,
        animationSpec = spring(Spring.StiffnessVeryLow)
    )
    Canvas(
        modifier = Modifier
            .size(360.dp)
            .background(color = Color.Red)
            .clickable {
                isBig = !isBig
            }
    ) {
        val size = this.size
        val path = Path()
        path.moveTo(size.width / 2, bigSize)
        val start = size.width / 6

        // 画圆
        path.addOval(Rect(start, bigSize, start * 5, 300 + start * 5 - start))

        // 写头上的王字
        path.addRect(Rect(start * 3 - 40f, bigSize + 60f, start * 3 + 40f, bigSize + 65f))
        path.addRect(Rect(start * 3 - 50f, bigSize + 85f, start * 3 + 50f, bigSize + 90f))
        path.addRect(Rect(start * 3 - 35f, bigSize + 110f, start * 3 + 35f, bigSize + 115f))
        path.addRect(Rect(start * 3 - 2.5f, bigSize + 60f, start * 3 + 2.5f, bigSize + 115f))

        // 画眉毛
        path.addRect(Rect(start * 1.5f + 50, bigSize + 200f, start * 1.5f + 130f, bigSize + 205f))
        path.addRect(Rect(start * 3.5f + 50, bigSize + 200f, start * 3.5f + 130f, bigSize + 205f))

        // 画眼睛
        path.addOval(
            Rect(
                start * 1.5f + 50,
                550f,
                start * 1.5f + 130f,
                550 + start * 1.5f + 130f - (start * 1.5f + 50)
            )
        )
        path.addOval(
            Rect(
                start * 3.5f + 50,
                550f,
                start * 3.5f + 130f,
                550 + start * 3.5f + 130f - (start * 3.5f + 50)
            )
        )

        // 画鼻子
        path.addRect(
            Rect(
                start * 3 - 40f,
                680f,
                start * 3 + 40f,
                680f + start * 3 + 40f - (start * 3 - 40f)
            )
        )

        // 画嘴
        path.addOval(
            Rect(
                start * 3 - 100f,
                850f,
                start * 3 + 100f,
                850f + start * 3 + 40f - (start * 3 - 40f)
            )
        )

        // 画胡须
        path.addRect(
            Rect(
                start - 70f,
                680f,
                start + 70f,
                685f
            )
        )
        path.addRect(
            Rect(
                start - 100f,
                725f,
                start + 100f,
                730f
            )
        )
        path.addRect(
            Rect(
                start - 80f,
                770f,
                start + 80f,
                775f
            )
        )
        path.addRect(
            Rect(
                start * 5 - 70f,
                680f,
                start * 5 + 70f,
                685f
            )
        )
        path.addRect(
            Rect(
                start * 5 - 100f,
                725f,
                start * 5 + 100f,
                730f
            )
        )
        path.addRect(
            Rect(
                start * 5 - 80f,
                770f,
                start * 5 + 80f,
                775f
            )
        )

        // 画耳朵
        path.addOval(
            Rect(
                start * 1.2f,
                bigSize - 50f,
                start * 2.2f,
                bigSize - 50f + start
            )
        )
        path.addOval(
            Rect(
                start * 3.8f,
                bigSize - 50f,
                start * 4.8f,
                bigSize - 50f + start
            )
        )

        path.close()
        drawPath(
            path = path, color = Color.Yellow,
            style = Stroke(width = 10f)
        )
    }

}