package xyz.schwaab.camcorder

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

data class AnimationSpecs(
    val width: Int,
    val height: Int,
    val duration: Duration,
    val fps: Int = 50,
) {
    constructor(
        width: Int,
        height: Int,
        totalFrames: Int,
        fps: Int = 50
    ): this(
        width,
        height,
        (totalFrames / fps).seconds,
        fps
    )

    val frameDurationInMillis: Long get() = 1000L / fps
    val totalFrames = (duration.toInt(DurationUnit.MILLISECONDS) / frameDurationInMillis).toInt()
}
