package io.github.vvmaksim.model

import com.github.bhlangonijr.chesslib.move.MoveConversionException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ChessManagerTest {
    @Test
    fun `getMoves should return correct moves v1`() {
        val pgnString = "1. e4 e5"
        val expectedMoves = listOf(Triple(1, "e4", "e5"))
        val moves = ChessManager.getMoves(pgnString)
        assertEquals(expectedMoves, moves)
    }

    @Test
    fun `getMoves should return correct moves v2`() {
        val pgnString = "1. e4 c5 2. Nc3 d6 3. Nf3 Nf6 4. g3"
        val expectedMoves =
            listOf(
                Triple(1, "e4", "c5"),
                Triple(2, "Nc3", "d6"),
                Triple(3, "Nf3", "Nf6"),
                Triple(4, "g3", null),
            )
        val moves = ChessManager.getMoves(pgnString)
        assertEquals(expectedMoves, moves)
    }

    @Test
    fun `getMoves should return empty moves list`() {
        val pgnString = ""
        val expectedMoves = emptyList<Triple<Int, String, String?>>()
        val moves = ChessManager.getMoves(pgnString)
        assertEquals(expectedMoves, moves)
    }

    @Test
    fun `getMoves should throw exception for invalid move`() {
        val pgnString = "1. e4 e9"
        assertThrows<MoveConversionException> {
            ChessManager.getMoves(pgnString)
        }
    }
}
