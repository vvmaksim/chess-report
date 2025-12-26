package io.github.vvmaksim.model

import com.github.bhlangonijr.chesslib.move.MoveList
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import java.net.URI

class PgnManager {
    private val client = HttpClient(CIO)

    fun close() {
        client.close()
    }

    suspend fun getPgnFromUrl(url: String): Result<GameData> {
        val fullPgnResult =
            try {
                val host = URI(url).host.lowercase().replace("www.", "")
                when (host) {
                    "lichess.org" -> fetchLichessPgn(url)
                    else -> Result.failure(IllegalArgumentException("Unsupported URL: $url"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }

        return fullPgnResult.map { parsePgn(it) }
    }

    private fun parsePgn(fullPgn: String): GameData {
        val lines = fullPgn.lines()
        val movesStartIndex = lines.indexOfFirst { !it.startsWith("[") && it.isNotBlank() }
        if (movesStartIndex == -1) return GameData("", GameResult.UNKNOWN)

        val moveTextBlock = lines.subList(movesStartIndex, lines.size).joinToString("\n").trim()

        val gameResults = listOf("1-0", "0-1", "1/2-1/2", "*")
        var gameResult = ""
        var movesWithoutResult = moveTextBlock
        for (res in gameResults) {
            if (moveTextBlock.endsWith(res)) {
                gameResult = res
                movesWithoutResult = moveTextBlock.removeSuffix(res).trim()
                break
            }
        }

        val cleanedMoves = movesWithoutResult.replace(Regex("""\s*\{[^}]*\}\s*"""), " ")

        val moveList = MoveList()
        moveList.loadFromSan(cleanedMoves)
        val sanArray = moveList.toSanArray()
        val movesBuilder = StringBuilder()
        for (i in sanArray.indices step 2) {
            val moveNumber = i / 2 + 1
            movesBuilder.append("$moveNumber. ${sanArray[i]}")
            if (i + 1 < sanArray.size) {
                movesBuilder.append(" ${sanArray[i + 1]}")
            }
            if (i + 2 < sanArray.size) { // Add space if it's not the last pair
                movesBuilder.append(" ")
            }
        }

        return GameData(
            moves = movesBuilder.toString(),
            result = parseGameResultAsStringToGameResult(gameResult),
        )
    }

    private fun parseGameResultAsStringToGameResult(resultAsString: String): GameResult =
        when (resultAsString) {
            "1-0" -> GameResult.FIRST_PLAYER_IS_WINNER
            "0-1" -> GameResult.SECOND_PLAYER_IS_WINNER
            "1/2-1/2" -> GameResult.DRAW
            else -> GameResult.UNKNOWN
        }

    private suspend fun fetchLichessPgn(url: String): Result<String> =
        try {
            val path = URI(url).path
            val gameId = path.split('/').firstOrNull { it.isNotBlank() }?.take(8)

            if (gameId == null || gameId.length < 8) {
                Result.failure(IllegalArgumentException("Invalid Lichess URL: could not extract a valid 8-character game ID from '$url'"))
            } else {
                val pgnUrl = "https://lichess.org/game/export/$gameId"
                val pgn = client.get(pgnUrl).bodyAsText()

                if (pgn.trim().startsWith("<!DOCTYPE html", ignoreCase = true) || pgn.trim().startsWith("Not Found")) {
                    Result.failure(IllegalArgumentException("Game not found on Lichess for ID: $gameId"))
                } else {
                    Result.success(pgn)
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
}
