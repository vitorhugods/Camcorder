# Camcorder
Tool to create, preview and save GIF animations use Compose Desktop.

You can use both frame count and/or elapsed time to draw the frames.

## How to use

1. Call `camcorderPreviewApplication` passing `AnimationSpecs`, an output file and a `FrameRenderer` lambda:

```kotlin
@ExperimentalUnitApi
fun main() {
    camcorderPreviewApplication(AnimationSpecs(width, heigth, 1.seconds), "timeCounter.gif") { frame, timeInMillis: Long ->
        Box(modifier = Modifier.background(Color.Green).fillMaxSize()) {
            Text(
                "Time: ${timeInMillis}ms",
                color = bleuDeFrance,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
```

2. Run the application, and you can preview the animation:

![Preview](https://raw.githubusercontent.com/vitorhugods/Camcorder/main/media/preview.png)

3. Click on `Render to GIF`, and the file will be saved:

![Time-based sample](https://raw.githubusercontent.com/vitorhugods/Camcorder/main/media/timeCounter.gif)


See [samples](https://github.com/vitorhugods/Camcorder/tree/main/sample/src/jvmMain/kotlin) for more examples on how to use. For a more complicated example, see [Explosions.kt](https://github.com/vitorhugods/Camcorder/blob/main/sample/src/jvmMain/kotlin/Explosions.kt):

![Explosions sample](https://raw.githubusercontent.com/vitorhugods/Camcorder/main/media/explosions.gif)
