package utils

import java.awt.Desktop
import java.net.URI

/**
 * 通过字符串打开系统默认浏览器
 */
fun String?.openBrowse() {
    if (this?.startsWith("http") == false && !this.startsWith("https")) {
        throw IllegalArgumentException("this illegal argument exception")
    }
    try {
        val uri = URI.create(this ?: "https://www.baidu.com")
        // 获取当前系统桌面扩展
        val dp = Desktop.getDesktop()
        // 判断系统桌面是否支持要执行的功能
        if (dp.isSupported(Desktop.Action.BROWSE)) {
            // 获取系统默认浏览器打开链接
            dp.browse(uri)
        }
    } catch (e: Exception) {
        println(e.message)
    }
}