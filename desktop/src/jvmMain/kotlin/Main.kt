import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.github.foodiestudio.application.FlipCard
import com.github.foodiestudio.application.media.EditorScreen
import com.github.foodiestudio.application.media.VideoEditor

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        FlipCard()
    }
}
