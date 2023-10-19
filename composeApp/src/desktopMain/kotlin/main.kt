import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Dimension
import de.franzsw.extractor.App
import de.franzsw.extractor.SharedRes
import de.franzsw.extractor.domain.SessionConverter
import de.franzsw.extractor.presentation.ExtractionViewModel

fun main() = application {
    val sessionConverter = SessionConverter()

    val viewModel = ExtractionViewModel(
        sessionConverter = sessionConverter
    )

    val state by viewModel.state.collectAsState()

    Window(
        title = "Extractor",
        state = rememberWindowState(width = 800.dp, height = 600.dp),
        onCloseRequest = ::exitApplication,
    ) {
        window.minimumSize = Dimension(350, 600)
        App(
            state = state,
            onEvent = viewModel::onEvent
        )
    }
}