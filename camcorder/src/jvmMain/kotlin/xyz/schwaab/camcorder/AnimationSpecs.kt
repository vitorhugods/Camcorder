package xyz.schwaab.camcorder

import kotlin.time.Duration
import kotlin.time.DurationUnit

data class AnimationSpecs(
    val width: Int,
    val height: Int,
    val duration: Duration,
    val fps: Int = 50,
) {
    val frameDurationInMillis: Long get() = 1000L / fps
    val totalFrames = (duration.toInt(DurationUnit.MILLISECONDS) / frameDurationInMillis).toInt()
}
