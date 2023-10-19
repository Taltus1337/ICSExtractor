package de.franzsw.extractor.domain

import de.franzsw.extractor.domain.model.ExtractionConfig
import de.franzsw.extractor.domain.model.WorkbookPreview
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.FileInputStream

class GetWorkbookPreview {
    operator fun invoke(config: ExtractionConfig, filePath: String): WorkbookPreview {
        val excelFile = FileInputStream(filePath)
        val workbook = WorkbookFactory.create(excelFile)
        val sheet = workbook.getSheetAt(config.zeroBasedSheet())

        val workers = mutableListOf<String>()

        val eventsEndRow = if (config.isAutoSelectLastRow) (sheet.lastRowNum - 1) else config.zeroBasedEventsEndRow()
        val eventsEndColumn = if (config.isAutoSelectLastColumn) (sheet.getRow(1).lastCellNum.toInt() - 1) else config.zeroBasedEventsEndColumn()

        val events = listOf(
            // Data Start Row
            listOf(
                sheet.getRow(config.zeroBasedEventsStartRow())?.getCell(config.zeroBasedEventsStartColumn())?.stringCellValue.orEmpty(),
                sheet.getRow(config.zeroBasedEventsStartRow())?.getCell(config.zeroBasedEventsStartColumn() + 1)?.stringCellValue.orEmpty(),

                "< ... >",

                sheet.getRow(config.zeroBasedEventsStartRow())?.getCell(eventsEndColumn - 1)?.stringCellValue.orEmpty(),
                sheet.getRow(config.zeroBasedEventsStartRow())?.getCell(eventsEndColumn)?.stringCellValue.orEmpty(),
            ),
            // Data Start Row + 1
            listOf(
                sheet.getRow(config.zeroBasedEventsStartRow() + 1)?.getCell(config.zeroBasedEventsStartColumn())?.stringCellValue.orEmpty(),
                sheet.getRow(config.zeroBasedEventsStartRow() + 1)?.getCell(config.zeroBasedEventsStartColumn() + 1)?.stringCellValue.orEmpty(),

                "< ... >",

                sheet.getRow(config.zeroBasedEventsStartRow() + 1)?.getCell(eventsEndColumn - 1)?.stringCellValue.orEmpty(),
                sheet.getRow(config.zeroBasedEventsStartRow() + 1)?.getCell(eventsEndColumn)?.stringCellValue.orEmpty(),
            ),


            // Data End Row -1
            listOf(
                sheet.getRow(eventsEndRow - 1)?.getCell(config.zeroBasedEventsStartColumn())?.stringCellValue.orEmpty(),
                sheet.getRow(eventsEndRow - 1)?.getCell(config.zeroBasedEventsStartColumn() + 1)?.stringCellValue.orEmpty(),

                "< ... >",

                sheet.getRow(eventsEndRow - 1)?.getCell(eventsEndColumn - 1)?.stringCellValue.orEmpty(),
                sheet.getRow(eventsEndRow - 1)?.getCell(eventsEndColumn)?.stringCellValue.orEmpty(),
            ),
            // Data End Row
            listOf(
                sheet.getRow(eventsEndRow)?.getCell(config.zeroBasedEventsStartColumn())?.stringCellValue.orEmpty(),
                sheet.getRow(eventsEndRow)?.getCell(config.zeroBasedEventsStartColumn() + 1)?.stringCellValue.orEmpty(),

                "< ... >",

                sheet.getRow(eventsEndRow)?.getCell(eventsEndColumn - 1)?.stringCellValue.orEmpty(),
                sheet.getRow(eventsEndRow)?.getCell(eventsEndColumn)?.stringCellValue.orEmpty(),
            )
        )

        for (row in config.zeroBasedEventsStartRow()..config.zeroBasedEventsEndRow()) {
            workers.add(sheet.getRow(row)?.getCell(config.zeroBasedWorkerColumn())?.stringCellValue?.trim().orEmpty())
        }

        return WorkbookPreview(
            eventsPreview = events,
            foundWorkers = workers.distinct()
        )
    }
}