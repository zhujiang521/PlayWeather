package com.zj.weather.ui.view.tiger

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun TigerWidget() {
    Canvas(modifier = Modifier
        .size(360.dp)
        .background(color = Color.Red)) {
        val size = this.size
        val path = Path()
        path.moveTo(size.width / 2, 300f)
        val start = size.width / 6

        // 画圆
        path.addOval(Rect(start, 300f, start * 5, 300 + start * 5 - start))

        // 写头上的王字
        path.addRect(Rect(start * 3 - 40f, 300f + 60f, start * 3 + 40f, 300f + 65f))
        path.addRect(Rect(start * 3 - 50f, 300f + 85f, start * 3 + 50f, 300f + 90f))
        path.addRect(Rect(start * 3 - 35f, 300f + 110f, start * 3 + 35f, 300f + 115f))
        path.addRect(Rect(start * 3 - 2.5f, 300f + 60f, start * 3 + 2.5f, 300f + 115f))

        // 画眉毛
        path.addRect(Rect(start * 1.5f + 50, 300f + 200f, start * 1.5f + 130f, 300f + 205f))
        path.addRect(Rect(start * 3.5f + 50, 300f + 200f, start * 3.5f + 130f, 300f + 205f))

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
                250f,
                start * 2.2f,
                250f + start
            )
        )
        path.addOval(
            Rect(
                start * 3.8f,
                250f,
                start * 4.8f,
                250f + start
            )
        )

        path.close()
        drawPath(
            path = path, color = Color.Yellow,
            style = Stroke(width = 10f)
        )
    }

}