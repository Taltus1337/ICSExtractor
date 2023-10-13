package de.franzsw.extractor.domain

import de.franzsw.extractor.domain.model.ExtractionConfig
import de.franzsw.extractor.domain.model.EventMeta
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ExportICS {
    operator fun invoke(
        eventMetas: List<EventMeta>,
        config: ExtractionConfig,
        inputFile: File,
        outputFolderPath: String
    ): String {
        val excludedAppointments = mutableListOf<String>()
        val messages = mutableListOf<String>()

        val excelFile = FileInputStream(inputFile)
        val workbook = WorkbookFactory.create(excelFile)
        val sheet = workbook.getSheetAt(config.sheet)

        val dataEndRow = if (config.isAutoSelectLastRow) sheet.lastRowNum else config.dataEndRow
        val dataEndColumn = if (config.isAutoSelectLastColumn) sheet.getRow(1).lastCellNum.toInt() else config.dataEndRow


        var availableWorkers = listOf<String>()

        // add all workers
        for (rowIndex in config.dataStartRow..dataEndRow) {
            val workerName = sheet.getRow(rowIndex)?.getCell(config.workerColumn)?.stringCellValue
            if (!workerName.isNullOrBlank() && workerName !in availableWorkers) {
                availableWorkers = availableWorkers.plus(workerName)
            }
        }

        for (worker in availableWorkers) {
            val shiftCount = mutableMapOf<String, Int>()
            val fileOutputStream = FileOutputStream("$outputFolderPath/${config.month}/${config.year} - $worker.ics")
            fileOutputStream.write(
                """
        BEGIN:VCALENDAR
        VERSION:2.0
        PRODID:-//ICS - Extractor//by Franz Software//DE
        CALSCALE:GREGORIAN
        METHOD:PUBLISH
        """.trimIndent().toByteArray()
            )
            fileOutputStream.write("\n".toByteArray())

            // iterate over all rows
            for (rowIndex in config.dataStartRow..dataEndRow) {
                // only use worker rows
                if (sheet.getRow(rowIndex)?.getCell(config.workerColumn)?.stringCellValue == worker) {
                    // iterate over all cells
                    for (columnIndex in config.dataStartColumn..dataEndColumn) {
                        val cell = sheet.getRow(rowIndex)?.getCell(columnIndex)
                        val cellContent = cell?.stringCellValue?.trim() ?: continue
                        val event = eventMetas.firstOrNull {
                            it.abbreviation.trim().uppercase() == cellContent.uppercase()
                        }

                        if (event == null) {
                            excludedAppointments.add(cellContent)
                            continue
                        }

                        // get day of month from index
                        val dayOfMonth = cell.columnIndex - (config.dataStartColumn)// TODO verify
                        val startDayOfMonthAsString = "${if (dayOfMonth < 10) "0" else ""}$dayOfMonth"

                        // handle overnight events (e.g. skip day)
                        val endDateAsString = if (event.isOvernight) {
                            // check if its last day of month (lastCellNum returns last cell + 1)
                            if (cell.columnIndex == dataEndColumn) { // TODO: verify not +1 needed
                                val nextMonth = ((config.month.toIntOrNull() ?: continue) + 1).let { month ->
                                    "${if (month < 10) "0" else ""}$month"
                                }

                                if (nextMonth == "13") {
                                    "${(config.year.toIntOrNull() ?: continue) + 1}0101"
                                } else {
                                    "${config.year}${nextMonth}01"
                                }
                            } else {
                                "${config.year}${config.month}$startDayOfMonthAsString"
                            }
                        } else {
                            "${config.year}${config.month}$startDayOfMonthAsString"
                        }

                        fileOutputStream.write(
                            """
                        BEGIN:VEVENT
                        SUMMARY:${event.abbreviation}
                        DTSTART:${config.year}${config.month}${startDayOfMonthAsString}T${event.start}00
                        DTEND:${endDateAsString}T${event.end}00
                        END:VEVENT
                    """.trimIndent().toByteArray()
                        )
                        fileOutputStream.write("\n".toByteArray())

                        shiftCount[event.abbreviation] = (shiftCount[event.abbreviation] ?: 0) + 1
                    }
                }
            }
            fileOutputStream.write("END:VCALENDAR".toByteArray())
            messages.add(
                buildString {
                    append("$worker # ")
                    append(shiftCount.keys.forEach { "$it = ${shiftCount[it]} / "})
                }
            )
        }
        return  buildString {
            appendLine("excluded events: ${excludedAppointments.joinToString()}")
            messages.forEach { message ->
                appendLine(message)
            }
        }
    }
}

