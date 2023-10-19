package de.franzsw.extractor.presentation

import de.franzsw.extractor.domain.model.ExtractionConfig
import de.franzsw.extractor.domain.model.EventMeta
import de.franzsw.extractor.domain.model.WorkbookPreview

data class ExtractionState(
    val eventMetas: List<EventMeta> = emptyList(),
    val extractionConfig: ExtractionConfig? = null,
    val inputFile: String? = null,
    val isInputFilePickerOpen: Boolean = false,
    val isOutputFolderPickerOpen: Boolean = false,
    val outputFolderPath: String? = null,
    val isMessageError: Boolean = false,
    val uiMessage: String? = null,
    val isFailedLoadFileError: Boolean = false,
    val preview: WorkbookPreview? = null,
    val isPreviewLoading: Boolean = false,
    val isExportLoading: Boolean = false
)
