package de.franzsw.extractor.domain.model

data class WorkbookPreview (
    val eventsPreview: List<List<String>>,
    val foundWorkers: List<String>
)
