package io.github.vvmaksim.model

import io.github.vvmaksim.app.config.PrivateConfig
import io.github.vvmaksim.app.config.TextManager
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream

object TableManager {
    fun createTable(
        filePath: String,
        data: MatchData,
    ) {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet()
        createTitle(
            workbook = workbook,
            sheet = sheet,
        )
        createTitleTable(
            workbook = workbook,
            sheet = sheet,
            data = data,
        )
        val firstMatchTotalColumns =
            createMatchMovesTable(
                sheet = sheet,
                workbook = workbook,
                data = data,
                matchNumber = 1,
                startColumnIndex = 1,
                isLastTable = false,
            )
        createMatchMovesTable(
            sheet = sheet,
            workbook = workbook,
            data = data,
            matchNumber = 2,
            startColumnIndex = 1 + firstMatchTotalColumns,
            isLastTable = true,
        )
        try {
            FileOutputStream(filePath).use { outputStream ->
                workbook.write(outputStream)
            }
        } finally {
            workbook.close()
        }
    }

    private fun createMatchMovesTable(
        sheet: XSSFSheet,
        workbook: XSSFWorkbook,
        data: MatchData,
        matchNumber: Int,
        startColumnIndex: Int,
        isLastTable: Boolean,
    ): Int {
        val movesString = if (matchNumber == 1) data.firstMatchMoves else data.secondMatchMoves
        val result = if (matchNumber == 1) data.firstMatchResult else data.secondMatchResult
        val parsedMoves = ChessManager.getMoves(movesString)
        val movesMap = parsedMoves.associate { it.first to (it.second to it.third) }
        val whitePlayer = if (matchNumber == 1) data.firstPlayerName else data.secondPlayerName
        val blackPlayer = if (matchNumber == 1) data.secondPlayerName else data.firstPlayerName
        val movesCount = parsedMoves.size
        val blocksCount = maxOf(2, (movesCount + 31) / 32)
        val totalColumns = blocksCount * 3
        val endColumnIndex = startColumnIndex + totalColumns - 1

        val headerBoldFont =
            workbook.createFont().apply {
                fontName = "Calibri"
                fontHeightInPoints = 11.toShort()
                bold = true
            }

        val headerRegularFont =
            workbook.createFont().apply {
                fontName = "Calibri"
                fontHeightInPoints = 11.toShort()
                bold = false
            }

        val centeredStyle =
            workbook.createCellStyle().apply {
                setFont(headerBoldFont)
                alignment = HorizontalAlignment.CENTER
            }

        val borderedStyle =
            workbook.createCellStyle().apply {
                setFont(headerRegularFont)
                alignment = HorizontalAlignment.CENTER
                borderTop = BorderStyle.THIN
                borderBottom = BorderStyle.THIN
                borderLeft = BorderStyle.THIN
                borderRight = BorderStyle.THIN
            }

        val borderedLabelStyle =
            workbook.createCellStyle().apply {
                setFont(headerBoldFont)
                alignment = HorizontalAlignment.CENTER
                borderTop = BorderStyle.THIN
                borderBottom = BorderStyle.THIN
                borderLeft = BorderStyle.THIN
                borderRight = BorderStyle.THIN
            }

        mergeCells(
            sheet = sheet,
            topLeftCellIndex = 6,
            bottomLeftCellIndex = 6,
            topRightCellIndex = startColumnIndex,
            bottomRightCellIndex = endColumnIndex,
            text = TextManager.Table.getGameNumberTitle(matchNumber),
            style = centeredStyle,
        )
        mergeCells(
            sheet = sheet,
            topLeftCellIndex = 7,
            bottomLeftCellIndex = 7,
            topRightCellIndex = startColumnIndex,
            bottomRightCellIndex = endColumnIndex,
            text = TextManager.Table.getTournamentNameWithDate(data.tournamentName, data.date),
            style = centeredStyle,
        )
        val whitePlayerRow = sheet.getRow(8) ?: sheet.createRow(8)
        whitePlayerRow.createCell(startColumnIndex).apply {
            setCellValue(TextManager.Words.WHITES)
            cellStyle = borderedLabelStyle
        }
        mergeCells(
            sheet = sheet,
            topLeftCellIndex = 8,
            bottomLeftCellIndex = 8,
            topRightCellIndex = startColumnIndex + 1,
            bottomRightCellIndex = endColumnIndex,
            text = whitePlayer,
            style = borderedStyle,
        )
        val blackPlayerRow = sheet.getRow(9) ?: sheet.createRow(9)
        blackPlayerRow.createCell(startColumnIndex).apply {
            setCellValue(TextManager.Words.BLACKS)
            cellStyle = borderedLabelStyle
        }
        mergeCells(
            sheet = sheet,
            topLeftCellIndex = 9,
            bottomLeftCellIndex = 9,
            topRightCellIndex = startColumnIndex + 1,
            bottomRightCellIndex = endColumnIndex,
            text = blackPlayer,
            style = borderedStyle,
        )
        if (isLastTable) {
            val borderFixStyle =
                workbook.createCellStyle().apply {
                    borderLeft = BorderStyle.THIN
                }
            (sheet.getRow(8) ?: sheet.createRow(8)).createCell(endColumnIndex + 1).cellStyle = borderFixStyle
            (sheet.getRow(9) ?: sheet.createRow(9)).createCell(endColumnIndex + 1).cellStyle = borderFixStyle
        }
        val headerFont =
            workbook.createFont().apply {
                bold = false
                fontHeightInPoints = 11.toShort()
            }
        val headerStyleNum =
            workbook.createCellStyle().apply {
                setFont(headerFont)
                alignment = HorizontalAlignment.CENTER
                borderBottom = BorderStyle.THIN
            }
        val headerStyleNumFirst =
            workbook.createCellStyle().apply {
                cloneStyleFrom(headerStyleNum)
                borderLeft = BorderStyle.THIN
            }
        val headerStyleMoves =
            workbook.createCellStyle().apply {
                setFont(headerFont)
                alignment = HorizontalAlignment.CENTER
                borderLeft = BorderStyle.THIN
                borderRight = BorderStyle.THIN
                borderTop = BorderStyle.THIN
                borderBottom = BorderStyle.THIN
            }
        val headerRow = sheet.getRow(12) ?: sheet.createRow(12)
        for (block in 0 until blocksCount) {
            val col1 = startColumnIndex + block * 3
            headerRow.createCell(col1).apply {
                setCellValue("№")
                cellStyle = if (block == 0 && startColumnIndex > 1) headerStyleNumFirst else headerStyleNum
            }
            headerRow.createCell(col1 + 1).apply {
                setCellValue(TextManager.Words.WHITES)
                cellStyle = headerStyleMoves
            }
            headerRow.createCell(col1 + 2).apply {
                setCellValue(TextManager.Words.BLACKS)
                cellStyle = headerStyleMoves
            }
        }
        val moveNumberFont =
            workbook.createFont().apply {
                bold = true
                fontHeightInPoints = 10.toShort()
                setColor(XSSFColor(PrivateConfig.SECONDARY_TABLE_COLOR, null))
            }
        val moveDataFont =
            workbook.createFont().apply {
                fontHeightInPoints = 11.toShort()
            }
        val moveDataStyle =
            workbook.createCellStyle().apply {
                setFont(moveDataFont)
                alignment = HorizontalAlignment.LEFT
                borderLeft = BorderStyle.THIN
                borderRight = BorderStyle.THIN
                borderTop = BorderStyle.THIN
                borderBottom = BorderStyle.THIN
            }
        val numDataStyle =
            workbook.createCellStyle().apply {
                setFont(moveNumberFont)
                alignment = HorizontalAlignment.CENTER
                dataFormat = workbook.createDataFormat().getFormat("@")
            }
        val numDataStyleFirstBlock =
            workbook.createCellStyle().apply {
                cloneStyleFrom(numDataStyle)
                borderLeft = BorderStyle.THIN
            }

        val lastBlockIndex = blocksCount - 1
        val baseResultRowForBlock = 13 + 28 // This is row index 41.
        val defaultResultStartMove = (blocksCount * 32) - 3
        val shift =
            if (movesCount >= defaultResultStartMove) {
                movesCount - (defaultResultStartMove - 1)
            } else {
                0
            }
        val resultRowIndex = baseResultRowForBlock + shift

        // Main loop to create grid and fill data
        for (block in 0 until blocksCount) {
            for (i in 0 until 32) {
                val rowNum = 13 + i
                val row = sheet.getRow(rowNum) ?: sheet.createRow(rowNum)
                val moveNumber = block * 32 + i + 1

                val numCol = startColumnIndex + block * 3
                val whiteCol = numCol + 1
                val blackCol = numCol + 2

                val numCell = row.createCell(numCol)
                val whiteCell = row.createCell(whiteCol)
                val blackCell = row.createCell(blackCol)

                numCell.cellStyle = if (block == 0) numDataStyleFirstBlock else numDataStyle
                whiteCell.cellStyle = moveDataStyle
                blackCell.cellStyle = moveDataStyle

                val isResultArea = (block == lastBlockIndex) && (rowNum >= resultRowIndex)

                if (isResultArea) {
                    numCell.setCellValue("")
                    whiteCell.setCellValue("")
                    blackCell.setCellValue("")
                } else {
                    numCell.setCellValue(moveNumber.toString())
                    val movePair = movesMap[moveNumber]
                    whiteCell.setCellValue(movePair?.first ?: "/")
                    blackCell.setCellValue(movePair?.second ?: "/")
                }
            }
        }
        val resultStartColumn = startColumnIndex + lastBlockIndex * 3
        val whiteScore: String
        val blackScore: String
        when (result) {
            GameResult.FIRST_PLAYER_IS_WINNER -> if (matchNumber == 1) "1" to "0" else "0" to "1"
            GameResult.SECOND_PLAYER_IS_WINNER -> if (matchNumber == 1) "0" to "1" else "1" to "0"
            GameResult.DRAW -> "0.5" to "0.5"
            GameResult.UNKNOWN -> "*" to "*"
        }.let {
            whiteScore = it.first
            blackScore = it.second
        }

        val resultHeaderStyle =
            workbook.createCellStyle().apply {
                setFont(headerFont)
                alignment = HorizontalAlignment.LEFT
                borderLeft = BorderStyle.THIN
                borderRight = BorderStyle.THIN
                borderTop = BorderStyle.THIN
                borderBottom = BorderStyle.THIN
            }

        val resultItogStyle =
            workbook.createCellStyle().apply {
                setFont(headerFont) // Non-bold
                alignment = HorizontalAlignment.LEFT
                borderLeft = BorderStyle.THIN
                borderRight = BorderStyle.THIN
            }

        val resultScoreStyle =
            workbook.createCellStyle().apply {
                setFont(moveDataFont)
                alignment = HorizontalAlignment.RIGHT
                dataFormat = workbook.createDataFormat().getFormat("@")
                borderLeft = BorderStyle.THIN
                borderRight = BorderStyle.THIN
                borderTop = BorderStyle.THIN
                borderBottom = BorderStyle.THIN
            }

        val resultHeaderRow = sheet.getRow(resultRowIndex) ?: sheet.createRow(resultRowIndex)
        resultHeaderRow.getCell(resultStartColumn) ?: resultHeaderRow.createCell(resultStartColumn)
        resultHeaderRow.getCell(resultStartColumn + 1) ?: resultHeaderRow.createCell(resultStartColumn + 1)
        resultHeaderRow.getCell(resultStartColumn + 2) ?: resultHeaderRow.createCell(resultStartColumn + 2)
        resultHeaderRow.getCell(resultStartColumn).apply {
            setCellValue(TextManager.Words.RESULT)
            cellStyle = resultItogStyle
        }
        resultHeaderRow.getCell(resultStartColumn + 1).apply {
            setCellValue(TextManager.Words.WHITES)
            cellStyle = resultHeaderStyle
        }
        resultHeaderRow.getCell(resultStartColumn + 2).apply {
            setCellValue(TextManager.Words.BLACKS)
            cellStyle = resultHeaderStyle
        }

        val resultScoreRow = sheet.getRow(resultRowIndex + 1) ?: sheet.createRow(resultRowIndex + 1)
        resultScoreRow.getCell(resultStartColumn) ?: resultScoreRow.createCell(resultStartColumn)
        resultScoreRow.getCell(resultStartColumn + 1) ?: resultScoreRow.createCell(resultStartColumn + 1)
        resultScoreRow.getCell(resultStartColumn + 2) ?: resultScoreRow.createCell(resultStartColumn + 2)
        resultScoreRow.getCell(resultStartColumn).apply {
            cellStyle = resultItogStyle
        }
        resultScoreRow.getCell(resultStartColumn + 1).apply {
            setCellValue(whiteScore)
            cellStyle = resultScoreStyle
        }
        resultScoreRow.getCell(resultStartColumn + 2).apply {
            setCellValue(blackScore)
            cellStyle = resultScoreStyle
        }

        val bottomRow = sheet.getRow(13 + 31)
        for (col in startColumnIndex until startColumnIndex + totalColumns) {
            val cell = bottomRow.getCell(col)
            val style =
                workbook.createCellStyle().apply {
                    cloneStyleFrom(cell.cellStyle)
                    borderBottom = BorderStyle.THIN
                }
            cell.cellStyle = style
        }

        return totalColumns
    }

