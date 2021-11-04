package com.zj.weather.ui.permission

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.zj.weather.R
import java.lang.Exception

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FeatureThatRequiresCameraPermissions(
    navigateToSettingsScreen: () -> Unit
) {
    // 跟踪用户是否不想再看到基本原理。
    var doNotShowRationale by rememberSaveable { mutableStateOf(false) }

    // 权限状态
    val cameraPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION,
    )

    when {
        // 如果授予相机权限，则显示启用该功能的屏幕
        cameraPermissionState.hasPermission -> {
            Text("Camera permission Granted")
        }
        // 如果用户拒绝权限但应该显示理由，或者用户第一次看到权限，
        // 解释为什么应用程序需要该功能并允许用户再次获得权限或看不到理由没有了。
        cameraPermissionState.shouldShowRationale ||
                !cameraPermissionState.permissionRequested -> {
            if (doNotShowRationale) {
                Text("Feature not available")
            } else {
                Column {
                    Text("The camera is important for this app. Please grant the permission.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                            Text("Request permission")
                        }
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = { doNotShowRationale = true }) {
                            Text("Don't show rationale again")
                        }
                    }
                }
            }
        }
        // 如果未满足上述条件，则用户拒绝该权限。让我们向用户提供常见问题解答，
        // 以防他们想了解更多信息并将其发送到“设置”屏幕，以便将来在那里启用它（如果他们愿意）。
        else -> {
            Column {
                Text(
                    "相机权限被拒绝。请参阅此常见问题解答，了解有关我们为什么需要此权限的信息。请在“设置”屏幕上授予我们访问权限。"
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = navigateToSettingsScreen) {
                    Text("Open Settings")
                }
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

fun isPermissionsGranted(context: Context?, permissions: Array<String?>): Boolean {
    for (permission in permissions) {
        if (!isPermissionGranted(context, permission)) {
            return false
        }
    }
    return true
}

fun isPermissionGranted(context: Context?, permission: String?): Boolean {
    if (context == null || permission == null) return false
    return ActivityCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

/**
 * 权限已经拒绝的情况下弹出对话框让用户跳转到设置进行权限设置
 */
@SuppressLint("ResourceType")
fun onAlertDialog(context: Context?) {
    if (context == null) return
    //Instantiate builder variable
    val builder = AlertDialog.Builder(context, R.style.ThemeOverlay_AppCompat)
    // set title
    builder.setTitle("权限设置")
    //set content area
    builder.setMessage("天气需要获取当前位置以展示当前的天气")
    //set negative button
    builder.setPositiveButton(
        "打开设置"
    ) { dialog, _ ->
        dialog.dismiss()
        startSettingAppPermission(context = context)
    }


    //set positive button
    builder.setNegativeButton(
        "取消"
    ) { dialog, _ ->
        dialog.dismiss()
    }

    builder.show()
}
