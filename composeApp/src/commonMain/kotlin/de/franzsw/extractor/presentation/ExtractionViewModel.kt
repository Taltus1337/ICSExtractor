package de.franzsw.extractor.presentation

import de.franzsw.extractor.data.SharedString
import de.franzsw.extractor.data.toCommonString
import de.franzsw.extractor.domain.ExportICS
import de.franzsw.extractor.domain.GetWorkbookPreview
import de.franzsw.extractor.domain.SessionConverter
import de.franzsw.extractor.domain.model.EventMeta
import de.franzsw.extractor.domain.model.ExtractionConfig
import de.franzsw.extractor.domain.util.verifyDigitsAndLength
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

    init {
        loadInitialData()
    }

    fun onEvent(event: ExtractionEvent) {
        // reset message on each action
        _state.update { it.copy(uiMessage = null) }
        when (event) {
            ExtractionEvent.AddEventMeta -> {
                _state.update {
                    it.copy(
                        eventMetas = it.eventMetas.plus(EventMeta("", "", "", false))
                    )
                }
            }

            is ExtractionEvent.DeleteEventMeta -> {
                _state.update {
                    it.copy(
                        eventMetas = it.eventMetas.filterIndexed { index, _ -> index != event.index }
                    )
                }
            }

            is ExtractionEvent.ChangeExtractionConfig -> changeExtractionConfig(event.extractionConfig)
            is ExtractionEvent.ChangeEventMeta -> changeOriginAppointment(event.index, event.eventMeta)


            ExtractionEvent.LaunchInputFilePicker -> _state.update {
                it.copy(
                    isInputFilePickerOpen = !it.isInputFilePickerOpen,
                    isFailedLoadFileError = false
                )
            }

            ExtractionEvent.LaunchOutputFolderPicker -> _state.update { it.copy(isOutputFolderPickerOpen = !it.isOutputFolderPickerOpen) }

            is ExtractionEvent.SelectInputFile -> _state.update {
                it.copy(
                    inputFile = event.filePath,
                    isInputFilePickerOpen = false
                )
            }

            is ExtractionEvent.SelectOutputFolder -> _state.update {
                it.copy(
                    outputFolderPath = event.path,
                    isOutputFolderPickerOpen = false
                )
            }

            ExtractionEvent.LoadPreview -> loadPreview()
            ExtractionEvent.SaveConfig -> {
                state.value.extractionConfig?.let { sessionConverter.saveExtractionConfig(it) }
                sessionConverter.saveInitialAppointments(state.value.eventMetas)
            }

            ExtractionEvent.LaunchExport -> export()
        }
    }

    private fun changeExtractionConfig(config: ExtractionConfig) {
        _state.updateAndGet {
            it.copy(
                extractionConfig = config,
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

    private fun loadPreview() {
        _state.update {
            it.copy(
                isPreviewLoading = true,
                isFailedLoadFileError = false,
            )
        }
        CoroutineScope(Dispatchers.IO).launch {
            _state.update { extractionState ->
                if (listOf(extractionState.inputFile, extractionState.extractionConfig).all { it != null }) {
                    try {
                        extractionState.copy(
                            preview = GetWorkbookPreview().invoke(extractionState.extractionConfig!!, extractionState.inputFile!!),
                            isPreviewLoading = false
                        )
                    } catch (e: Exception) {
                        extractionState.copy(
                            uiMessage = e.message,
                            isMessageError = true,
                            isPreviewLoading = false
                        )
                    }
                } else {
                    extractionState.copy(
                        isFailedLoadFileError = true,
                        isPreviewLoading = false
                    )
                }
            }
        }
    }


    private fun export() {
        val errorMessage = mutableListOf<String>()
        _state.update { it.copy(isExportLoading = true) }

        if (state.value.inputFile == null ||
            state.value.outputFolderPath == null ||
            state.value.extractionConfig == null
        ) {
            errorMessage.add(SharedString.ErrorNoFolderFile.toCommonString())
        }

        if (state.value.eventMetas.any() {
                it.end.verifyDigitsAndLength(4) == null ||
                        it.start.verifyDigitsAndLength(4) == null
            }) {
            errorMessage.add(SharedString.ErrorInvalidTimeForEvents.toCommonString())
        }

        if (state.value.extractionConfig?.month?.verifyDigitsAndLength(2) == null) {
            errorMessage.add(SharedString.ErrorInvalidMonthFormat.toCommonString())
        }

        if (state.value.extractionConfig?.year?.verifyDigitsAndLength(4) == null) {
            errorMessage.add(SharedString.ErrorInvalidYearFormat.toCommonString())
        }


        if (errorMessage.isNotEmpty()) {
            _state.update { extractionState ->
                extractionState.copy(
                    uiMessage = buildString {
                        errorMessage.forEach {
                            appendLine(it)
                        }
                    },
                    isMessageError = true,
                    isExportLoading = false
                )
            }
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val result = ExportICS().invoke(
                        state.value.eventMetas,
                        state.value.extractionConfig!!,
                        state.value.inputFile!!,
                        state.value.outputFolderPath!!
                    )
                    _state.update {
                        it.copy(
                            isMessageError = false,
                            uiMessage = result,
                            isExportLoading = false
                        )
                    }
                } catch (e: Exception) {
                    _state.update {
                        it.copy(
                            isMessageError = true,
                            uiMessage = e.message,
                            isExportLoading = false
                        )
                    }
                }

            }
        }
    }

    private fun loadInitialData() {
        _state.update {
            it.copy(
                eventMetas = sessionConverter.getInitialAppointments(),
                extractionConfig = sessionConverter.getExtractionConfig()
            )
        }
    }
}