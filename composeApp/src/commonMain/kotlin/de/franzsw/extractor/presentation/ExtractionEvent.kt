package de.franzsw.extractor.presentation

import de.franzsw.extractor.domain.model.ExtractionConfig
import de.franzsw.extractor.domain.model.EventMeta
import java.io.File

sealed class ExtractionEvent {
    data object AddOriginAppointment : ExtractionEvent()
    data class ChangeOriginAppointment(val index: Int, val eventMeta: EventMeta) : ExtractionEvent()
    data class DeleteOriginAppointment(val index: Int) : ExtractionEvent()

    data class ChangeExtractionConfig(val extractionConfig: ExtractionConfig) : ExtractionEvent()

    data object LaunchInputFilePicker : ExtractionEvent()
    data class  SelectInputFile(val file: File) : ExtractionEvent()
    data object LaunchOutputFolderPicker : ExtractionEvent()
    data class SelectOutputFolder(val path: String) : ExtractionEvent()

    data object SaveConfig : ExtractionEvent()
    data object LaunchExport : ExtractionEvent()
}