package de.franzsw.extractor.domain.model

import kotlinx.serialization.Serializable

/**
 * @param year 4-Digits
 * @param month 2-Digits
 * @param sheet 0-based Index of the sheet / table
 * @param workerColumn 0-based Index of column with worker names (used to separate and for naming of .ics output file)
 * @param dataStartRow 0-based Index of first row with appointments for workers
 * @param dataEndRow 0-based Index of last row with appointments (only used if [isAutoSelectLastRow] = false)
 * @param dataStartColumn 0-based Index of first column with appointments for workers
 * @param dataEndColumn 0-based Index of last column with appointments (only used if [isAutoSelectLastColumn] = false)
 */
@Serializable
data class ExtractionConfig(
    val year: String,
    val month: String,
    val sheet: Int,
    val workerColumn: Int,
    val dataStartRow: Int,
    val dataEndRow: Int,
    val dataStartColumn: Int,
    val dataEndColumn: Int,
    val isAutoSelectLastColumn: Boolean,
    val isAutoSelectLastRow: Boolean,
)

fun getDefaultExtractionConfig() =
    ExtractionConfig(
        year = "2023",
        month = "09",
        sheet = 0,
        workerColumn = 2,
        dataStartRow = 4,
        dataEndRow = 30,
        dataStartColumn = 4,
        dataEndColumn = 32,
        isAutoSelectLastColumn = true,
        isAutoSelectLastRow = true
    )