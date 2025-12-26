package io.github.vvmaksim.app.config

import java.nio.file.Path

object TextManager {
    object UI {
        const val MATCH_INFO_TITLE = "Заполните данные для отчёта:"
        const val FIRST_PLAYER_LABEL = "Игрок 1"
        const val SECOND_PLAYER_LABEL = "Игрок 2"
        const val FULL_NAME_PLACEHOLDER = "ФИО"
        const val REPORTER_LABEL = "Репортёр"
        const val TEACHER_LABEL = "Преподаватель"
        const val TOURNAMENT_NAME_LABEL = "Название турнира"
        const val TOURNAMENT_NAME_PLACEHOLDER = "Шахматный турнир №1"
        const val MATCH_DATE_LABEL = "Дата матча"
        const val MATCH_DATE_PLACEHOLDER = "ДД.ММ.ГГГГ"
        const val STUDENT_ID_NUMBER_LABEL = "Номер студенческого"
        const val STUDENT_ID_NUMBER_PLACEHOLDER = "000000"
        const val STUDENT_GROUP_NUMBER_LABEL = "Номер группы"
        const val STUDENT_GROUP_NUMBER_PLACEHOLDER = "0000"
        const val FIRST_GAME_TITLE = "Партия 1:"
        const val SECOND_GAME_TITLE = "Партия 2:"
        const val WRITE_MOVES_FROM_LINK = "Записать ходы партии по ссылке"
        const val MATCH_MOVES_LABEL_WITH_LINK = "Вставьте ссылку на партию с Lichess.org"
        const val MATCH_MOVES_LABEL_WITH_PGN = "Ходы в формате PGN"
        const val MATCH_MOVES_PLACEHOLDER_WITH_LINK = "Например, https://lichess.org/CgEgzuVH"
        const val MATCH_MOVES_PLACEHOLDER_WITH_PGN = "Метаинформация и различные скобки недопустимы. Формат записи:1.e4 e5 2.Nf3 Nc6 ..."
        const val SELECT_DIRECTORY_PATH_FOR_SAVE_REPORT = "Выберите директорию, в которую будет сохранён отчёт"
        const val GENERATE_REPORT_BUTTON_TEXT = "Сгенерировать отчёт"
        const val OPEN_DIRECTORY_BUTTON_TEXT = "Открыть папку с отчётом"
    }

    object Table {
        const val MAIN_TITLE =
            "Отчет о результатах самостоятельной работы обучающегося по дисциплинам \n" +
                " \"Физическая культура\" или \"Элективные курсы по физической культуре и спорту\""
        const val STUDENT_ID_CARD = "Студ.\nбилет: "
        const val FULL_NAME = "ФИО"
        const val SPORT_DEPARTMENT = "Спортивное отделение"
        const val FILE_NAME_FORMAT =
            "Формат имени файла с отчетом: Отчет.ТК.ФВиС.999901.2020-04-12, где ТК – текущий контроль, " +
                "ФВиС – кафедра физического воспитания и спорта, 999901 – номер студ. билета, " +
                "2020-04-12 – дата отправки отчета."

        fun getTableName(
            studentIDNumber: Int,
            formattedDate: String,
        ) = "Отчёт.ТК.ФВиС.$studentIDNumber.$formattedDate.xlsx"

        fun getGameNumberTitle(matchNumber: Int) = "Шахматная партия №$matchNumber"

        fun getTournamentNameWithDate(
            tournamentName: String,
            date: String,
        ) = "$tournamentName $date"
    }

    object Words {
        const val WINNER = "Победитель"
        const val DRAW = "Ничья"
        const val WHITES = "Белые"
        const val BLACKS = "Чёрные"
        const val RESULT = "Итог:"
        const val GROUP = "Группа"
        const val TEACHER = "Преподаватель"
        const val CHESS = "Шахматы"
    }

    object Errors {
        const val UNKNOWN_ERROR_MESSAGE = "Что-то пошло не так=("

        fun getOpenDirectoryErrorMessage(
            errorPath: Path,
            exceptionAsString: String,
        ) = "Не удалось открыть директорию:$errorPath. Ex:$exceptionAsString"

        fun getIncorrectNotationInGameErrorMessage(
            gameNumber: Int,
            exceptionAsString: String?,
        ) = "Некорректная нотация в Партии $gameNumber: $exceptionAsString"

        fun getErrorInGameErrorMessage(
            gameNumber: Int,
            exceptionAsString: String?,
        ) = "Ошибка в Партии $gameNumber: $exceptionAsString"
    }
}
