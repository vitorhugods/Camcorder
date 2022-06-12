@file:OptIn(ExperimentalUnitApi::class)

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import xyz.schwaab.camcorder.AnimationSpecs
import xyz.schwaab.camcorder.camcorderPreviewApplication
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

@ExperimentalUnitApi
fun main() {
    val numberOfExplosions = 150
    val width = 500
    val height = 500
    val duration = 5.seconds
    val explosions = buildExplosionsList(numberOfExplosions, width, height, duration)

    camcorderPreviewApplication(AnimationSpecs(width, height, duration), sampleFile("explosions")) { _: Int, elapsedTimeInMillis: Long ->
        Text(
            "Explosions!",
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center,
            color = Color.White,
            style = TextStyle(fontSize = TextUnit(24f, TextUnitType.Sp))
        )

        Canvas(Modifier.fillMaxSize()) {
            explosions.forEach { explosion ->
                val currentSize = explosion.sizeAtTime(elapsedTimeInMillis)
                if (currentSize > 0) {
                    val center = Offset(explosion.x.toFloat(), explosion.y.toFloat())
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(Color.Yellow, Color.Red),
                            center = center,
                            radius = explosion.size.toFloat()
                        ),
                        radius = currentSize.toFloat(),
                        center = center
                    )
                }
            }
        }
    }
}

private fun buildExplosionsList(
    numberOfExplosions: Int,
    width: Int,
    height: Int,
    totalAnimationDuration: Duration
): List<Explosion> = buildList(numberOfExplosions) {
    repeat(numberOfExplosions) {
        add(
            Explosion(
                Random.nextInt(0, width),
                Random.nextInt(0, height),
                Random.nextInt(0, totalAnimationDuration.toInt(DurationUnit.MILLISECONDS)),
                Random.nextInt(0, Explosion.MAX_SIZE)
            )
        )
    }
}

data class Explosion(val x: Int, val y: Int, val t0: Int, val size: Int) {

    private val explosionCurve = FastOutSlowInEasing

    fun sizeAtTime(time: Long): Int =
        if (t0 > time) 0
        else {
            val elapsedTime = time - t0
            if (elapsedTime > DURATION) 0
            else {
                val progress = elapsedTime.toFloat() / DURATION
                (size * explosionCurve.transform(1f - progress)).roundToInt()
            }
        }

    companion object {
        const val DURATION = 500
        const val MAX_SIZE = 35
    }
}
