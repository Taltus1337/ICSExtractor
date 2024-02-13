import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import de.franzsw.extractor.App
import de.franzsw.extractor.presentation.SyncRootComponent
import java.awt.Dimension

fun main() = application {

    val root = SyncRootComponent(DefaultComponentContext(LifecycleRegistry()))


    Window(
        title = "Extractor",
        state = rememberWindowState(width = 800.dp, height = 600.dp),
        onCloseRequest = ::exitApplication,
    ) {
        window.minimumSize = Dimension(350, 600)

        val childStack by root.childStack.subscribeAsState()

        Children(
            stack = childStack,
            animation = stackAnimation(slide())
        ) { child ->
            when (val instance = child.instance) {
                is SyncRootComponent.Child.SyncMenu -> {
                    val component = instance.component
                    val state by component.state.subscribeAsState()

                    App(
                        state = state,
                        onEvent = component::onEvent
                    )
                }

            }
        }


    }
}