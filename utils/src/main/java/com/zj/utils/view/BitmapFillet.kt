package com.zj.utils.view

import android.content.Context
import android.graphics.*
import com.zj.utils.XLog
import com.zj.utils.view.BitmapFillet.CORNER_ALL
import com.zj.utils.view.BitmapFillet.CORNER_BOTTOM
import com.zj.utils.view.BitmapFillet.CORNER_BOTTOM_LEFT
import com.zj.utils.view.BitmapFillet.CORNER_BOTTOM_RIGHT
import com.zj.utils.view.BitmapFillet.CORNER_LEFT
import com.zj.utils.view.BitmapFillet.CORNER_NONE
import com.zj.utils.view.BitmapFillet.CORNER_RIGHT
import com.zj.utils.view.BitmapFillet.CORNER_TOP
import com.zj.utils.view.BitmapFillet.CORNER_TOP_LEFT
import com.zj.utils.view.BitmapFillet.CORNER_TOP_RIGHT
import com.zj.utils.weather.IconUtils

/**
 * Created by zhujiang on 21/12/1.
 *
 * 设置圆角
 *
 * [CORNER_NONE] 四个角都不设置
 * [CORNER_TOP_LEFT] 左上
 * [CORNER_TOP_RIGHT] 右上
 * [CORNER_BOTTOM_LEFT] 左下
 * [CORNER_BOTTOM_RIGHT] 右下
 * [CORNER_ALL] 四个角都设置
 * [CORNER_TOP] 上面两个角
 * [CORNER_BOTTOM] 下面两个角
 * [CORNER_LEFT] 左边两个角
 * [CORNER_RIGHT] 右边两个角
 */
object BitmapFillet {

    const val CORNER_NONE = 0
    const val CORNER_TOP_LEFT = 1
    const val CORNER_TOP_RIGHT = 1 shl 1
    const val CORNER_BOTTOM_LEFT = 1 shl 2
    const val CORNER_BOTTOM_RIGHT = 1 shl 3
    const val CORNER_ALL =
        CORNER_TOP_LEFT or CORNER_TOP_RIGHT or CORNER_BOTTOM_LEFT or CORNER_BOTTOM_RIGHT
    const val CORNER_TOP = CORNER_TOP_LEFT or CORNER_TOP_RIGHT
    const val CORNER_BOTTOM = CORNER_BOTTOM_LEFT or CORNER_BOTTOM_RIGHT
    const val CORNER_LEFT = CORNER_TOP_LEFT or CORNER_BOTTOM_LEFT
    const val CORNER_RIGHT = CORNER_TOP_RIGHT or CORNER_BOTTOM_RIGHT

    /**
     * 将普通Bitmap按照centerCrop的方式进行截取
     */
    fun zoomImg(context: Context, icon: String): Bitmap {
        val bitmap = getWeatherBgBitmap(context, icon)
        return zoomImg(bitmap)
    }

    /**
     * 将普通Bitmap按照centerCrop的方式进行截取
     */
    fun zoomImg(bm: Bitmap): Bitmap {
        val w = bm.width // 得到图片的宽，高
        val h = bm.height
        val retX: Int
        val retY: Int
        val wh = w.toDouble() / h.toDouble()
        val nwh = w.toDouble() / w.toDouble()
        if (wh > nwh) {
            retX = h * w / w
            retY = h
        } else {
            retX = w
            retY = w * w / w
        }
        val startX = if (w > retX) (w - retX) / 2 else 0 //基于原图，取正方形左上角x坐标
        val startY = if (h > retY) (h - retY) / 2 else 0
        val bit = Bitmap.createBitmap(bm, startX, startY, retX, retY, null, false)
        bm.recycle()
        return bit
    }

    private fun getWeatherBgBitmap(
        context: Context,
        icon: String
    ): Bitmap {
        val resId = IconUtils.getWeatherBack(context, icon)
        return BitmapFactory.decodeResource(context.resources, resId)
    }

    /**
     * 图片设置圆角
     *
     * @param bitmap 图片
     * @param roundDp 圆角值：PX
     * @param corners 设置那个角是圆角，默认为四个角全部圆角
     * @return 设置圆角后的图片
     */
    fun fillet(context: Context, bitmap: Bitmap, roundDp: Int, corners: Int = CORNER_ALL): Bitmap {
        val roundPx = context.dp2px(roundDp.toFloat())
        XLog.e("roundDp:$roundDp  roundPx:$roundPx   corners:$corners")
        return try {
            // 其原理就是：先建立一个与图片大小相同的透明的Bitmap画板
            // 然后在画板上画出一个想要的形状的区域。
            // 最后把源图片帖上。
            val width = bitmap.width
            val height = bitmap.height
            val paintingBoard = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(paintingBoard)
            canvas.drawARGB(
                Color.TRANSPARENT,
                Color.TRANSPARENT,
                Color.TRANSPARENT,
                Color.TRANSPARENT
            )
            val paint = Paint()
            paint.isAntiAlias = true
            paint.color = Color.BLACK

            //画出4个圆角
            val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
            canvas.drawRoundRect(rectF, roundPx.toFloat(), roundPx.toFloat(), paint)

            //把不需要的圆角去掉
            val notRoundedCorners = corners xor CORNER_ALL
            if (notRoundedCorners and CORNER_TOP_LEFT != 0) {
                clipTopLeft(canvas, paint, roundPx)
            }
            if (notRoundedCorners and CORNER_TOP_RIGHT != 0) {
                clipTopRight(canvas, paint, roundPx, width)
            }
            if (notRoundedCorners and CORNER_BOTTOM_LEFT != 0) {
                clipBottomLeft(canvas, paint, roundPx, height)
            }
            if (notRoundedCorners and CORNER_BOTTOM_RIGHT != 0) {
                clipBottomRight(canvas, paint, roundPx, width, height)
            }
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

            //帖子图
            val src = Rect(0, 0, width, height)
            canvas.drawBitmap(bitmap, src, src, paint)
            paintingBoard
        } catch (exp: Exception) {
            XLog.e(exp.message)
            bitmap
        }
    }

    private fun clipTopLeft(canvas: Canvas, paint: Paint, offset: Int) {
        val block = Rect(0, 0, offset, offset)
        canvas.drawRect(block, paint)
    }

    private fun clipTopRight(canvas: Canvas, paint: Paint, offset: Int, width: Int) {
        val block = Rect(width - offset, 0, width, offset)
        canvas.drawRect(block, paint)
    }

    private fun clipBottomLeft(canvas: Canvas, paint: Paint, offset: Int, height: Int) {
        val block = Rect(0, height - offset, offset, height)
        canvas.drawRect(block, paint)
    }

    private fun clipBottomRight(
        canvas: Canvas,
        paint: Paint,
        offset: Int,
        width: Int,
        height: Int
    ) {
        val block = Rect(width - offset, height - offset, width, height)
        canvas.drawRect(block, paint)
    }

}

/**
 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
 */
private fun Context.px2dp(pxValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

/**
 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
 */
private fun Context.dp2px(dpValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}