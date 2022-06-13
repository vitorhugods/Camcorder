import androidx.compose.ui.graphics.Color
import java.io.File

fun sampleFile(fileName: String) = File("media/$fileName.gif")


val Color.Companion.BleuDeFrance get() = Color(49, 140, 231)