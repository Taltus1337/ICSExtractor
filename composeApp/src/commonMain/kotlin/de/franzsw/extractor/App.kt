package de.franzsw.extractor

import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.darkrockstudios.libraries.mpfilepicker.DirectoryPicker
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import de.franzsw.extractor.data.SharedString
import de.franzsw.extractor.data.toCommonString
import de.franzsw.extractor.presentation.ExtractionEvent
import de.franzsw.extractor.presentation.ExtractionState
import de.franzsw.extractor.presentation.components.*
import de.franzsw.extractor.presentation.theme.AppTheme
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc

@Composable
internal fun App(
    state: ExtractionState,
    onEvent: (ExtractionEvent) -> Unit
) = AppTheme {
    Row {
        val listState = rememberLazyListState()



        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {

            item("01") {
                TopRow(
                    onLaunchInputPicker = { onEvent(ExtractionEvent.LaunchInputFilePicker) },
                    inputPath = state.inputFile.orEmpty(),
                    onLaunchOutputPicker = { onEvent(ExtractionEvent.LaunchOutputFolderPicker) },
                    outputPath = state.outputFolderPath.orEmpty()
                )
            }

            item("02_1") {
                Text(
                    text = SharedString.EventConfig.toCommonString(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .padding(8.dp)
                        .padding(bottom = 8.dp)
                )
            }

            item("02") {
                EventsAssembly(
                    events = state.eventMetas,
                    onChangeEvent = { index, event -> onEvent(ExtractionEvent.ChangeEventMeta(index, event)) },
                    onDeleteEvent = { index -> onEvent(ExtractionEvent.DeleteEventMeta(index)) },
                    onAddEvent = { onEvent(ExtractionEvent.AddEventMeta) },
                    modifier = Modifier
                        .padding(8.dp)
                )
            }

            item("03") {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }

            item("04_1") {
                Text(
                    text = SharedString.TableConfig.toCommonString(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .padding(8.dp)
                        .padding(bottom = 8.dp)
                )
            }

            item("04") {
                state.extractionConfig?.let { config ->
                    ConfigAssembly(
                        config = config,
                        onChangeConfig = { onEvent(ExtractionEvent.ChangeExtractionConfig(it)) },
                        modifier = Modifier
                            .padding(8.dp)
                    )
                }
            }

            item("05") {
                PreviewAssembly(
                    preview = state.preview,
                    onLoadPreview = { onEvent(ExtractionEvent.LoadPreview) },
                    isFailedOpenError = state.isFailedLoadFileError,
                    isLoading = state.isPreviewLoading
                )
            }

            item("06") {
                ActionInfoAssembly(
                    onClickExport = { onEvent(ExtractionEvent.LaunchExport) },
                    errorMessage = state.uiMessage,
                    isError = state.isMessageError,
                    onSaveConfigEvents = { onEvent(ExtractionEvent.SaveConfig) },
                    isLoading = state.isExportLoading
                )
            }


        }
        VerticalScrollbar(
            modifier = Modifier
                .fillMaxHeight(),
            adapter = rememberScrollbarAdapter(listState),
            style = LocalScrollbarStyle.current.copy(
                thickness = 10.dp,
                hoverColor = MaterialTheme.colorScheme.secondary,
                unhoverColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
            )
        )
    }


    FilePicker(state.isInputFilePickerOpen, fileExtensions = listOf("xlsx")) { file ->
        if (file != null) {
            onEvent(ExtractionEvent.SelectInputFile(file.path))
        }
    }

    DirectoryPicker(state.isOutputFolderPickerOpen) { path ->
        if (path != null) {
            onEvent(ExtractionEvent.SelectOutputFolder(path))
        }
    }
}
