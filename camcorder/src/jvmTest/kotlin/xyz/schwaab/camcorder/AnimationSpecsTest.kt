package xyz.schwaab.camcorder

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

class AnimationSpecsTest {

    @Test
    fun givenTotalFramesAndFPS_whenCreatingAnimationSpecs_thenDurationShouldBeCorrect() {
        val totalFrames = 100
        val fps = 50
        val duration = 2.seconds

        val specs = AnimationSpecs(500, 500, totalFrames, fps)

        assertEquals(duration, specs.duration)
    }

    @Test
    fun givenDurationAndFPS_whenCreatingAnimationSpecs_thenTotalFramesShouldBeCorrect() {
        val duration = 2.seconds
        val fps = 50
        val totalFrames = 100

        val specs = AnimationSpecs(500, 500, duration, fps)

        assertEquals(totalFrames, specs.totalFrames)
    }
}