package com.zj.weather.utils

import android.os.Build
import java.util.*


// 语言是否为中文
val isZhLanguage: Boolean
    get() = Locale.getDefault().language == "zh"

/**
 * 判断当前手机系统版本API是否是29以上。
 * @return 29以上返回true，否则返回false。
 */
val isQOrLater: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

// Android 12.0 S 21
val isSOrLater: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S