    private fun mergeCells(
        sheet: XSSFSheet,
        topLeftCellIndex: Int,
        bottomLeftCellIndex: Int,
        topRightCellIndex: Int,
        bottomRightCellIndex: Int,
        text: String,
        style: XSSFCellStyle,
    ) {
        val region = CellRangeAddress(topLeftCellIndex, bottomLeftCellIndex, topRightCellIndex, bottomRightCellIndex)
        sheet.addMergedRegion(region)
        val row = sheet.getRow(topLeftCellIndex) ?: sheet.createRow(topLeftCellIndex)
        row.getCell(topRightCellIndex) ?: row.createCell(topRightCellIndex).apply {
            setCellValue(text)
            cellStyle = style
        }
    }

    private fun createTitle(
        workbook: XSSFWorkbook,
        sheet: XSSFSheet,
    ) {
        val font =
            workbook.createFont().apply {
                bold = true
                fontHeightInPoints = 14.toShort()
            }
        val style =
            workbook.createCellStyle().apply {
                setFont(font)
                wrapText = true
                alignment = HorizontalAlignment.CENTER
                verticalAlignment = VerticalAlignment.CENTER
            }
        mergeCells(
            sheet = sheet,
            topLeftCellIndex = 0,
            bottomLeftCellIndex = 0,
            topRightCellIndex = 1,
            bottomRightCellIndex = 15,
            text = TextManager.Table.MAIN_TITLE,
            style = style,
        )
        sheet.getRow(0).apply {
            heightInPoints = 40f
        }
    }

