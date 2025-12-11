package io.github.vvmaksim.model

import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.move.MoveList
import io.github.vvmaksim.app.Logger

object ChessManager {
    fun getMoves(pgnString: String): List<Triple<Int, String, String?>> {
        if (pgnString.isBlank()) {
            return emptyList()
        }
        try {
            val moveList = MoveList()
            moveList.loadFromSan(pgnString)

            val board = Board()
            for (move in moveList) {
                board.doMove(move)
            }
            val san = moveList.toSanArray().toList()
            val result = mutableListOf<Triple<Int, String, String?>>()

            for (i in san.indices step 2) {
                val moveNumber = i / 2 + 1
                val white = san[i]
                val black = if (i + 1 < san.size) san[i + 1] else null
                result.add(Triple(moveNumber, white, black))
            }
            return result
        } catch (ex: Exception) {
            Logger.error("Ошибка парсинга PGN '$pgnString': $ex")
            return emptyList()
        }
    }
}
