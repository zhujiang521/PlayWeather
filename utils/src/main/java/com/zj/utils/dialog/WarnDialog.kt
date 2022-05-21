package com.zj.utils.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.zj.utils.R

@Composable
fun ShowWarnDialog(
    alertDialog: MutableState<Boolean>,
    titleId: Int,
    contentId: Int,
    cancelId: Int = R.string.dialog_sure
) {
    ShowWarnDialog(
        alertDialog = alertDialog,
        title = stringResource(id = titleId),
        content = stringResource(id = contentId),
        cancel = stringResource(id = cancelId)
    )
}


@Composable
fun ShowWarnDialog(
    alertDialog: MutableState<Boolean>,
    title: String,
    content: String,
    cancel: String = stringResource(id = R.string.dialog_sure),
) {
    if (!alertDialog.value) return
    val buttonHeight = 45.dp
    Dialog(onDismissRequest = {
        alertDialog.value = false
    }) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Text(
                    text = content,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(
                        top = 12.dp, bottom = 25.dp,
                        start = 20.dp, end = 20.dp
                    )
                )
                Divider()
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(buttonHeight),
                    onClick = {
                        alertDialog.value = false
                    }
                ) {
                    Text(
                        text = cancel,
                        fontSize = 16.sp,
                        color = Color(red = 53, green = 128, blue = 186)
                    )
                }
            }
        }
    }
}