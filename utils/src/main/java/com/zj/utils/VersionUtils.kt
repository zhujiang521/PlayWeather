package com.zj.utils

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


/**
 * 判断当前手机系统版本API是否是30以上。
 * @return 30以上返回true，否则返回false。
 */
val isROrLater: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

// Android 12.0 S 31
val isSOrLater: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S