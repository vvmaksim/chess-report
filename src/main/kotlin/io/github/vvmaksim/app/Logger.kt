package io.github.vvmaksim.app

import io.github.oshai.kotlinlogging.KotlinLogging

object Logger {
    private val logger = KotlinLogging.logger("chess-report")

    fun startMessage() = logger.info { "chess-report started!" }

    fun finishMessage() = logger.info { "chess-report shutdown" }

    fun info(message: String) = logger.info { message }

    fun debug(message: String) = logger.debug { message }

    fun warn(message: String) = logger.warn { message }

    fun error(message: String) = logger.error { message }
}