    private fun createTitleTable(
        workbook: XSSFWorkbook,
        sheet: XSSFSheet,
        data: MatchData,
    ) {
        val font =
            workbook.createFont().apply {
                bold = true
                fontHeightInPoints = 11.toShort()
            }
        val style =
            workbook.createCellStyle().apply {
                setFont(font)
                wrapText = true
                borderTop = BorderStyle.MEDIUM
                borderLeft = BorderStyle.THIN
                borderRight = BorderStyle.THIN
                borderBottom = BorderStyle.MEDIUM
                alignment = HorizontalAlignment.CENTER
                verticalAlignment = VerticalAlignment.CENTER
            }

        val row1 =
            sheet.getRow(2) ?: sheet.createRow(2).apply {
                heightInPoints = 40f
            }
        (row1.getCell(1) ?: row1.createCell(1)).apply {
            setCellValue(TextManager.Table.STUDENT_ID_CARD)
            cellStyle = style
        }

        mergeCells(
            sheet = sheet,
            topLeftCellIndex = 2,
            bottomLeftCellIndex = 2,
            topRightCellIndex = 2,
            bottomRightCellIndex = 5,
            text = TextManager.Table.FULL_NAME,
            style = style,
        )

        (row1.getCell(6) ?: row1.createCell(6)).apply {
            setCellValue(TextManager.Words.GROUP)
            cellStyle = style
        }

        mergeCells(
            sheet = sheet,
            topLeftCellIndex = 2,
            bottomLeftCellIndex = 2,
            topRightCellIndex = 7,
            bottomRightCellIndex = 9,
            text = TextManager.Table.SPORT_DEPARTMENT,
            style = style,
        )

        mergeCells(
            sheet = sheet,
            topLeftCellIndex = 2,
            bottomLeftCellIndex = 2,
            topRightCellIndex = 10,
            bottomRightCellIndex = 12,
            text = TextManager.Words.TEACHER,
            style = style,
        )

        (row1.getCell(12) ?: row1.createCell(12)).apply {
            cellStyle = style
        }

        val row2 =
            sheet.getRow(3) ?: sheet.createRow(3).apply {
                heightInPoints = 40f
            }

        (row2.getCell(1) ?: row2.createCell(1)).apply {
            setCellValue(data.studentIDNumber.toString())
            cellStyle = style
        }

        mergeCells(
            sheet = sheet,
            topLeftCellIndex = 3,
            bottomLeftCellIndex = 3,
            topRightCellIndex = 2,
            bottomRightCellIndex = 5,
            text = data.reporterName,
            style = style,
        )

        (row2.getCell(6) ?: row2.createCell(6)).apply {
            setCellValue(data.studentGroupNumber.toString())
            cellStyle = style
        }

        mergeCells(
            sheet = sheet,
            topLeftCellIndex = 3,
            bottomLeftCellIndex = 3,
            topRightCellIndex = 7,
            bottomRightCellIndex = 9,
            text = TextManager.Words.CHESS,
            style = style,
        )

        mergeCells(
            sheet = sheet,
            topLeftCellIndex = 3,
            bottomLeftCellIndex = 3,
            topRightCellIndex = 10,
            bottomRightCellIndex = 12,
            text = data.teacherName,
            style = style,
        )

        (row2.getCell(12) ?: row2.createCell(12)).apply {
            cellStyle = style
        }

        val templateFont =
            workbook.createFont().apply {
                bold = true
                setColor(XSSFColor(PrivateConfig.SECONDARY_TABLE_COLOR, null))
                fontHeightInPoints = 10.toShort()
            }

        val templateStyle =
            workbook.createCellStyle().apply {
                setFont(templateFont)
                wrapText = true
                alignment = HorizontalAlignment.CENTER
                verticalAlignment = VerticalAlignment.CENTER
            }

        mergeCells(
            sheet = sheet,
            topLeftCellIndex = 2,
            bottomLeftCellIndex = 3,
            topRightCellIndex = 13,
            bottomRightCellIndex = 15,
            text = TextManager.Table.FILE_NAME_FORMAT,
            style = templateStyle,
        )
    }
}
