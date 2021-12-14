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
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.zj.weather.R
import com.zj.weather.common.dialog.ShowDialog
import com.zj.weather.ui.view.weather.viewmodel.WeatherViewModel

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
    PermissionsRequired(
        multiplePermissionsState = locationPermissionState,
        permissionsNotGrantedContent = {
            LaunchedEffect(Unit) {
                locationPermissionState.permissions.forEach {
                    it.launchPermissionRequest()
                }
            }
        },
        permissionsNotAvailableContent = {
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
                startSettingAppPermission(context)
            }
        }) {
        LaunchedEffect(Unit) {
            getLocation(context, weatherViewModel)
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