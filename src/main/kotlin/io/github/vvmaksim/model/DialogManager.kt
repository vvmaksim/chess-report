package io.github.vvmaksim.model

import io.github.vvmaksim.app.config.PrivateConfig
import java.io.File
import javax.swing.JFileChooser
import javax.swing.UIManager

object DialogManager {
    fun showSelectDirectoryDialog(
        directory: String,
        title: String = "Select Directory",
    ): String? =
        fileDialogManager(
            selectionMode = JFileChooser.DIRECTORIES_ONLY,
            title = title,
            directory = directory,
        )?.selectedFile?.absolutePath

    fun fileDialogManager(
        selectionMode: Int,
        title: String,
        directory: String,
    ): JFileChooser? {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

        val fileChooser =
            JFileChooser().apply {
                fileSelectionMode = selectionMode
                isMultiSelectionEnabled = false
                isFileHidingEnabled = false
                dialogTitle = title
                val initialDir = File(directory)
                currentDirectory =
                    if (initialDir.exists() && initialDir.isDirectory) {
                        initialDir
                    } else {
                        File(PrivateConfig.getDefaultUserDirPath())
                    }
            }

        return if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            fileChooser
        } else {
            null
        }
    }
}
