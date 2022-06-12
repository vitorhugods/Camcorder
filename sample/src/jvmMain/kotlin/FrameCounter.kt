import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import xyz.schwaab.camcorder.AnimationSpecs
import xyz.schwaab.camcorder.camcorderPreviewApplication
import kotlin.time.Duration.Companion.seconds

@ExperimentalUnitApi
fun main() {
    camcorderPreviewApplication(AnimationSpecs(256, 35, 1.seconds), sampleFile("frameCounter")) { frame: Int, _: Long ->
        Box(modifier = Modifier.background(Color.Yellow).fillMaxSize()) {
            Text(
                "Frame: $frame",
                fontSize = TextUnit(28f, TextUnitType.Sp),
                color = Color(49, 140, 231),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
