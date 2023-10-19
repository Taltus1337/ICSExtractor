package de.franzsw.extractor.presentation

import de.franzsw.extractor.domain.model.EventMeta
import de.franzsw.extractor.domain.model.ExtractionConfig

sealed class ExtractionEvent {
    data object AddEventMeta : ExtractionEvent()
    data class ChangeEventMeta(val index: Int, val eventMeta: EventMeta) : ExtractionEvent()
    data class DeleteEventMeta(val index: Int) : ExtractionEvent()

    data class ChangeExtractionConfig(val extractionConfig: ExtractionConfig) : ExtractionEvent()

    data object LaunchInputFilePicker : ExtractionEvent()
    data class SelectInputFile(val filePath: String) : ExtractionEvent()
    data object LaunchOutputFolderPicker : ExtractionEvent()
    data class SelectOutputFolder(val path: String) : ExtractionEvent()

    data object LoadPreview : ExtractionEvent()
    data object SaveConfig : ExtractionEvent()
    data object LaunchExport : ExtractionEvent()
}