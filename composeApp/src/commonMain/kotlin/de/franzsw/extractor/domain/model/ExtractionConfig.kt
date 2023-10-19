package de.franzsw.extractor.domain.model

import kotlinx.serialization.Serializable

/**
 * @param year 4-Digits
 * @param month 2-Digits
 * @param sheet sheet / table
 * @param workerColumn column with worker names (used to separate and for naming of .ics output file)
 * @param eventsStartRow first row with appointments for workers
 * @param eventsEndRow last row with appointments (only used if [isAutoSelectLastRow] = false)
 * @param eventsStartColumn first column with appointments for workers
 * @param eventsEndColumn last column with appointments (only used if [isAutoSelectLastColumn] = false)
 */
@Serializable
data class ExtractionConfig(
    val year: String,
    val month: String,
    val sheet: Int,
    val workerColumn: Int,
    val eventsStartRow: Int,
    val eventsEndRow: Int,
    val eventsStartColumn: Int,
    val eventsEndColumn: Int,
    val isAutoSelectLastColumn: Boolean,
    val isAutoSelectLastRow: Boolean,
) {
    fun zeroBasedSheet() = sheet - 1
    fun zeroBasedWorkerColumn() = workerColumn - 1
    fun zeroBasedEventsStartRow() = eventsStartRow - 1
    fun zeroBasedEventsEndRow() = eventsEndRow - 1
    fun zeroBasedEventsStartColumn() = eventsStartColumn - 1
    fun zeroBasedEventsEndColumn() = eventsEndColumn - 1
}

fun getDefaultExtractionConfig() =
    ExtractionConfig(
        year = "2023",
        month = "09",
        sheet = 1,
        workerColumn = 2,
        eventsStartRow = 4,
        eventsEndRow = 30,
        eventsStartColumn = 4,
        eventsEndColumn = 32,
        isAutoSelectLastColumn = false,
        isAutoSelectLastRow = false
    )