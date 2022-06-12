# Camcorder
Tool to create, preview and save GIF animations use Compose Desktop.

You can use both frame count and/or elapsed time to draw the frames.

## How to use

```kotlin
@ExperimentalUnitApi
fun main() {
    camcorderPreviewApplication(AnimationSpecs(256, 35, 1.seconds), sampleFile("timeCounter")) { frame, timeInMillis: Long ->
        Box(modifier = Modifier.background(Color.Green).fillMaxSize()) {
            Text(
                "Time: ${timeInMillis}ms",
                fontSize = TextUnit(28f, TextUnitType.Sp),
                color = Color(49, 140, 231),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
```

Output:

![Time-based sample](https://raw.githubusercontent.com/vitorhugods/Camcorder/main/output/timeCounter.gif)


See [samples](https://github.com/vitorhugods/Camcorder/tree/main/sample/src/jvmMain/kotlin) for more examples on how to use. For a more complicated example, see [Explosions.kt](https://github.com/vitorhugods/Camcorder/blob/main/sample/src/jvmMain/kotlin/Explosions.kt):

![Explosions sample](https://raw.githubusercontent.com/vitorhugods/Camcorder/main/output/explosions.gif)
