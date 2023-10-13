package de.franzsw.extractor.domain

import de.franzsw.extractor.domain.model.ExtractionConfig
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.File
import java.io.FileInputStream

class WorkbookPreview {
    operator fun invoke(config: ExtractionConfig, file: File): List<List<String>> {
        val excelFile = FileInputStream(file)
        val workbook = WorkbookFactory.create(excelFile)
        val sheet = workbook.getSheetAt(config.sheet)

        val dataEndRow = if (config.isAutoSelectLastRow) sheet.lastRowNum else config.dataEndRow
        val dataEndColumn = if (config.isAutoSelectLastColumn) sheet.getRow(1).lastCellNum.toInt() else config.dataEndRow

        return listOf(
            // Data Start Row
            listOf(
                sheet.getRow(config.dataStartRow)?.getCell(config.dataStartColumn)?.stringCellValue.orEmpty(),
                sheet.getRow(config.dataStartRow)?.getCell(config.dataStartColumn + 1)?.stringCellValue.orEmpty(),

                sheet.getRow(config.dataStartRow)?.getCell(dataEndColumn - 1)?.stringCellValue.orEmpty(),
                sheet.getRow(config.dataStartRow)?.getCell(dataEndColumn)?.stringCellValue.orEmpty(),
            ),
            // Data Start Row + 1
            listOf(
                sheet.getRow(config.dataStartRow + 1)?.getCell(config.dataStartColumn)?.stringCellValue.orEmpty(),
                sheet.getRow(config.dataStartRow + 1)?.getCell(config.dataStartColumn + 1)?.stringCellValue.orEmpty(),

                sheet.getRow(config.dataStartRow + 1)?.getCell(dataEndColumn - 1)?.stringCellValue.orEmpty(),
                sheet.getRow(config.dataStartRow + 1)?.getCell(dataEndColumn)?.stringCellValue.orEmpty(),
            ),


            // Data End Row -1
            listOf(
                sheet.getRow(dataEndRow - 1).getCell(config.dataStartColumn)?.stringCellValue.orEmpty(),
                sheet.getRow(dataEndRow - 1)?.getCell(config.dataStartColumn + 1)?.stringCellValue.orEmpty(),

                sheet.getRow(dataEndRow - 1)?.getCell(dataEndColumn - 1)?.stringCellValue.orEmpty(),
                sheet.getRow(dataEndRow - 1)?.getCell(dataEndColumn)?.stringCellValue.orEmpty(),
            ),
            // Data End Row
            listOf(
                sheet.getRow(dataEndRow - 1).getCell(config.dataStartColumn)?.stringCellValue.orEmpty(),
                sheet.getRow(dataEndRow - 1)?.getCell(config.dataStartColumn + 1)?.stringCellValue.orEmpty(),

                sheet.getRow(dataEndRow - 1)?.getCell(dataEndColumn - 1)?.stringCellValue.orEmpty(),
                sheet.getRow(dataEndRow - 1)?.getCell(dataEndColumn)?.stringCellValue.orEmpty(),
            )
        )

    }
}