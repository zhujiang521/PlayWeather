package com.zj.weather.utils.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.zj.weather.R
import com.zj.weather.common.dialog.ShowDialog
import com.zj.weather.ui.view.weather.viewmodel.WeatherViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FeatureThatRequiresLocationPermissions(weatherViewModel: WeatherViewModel) {
    val context = LocalContext.current
    val alertDialog = rememberSaveable { mutableStateOf(false) }
    // 权限状态
    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION,
    )
    when {
        // 如果授予定位权限，则显示启用该功能的屏幕
        locationPermissionState.hasPermission -> {
            LaunchedEffect(Unit) {
                getLocation(context, weatherViewModel)
            }
        }
        // 申请权限
        locationPermissionState.shouldShowRationale ||
                !locationPermissionState.permissionRequested -> {
            LaunchedEffect(Unit) {
                locationPermissionState.launchPermissionRequest()
            }
        }
        // 如果未满足上述条件，则用户拒绝该权限。让我们向用户提供常见问题解答，
        // 以防他们想了解更多信息并将其发送到“设置”屏幕，以便将来在那里启用它（如果他们愿意）。
        else -> {
            alertDialog.value = true
            ShowDialog(
                alertDialog = alertDialog,
                title = stringResource(id = R.string.permission_title),
                content = stringResource(id = R.string.permission_content),
                cancelString = stringResource(id = R.string.permission_cancel),
                confirmString = stringResource(id = R.string.permission_sure)
            ) {
                startSettingAppPermission(context)
            }
        }
    }
}

/**
 * 跳转到设置权限页面
 * 联想手机专属
 */
fun startSettingAppPermission(context: Context) {
    try {
        val intent = Intent("com.zui.safecenter.permissionmanager.AppPermission")
        intent.putExtra("title", context.getString(R.string.app_name))
        intent.putExtra("package", context.packageName)
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
        startSettingAppDetails(context)
    }
}

/**
 * 其他安卓手机跳转到设置权限页面
 */
fun startSettingAppDetails(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.parse("package:" + context.packageName)
    context.startActivity(intent)
}