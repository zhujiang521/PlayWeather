package com.zj.weather.utils

import kotlinx.coroutines.Job


/**
 * 检查协程是否存在并运行
 */
fun Job?.checkCoroutines() {
    if (this?.isActive == true) return
    this?.cancel()
    XLog.d("已在查询，先取消之前的协程")
}