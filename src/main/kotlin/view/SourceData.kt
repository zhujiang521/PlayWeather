package view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import utils.buildPainter

@Composable
fun SourceData() {
    Row(
        modifier = Modifier.fillMaxSize().padding(top = 20.dp, bottom = 15.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(painter = buildPainter("image/ic_launcher.svg"), "", modifier = Modifier.size(15.dp))

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = "数据来自和风天气",
            fontSize = 12.sp,
        )
    }
}
