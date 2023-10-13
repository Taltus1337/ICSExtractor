package de.franzsw.extractor.presentation

import de.franzsw.extractor.domain.model.ExtractionConfig
import de.franzsw.extractor.domain.model.EventMeta
import java.io.File

data class ExtractionState(
    val eventMetas: List<EventMeta> = emptyList(),
    val extractionConfig: ExtractionConfig? = null,
    val inputFile: File? = null,
    val isInputFilePickerOpen: Boolean = false,
    val isOutputFolderPickerOpen: Boolean = false,
    val outputFolderPath: String? = null,
    val uiMessage: String? = null,
    val preview: List<List<String>>? = null
)
