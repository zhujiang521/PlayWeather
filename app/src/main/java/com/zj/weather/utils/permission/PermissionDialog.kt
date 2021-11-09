package com.zj.weather.utils.permission

import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import com.zj.weather.R

/**
 * 权限已经拒绝的情况下弹出对话框让用户跳转到设置进行权限设置
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ShowPermissionDialog(
    alertDialog: MutableState<Boolean>,
    onConfirmListener: () -> Unit
) {
    if (!alertDialog.value) return
    AlertDialog(
        //当用户尝试通过单击外部或按下后退按钮来关闭对话框时执行。单击关闭按钮时不会调用此方法。
        onDismissRequest = {
            alertDialog.value = false
        },
        //对话框的标题，它应该指定对话框的目的。标题不是强制性的，因为文本中可能有足够的信息。提供的文本样式默认为 Typography.h6
        title = {
            Text(text = stringResource(id = R.string.permission_title), fontSize = 15.sp)
        },
        //显示有关对话框目的的详细信息的文本。提供的文本样式默认为 Typography.body1
        text = {
            Text(
                text = stringResource(id = R.string.permission_content), fontSize = 14.sp
            )
        },
        //一个按钮，用于确认的操作，从而解决触发对话框的原因。该对话框不会为此按钮设置任何事件，因此它们需要由调用者设置。null则不显示
        confirmButton = {
            TextButton(
                onClick = {
                    alertDialog.value = false
                    onConfirmListener()
                }
            ) {
                Text(
                    text = stringResource(id = R.string.permission_sure),
                    fontSize = 15.sp,
                    color = MaterialTheme.colors.secondary
                )
            }
        },
        //用于关闭对话框的按钮。该对话框不会为此按钮设置任何事件，因此它们需要由调用者设置,null则不显示
        dismissButton = {
            TextButton(
                onClick = {
                    alertDialog.value = false
                }
            ) {
                Text(
                    text = stringResource(id = R.string.permission_cancel),
                    fontSize = 15.sp,
                    color = MaterialTheme.colors.secondary
                )
            }
        },
        //对话框的背景颜色
        backgroundColor = MaterialTheme.colors.background,
        //此对话框为其子级提供的首选内容颜色。
        contentColor = MaterialTheme.colors.secondary,
        //平台特定的属性，以进一步配置对话框
        properties = DialogProperties(
            //是否可以通过按下后退按钮来关闭对话框。 如果为 true，按下后退按钮将调用 onDismissRequest。
            dismissOnBackPress = true,
            //是否可以通过在对话框边界外单击来关闭对话框。 如果为 true，单击对话框外将调用 onDismissRequest
            dismissOnClickOutside = true,
            //用于在对话框窗口上设置 WindowManager.LayoutParams.FLAG_SECURE 的策略。
            securePolicy = SecureFlagPolicy.Inherit,
            //对话框内容的宽度是否应限制为平台默认值，小于屏幕宽度。
            usePlatformDefaultWidth = true
        )
    )
}