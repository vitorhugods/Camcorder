package xyz.schwaab.camcorder

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.squareup.gifencoder.GifEncoder
import com.squareup.gifencoder.ImageOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt
import kotlin.system.measureTimeMillis
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

typealias FrameRenderer = @Composable (frame: Int, timeInMillis: Long) -> Unit

fun camcorderPreviewApplication(
    animationSpecs: AnimationSpecs,
    outputFile: File,
    frameRenderer: FrameRenderer,
) = application { CamcorderPreviewWindow(animationSpecs, outputFile, frameRenderer) }

@Composable
fun ApplicationScope.CamcorderPreviewWindow(
    animationSpecs: AnimationSpecs,
    outputFile: File,
    animationFrameRenderer: FrameRenderer
) {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Camcorder Preview",
        state = rememberWindowState(
            position = WindowPosition(alignment = Alignment.Center),
            width = Dp.Unspecified, height = Dp.Unspecified
        ),
    ) {
        MaterialTheme {
            AnimationControls(animationSpecs, outputFile, animationFrameRenderer)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalUnitApi::class, ExperimentalTime::class)
@Composable
private fun AnimationControls(
    specs: AnimationSpecs,
    outputFile: File,
    renderer: FrameRenderer
) {
    val isRendering = remember { mutableStateOf(false) }
    val currentFrameState = remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(16.dp)) {
        Text(
            "Preview, frame ${currentFrameState.value} of ${specs.totalFrames}",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        val frame = currentFrameState.value
        val time = frame * specs.frameDurationInMillis
        Image(
            bitmap = renderFrame(specs, frame, time, renderer)
                .render()
                .toComposeImageBitmap(),
            contentDescription = "Animation Preview",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Slider(
            value = currentFrameState.value.toFloat(),
            valueRange = 0f..specs.totalFrames.toFloat(),
            enabled = !isRendering.value,
            modifier = Modifier.width(256.dp).align(Alignment.CenterHorizontally),
            onValueChange = {
                currentFrameState.value = it.roundToInt()
            })

        Button(modifier = Modifier.align(Alignment.CenterHorizontally), enabled = !isRendering.value, onClick = {
            isRendering.value = true
            scope.launch(Dispatchers.Default) {
                renderGif(specs, currentFrameState, outputFile, renderer)
                isRendering.value = false
            }
        }) {
            Text("Render To GIF")
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
private fun renderFrame(specs: AnimationSpecs, frame: Int, time: Long, renderer: FrameRenderer) =
    ImageComposeScene(specs.width, specs.height) {
        renderer(frame, time)
    }

@ExperimentalTime
@ExperimentalUnitApi
@ExperimentalComposeUiApi
private fun renderGif(
    specs: AnimationSpecs,
    currentFrameState: MutableState<Int>,
    outputFile: File,
    renderer: FrameRenderer
) {
    val totalFrames = specs.totalFrames
    val frameDurationInMillis = specs.frameDurationInMillis
    val outputStream = ByteArrayOutputStream()
    val options = ImageOptions().setDelay(frameDurationInMillis, TimeUnit.MILLISECONDS)
    val gifEncoder = GifEncoder(outputStream, specs.width, specs.height, 0)

    (0..totalFrames).forEach { iteration ->
        currentFrameState.value = iteration
        print("Rendering Iteration $iteration of $totalFrames")
        val measuredPixelMap = measureTimedValue {
            val bitmap = renderFrame(specs, iteration, iteration * frameDurationInMillis, renderer).render().toComposeImageBitmap()
            bitmap.toPixelMap()
        }
        print(" Took ${measuredPixelMap.duration.inWholeMilliseconds}ms to render and convert to PixelMap")

        val pixelMap = measuredPixelMap.value

        val addFrameTime = measureTimeMillis {
            gifEncoder.addImage(pixelMap.buffer, pixelMap.width, options)
        }

        println(" Took ${addFrameTime}ms to add frame.")
    }

    gifEncoder.finishEncoding()
    outputFile.parentFile.mkdirs()
    outputFile.writeBytes(outputStream.toByteArray())
}

