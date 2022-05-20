package com.zj.weather.permission

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
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.zj.utils.dialog.ShowDialog
import com.zj.weather.R
import com.zj.weather.view.weather.viewmodel.WeatherViewModel

@ExperimentalPermissionsApi
@Composable
fun FeatureThatRequiresLocationPermissions(weatherViewModel: WeatherViewModel) {
    val context = LocalContext.current
    val alertDialog = rememberSaveable { mutableStateOf(false) }
    // 权限状态
    val locationPermissionState = rememberMultiplePermissionsState(
        permissions = arrayListOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    )

    when {
        // 成功获取权限
        locationPermissionState.allPermissionsGranted -> {
            LaunchedEffect(Unit) {
                getLocation(context, weatherViewModel)
            }
        }
        // 当前没有权限，需要申请权限
        locationPermissionState.shouldShowRationale ||
                !locationPermissionState.allPermissionsGranted -> {
            LaunchedEffect(Unit) {
                locationPermissionState.launchMultiplePermissionRequest()
            }
        }
        // 用户拒绝该权限，弹出对话框提醒用户跳转设置进行获取
        else -> {
            LaunchedEffect(Unit) {
                alertDialog.value = true
            }
            ShowDialog(
                alertDialog = alertDialog,
                title = stringResource(id = R.string.permission_title),
                content = stringResource(id = R.string.permission_content),
                cancelString = stringResource(id = R.string.permission_cancel),
                confirmString = stringResource(id = R.string.permission_sure)
            ) {
                context.startSettingAppPermission()
            }
        }
    }
}

/**
 * 跳转到设置权限页面
 * 联想手机专属
 */
fun Context.startSettingAppPermission() {
    try {
        val intent = Intent("com.zui.safecenter.permissionmanager.AppPermission")
        intent.putExtra("title", getString(R.string.app_name))
        intent.putExtra("package", packageName)
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
        // 其他安卓手机跳转到设置权限页面
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:$packageName")
        startActivity(intent)
    }
}