package de.franzsw.extractor.domain

import de.franzsw.extractor.data.SharedString
import de.franzsw.extractor.data.toCommonString
import de.franzsw.extractor.domain.model.ExtractionConfig
import de.franzsw.extractor.domain.model.EventMeta
import de.franzsw.extractor.domain.util.fromZeroBased
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ExportICS {
    operator fun invoke(
        eventMetas: List<EventMeta>,
        config: ExtractionConfig,
        inputFile: String,
        outputFolderPath: String
    ): String {
        val excludedAppointments = mutableListOf<String>()
        val messages = mutableListOf<String>()

        val excelFile = FileInputStream(inputFile)
        val workbook = WorkbookFactory.create(excelFile)
        val sheet = workbook.getSheetAt(config.zeroBasedSheet())

        val eventsEndRow = if (config.isAutoSelectLastRow) (sheet.lastRowNum - 1) else config.zeroBasedEventsEndRow()
        val eventsEndColumn = if (config.isAutoSelectLastColumn) (sheet.getRow(1).lastCellNum.toInt() - 1) else config.zeroBasedEventsEndColumn()


        var availableWorkers = listOf<String>()

        // add all workers
        for (rowIndex in config.zeroBasedEventsStartRow()..eventsEndRow) {
            val workerName = sheet.getRow(rowIndex)?.getCell(config.zeroBasedWorkerColumn())?.stringCellValue?.trim()
            if (!workerName.isNullOrBlank() && workerName !in availableWorkers) {
                availableWorkers = availableWorkers.plus(workerName)
            }
        }

        for (worker in availableWorkers) {
            val shiftCount = mutableMapOf<String, Int>()
            val fileOutputStream = FileOutputStream("$outputFolderPath/${config.month}-${config.year} - $worker.ics")
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
            for (rowIndex in config.zeroBasedEventsStartRow()..eventsEndRow) {
                // only use worker rows
                if (sheet.getRow(rowIndex)?.getCell(config.zeroBasedWorkerColumn())?.stringCellValue?.trim() == worker) {
                    // iterate over all cells
                    for (columnIndex in config.zeroBasedEventsStartColumn()..eventsEndColumn) {
                        val cell = sheet.getRow(rowIndex)?.getCell(columnIndex)
                        val cellContent = cell?.stringCellValue?.trim() ?: continue

                        // get event from predefined list
                        val event = eventMetas.firstOrNull {
                            it.abbreviation.trim().uppercase() == cellContent.uppercase()
                        }

                        if (event == null) {
                            cellContent.takeIf { it.isNotBlank() }?.let { excludedAppointments.add(it) }
                            continue
                        }

                        // get day of month from index
                        val dayOfMonth = (cell.columnIndex - (config.zeroBasedEventsStartColumn())).fromZeroBased()
                        val startDayOfMonthAsString = "${if (dayOfMonth < 10) "0" else ""}$dayOfMonth"

                        // handle overnight events (e.g. skip day)
                        val endDateAsString = if (event.isOvernight) {
                            // check if its last day of month
                            if (cell.columnIndex == eventsEndColumn) {
                                val nextMonth = ((config.month.toIntOrNull() ?: continue) + 1).let { month ->
                                    "${if (month < 10) "0" else ""}$month"
                                }

                                if (nextMonth == "13") {
                                    "${(config.year.toIntOrNull() ?: continue) + 1}0101"
                                } else {
                                    "${config.year}${nextMonth}01"
                                }

                            } else {
                                "${config.year}${config.month}${if ((dayOfMonth + 1) < 10) "0" else ""}${dayOfMonth + 1}"
                            }
                        } else {
                            "${config.year}${config.month}$startDayOfMonthAsString"
                        }

                        fileOutputStream.write(
                            """
                        BEGIN:VEVENT
                        SUMMARY:${event.abbreviation}
                        DTSTART;TZID=Europe/Berlin:${config.year}${config.month}${startDayOfMonthAsString}T${event.start}00
                        DTEND;TZID=Europe/Berlin:${endDateAsString}T${event.end}00
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
                    shiftCount.keys.forEach {
                        append("$it = ${shiftCount[it]} / ")
                    }
                }
            )
        }
        return  buildString {
            appendLine("${SharedString.ExcludedEvents.toCommonString()}: ${excludedAppointments.distinct().joinToString()}")
            appendLine("${SharedString.AddedEvents.toCommonString()}:")
            messages.forEach { message ->
                appendLine(message)
            }
        }
    }
}

