import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import map.CountryShape
import xyz.schwaab.camcorder.AnimationSpecs
import xyz.schwaab.camcorder.camcorderPreviewApplication

fun main() {
    val width = 1024
    val height = 1024
    val countryShape = CountryShape.sample()
    val totalFrames = countryShape.vertices.size

    val outputFile = sampleFile("shapeDrawing")
    val animationSpecs = AnimationSpecs(width, height, totalFrames)

    camcorderPreviewApplication(animationSpecs, outputFile) { frame, _ ->
        Canvas(Modifier.fillMaxSize()) {
            countryShape.vertices.take(frame).forEach { vertex ->
                drawLine(
                    Color.BleuDeFrance,
                    Offset(vertex.first.lon, vertex.first.lat),
                    Offset(vertex.second.lon, vertex.second.lat)
                )
            }
        }
    }
}
