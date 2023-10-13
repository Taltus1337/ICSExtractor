package de.franzsw.extractor.presentation

import de.franzsw.extractor.domain.ExportICS
import de.franzsw.extractor.domain.SessionConverter
import de.franzsw.extractor.domain.WorkbookPreview
import de.franzsw.extractor.domain.model.ExtractionConfig
import de.franzsw.extractor.domain.model.EventMeta
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ExtractionViewModel(
    private val sessionConverter: SessionConverter
) {

    private val _state = MutableStateFlow(ExtractionState())
    val state = _state
        .stateIn(CoroutineScope(Dispatchers.Main), SharingStarted.WhileSubscribed(5000), ExtractionState())


    fun onEvent(event: ExtractionEvent) {
        // reset message on each action
        _state.update { it.copy(uiMessage = null) }
        when (event) {
            ExtractionEvent.AddOriginAppointment -> {
                _state.update {
                    it.copy(
                        eventMetas = it.eventMetas.plus(EventMeta("", "", "", false))
                    )
                }
            }

            is ExtractionEvent.DeleteOriginAppointment -> {
                _state.update {
                    it.copy(
                        eventMetas = it.eventMetas.filterIndexed { index, _ -> index != event.index }
                    )
                }
            }

            is ExtractionEvent.ChangeExtractionConfig -> changeExtractionConfig(event.extractionConfig)
            is ExtractionEvent.ChangeOriginAppointment -> changeOriginAppointment(event.index, event.eventMeta)


            ExtractionEvent.LaunchInputFilePicker -> _state.update { it.copy(isInputFilePickerOpen = !it.isInputFilePickerOpen) }
            ExtractionEvent.LaunchOutputFolderPicker -> _state.update { it.copy(isOutputFolderPickerOpen = !it.isOutputFolderPickerOpen) }

            is ExtractionEvent.SelectInputFile -> _state.update { it.copy(inputFile = event.file) }
            is ExtractionEvent.SelectOutputFolder -> _state.update { it.copy(outputFolderPath = event.path) }

            ExtractionEvent.SaveConfig -> {
                state.value.extractionConfig?.let { sessionConverter.saveExtractionConfig(it) }
            }
            ExtractionEvent.LaunchExport -> export()
        }
    }

    private fun changeExtractionConfig(config: ExtractionConfig) {
        _state.updateAndGet {
            it.copy(
                extractionConfig = config,
                preview = it.inputFile?.let { file -> WorkbookPreview().invoke(config,file)}
            )
        }
    }

    private fun changeOriginAppointment(index: Int, alteredAppointment: EventMeta) {
        _state.updateAndGet {
            it.copy(
                eventMetas = it.eventMetas.mapIndexed { idx, appointment ->
                    if (index == idx) {
                        alteredAppointment
                    } else {
                        appointment
                    }
                }
            )
        }
    }


    private fun export() {
        if (state.value.inputFile != null &&
            state.value.outputFolderPath != null &&
            state.value.extractionConfig != null
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                ExportICS().invoke(
                    state.value.eventMetas,
                    state.value.extractionConfig!!,
                    state.value.inputFile!!,
                    state.value.outputFolderPath!!
                )
            }
        } else {
            _state.update {
                it.copy(
                    uiMessage = "Datei / Ordner nicht ausgewält"
                )
            }
        }

    }
}