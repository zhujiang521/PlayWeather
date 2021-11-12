package com.zj.weather.common.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun ShowDialog(
    alertDialog: MutableState<Boolean>,
    title: String? = null,
    content: String,
    cancelString: String,
    confirmString: String,
    onConfirmListener: () -> Unit
) {
    if (!alertDialog.value) return
    Dialog(onDismissRequest = {
        alertDialog.value = false
    }) {
        Card(shape = RoundedCornerShape(10.dp)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 20.dp)
            ) {
                if (title != null) {
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(top = 5.dp)
                    )
                }

                Text(
                    text = content,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 22.dp, bottom = 35.dp)
                )
                Divider()
                Row {
                    TextButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        onClick = {
                            alertDialog.value = false
                        }
                    ) {
                        Text(
                            text = cancelString,
                            fontSize = 16.sp,
                            color = MaterialTheme.colors.secondary
                        )
                    }
                    Divider(
                        modifier = Modifier
                            .width(1.dp)
                            .height(50.dp)
                    )
                    TextButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        onClick = {
                            alertDialog.value = false
                            onConfirmListener()
                        }
                    ) {
                        Text(
                            text = confirmString,
                            fontSize = 16.sp,
                            color = MaterialTheme.colors.secondary
                        )
                    }
                }
            }
        }
    }
}