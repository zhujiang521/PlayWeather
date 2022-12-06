import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.Density


/**
 * 构建Painter，为了图片使用
 */
fun buildPainter(resourcePath: String): Painter {
    val painter: Painter = if (resourcePath.endsWith(".svg")) {
        useResource(resourcePath) {
            loadSvgPainter(it, Density(2f))
        }
    } else if (resourcePath.endsWith(".png") || resourcePath.endsWith(".jpg") || resourcePath.endsWith(".jpeg") ||
        resourcePath.endsWith(".webp") || resourcePath.endsWith(".PNG") || resourcePath.endsWith(".JPG") ||
        resourcePath.endsWith(".JPEG") || resourcePath.endsWith(".WEBP") || resourcePath.endsWith(".ICO")
    ) {
        BitmapPainter(useResource(resourcePath, ::loadImageBitmap))
    } else {
        throw IllegalArgumentException("resource is illegal argument")
    }
    return painter
}