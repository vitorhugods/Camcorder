import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import map.CountryShape
import xyz.schwaab.camcorder.AnimationSpecs
import xyz.schwaab.camcorder.camcorderPreviewApplication
import java.lang.Float.max

fun main() {
    val width = 1024
    val height = 1024
    val countryShape = CountryShape.sample()
    val totalFrames = countryShape.vertices.size

    val outputFile = sampleFile("shapeDrawing")
    val animationSpecs = AnimationSpecs(width, height, totalFrames)
    camcorderPreviewApplication(animationSpecs, outputFile) { frame, _ ->

        Canvas(Modifier.fillMaxSize()) {
            val maxSize = max(size.toRect().height, size.toRect().width)
            println(countryShape.extremes)

            countryShape.vertices.take(frame).forEach { vertex ->
                val first = countryShape.transposeCoordinateToCanvas(vertex.first, maxSize)
                val second = countryShape.transposeCoordinateToCanvas(vertex.second, maxSize)
                drawLine(
                    Color.BleuDeFrance,
                    Offset(first.lon, first.lat),
                    Offset(second.lon, second.lat),
                    strokeWidth = 10f
                )
            }
        }
    }
}
