package de.franzsw.extractor.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import de.franzsw.extractor.domain.SessionConverter
import kotlinx.serialization.Serializable

class SyncRootComponent(
    componentContext: ComponentContext
): ComponentContext by componentContext {

    private val navigation = StackNavigation<Configuration>()

    val childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.Process,
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun createChild(
        config: Configuration,
        context: ComponentContext
    ): Child {
        return when(config) {
            Configuration.Process -> Child.SyncMenu(
                ProcessComponent(
                    componentContext = context,
                    sessionConverter = SessionConverter()
                )
            )
        }
    }


//    navigation.pushNew(Configuration.SyncAuth(text))
    sealed class Child {
        data class SyncMenu(val component: ProcessComponent): Child()
    }

    @Serializable
    sealed class Configuration {
        @Serializable
        data object Process: Configuration()

    }
}