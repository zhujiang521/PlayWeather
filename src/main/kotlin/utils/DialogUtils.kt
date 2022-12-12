package utils

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeDialog
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState
import java.awt.Dimension

@Composable
fun ShowDialog(
    alertDialog: MutableState<Boolean>,
    title: String,
    content: String,
    cancelString: String = "取消",
    confirmString: String = "确定",
    onConfirmListener: () -> Unit
) {
    if (!alertDialog.value) return
    val buttonHeight = 45.dp
    Dialog(
        onCloseRequest = { alertDialog.value = false }, visible = alertDialog.value,
        state = rememberDialogState(size = DpSize(300.dp, 200.dp)),
        title = "Weather", icon = painterResource("image/ic_launcher.svg")
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onSecondary,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = content,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                maxLines = 3,
                modifier = Modifier.padding(
                    top = 12.dp, bottom = 25.dp,
                    start = 20.dp, end = 20.dp
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Divider()
            Row {
                TextButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(buttonHeight),
                    onClick = {
                        alertDialog.value = false
                    }
                ) {
                    Text(
                        text = cancelString,
                        fontSize = 16.sp,
                        maxLines = 1,
                        color = Color(red = 53, green = 128, blue = 186)
                    )
                }
                Divider(
                    modifier = Modifier
                        .width(1.dp)
                        .height(buttonHeight)
                )
                TextButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(buttonHeight),
                    onClick = {
                        alertDialog.value = false
                        onConfirmListener()
                    }
                ) {
                    Text(
                        text = confirmString,
                        fontSize = 16.sp,
                        maxLines = 1,
                        color = Color(red = 53, green = 128, blue = 186)
                    )
                }
            }
        }
    }
}
