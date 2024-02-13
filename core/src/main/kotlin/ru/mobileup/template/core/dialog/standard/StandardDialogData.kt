package ru.mobileup.template.core.dialog.standard

import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc

data class StandardDialogData(
    val title: StringDesc,
    val message: StringDesc? = null,
    val confirmButton: DialogButton,
    val dismissButton: DialogButton? = null,
    val dismissableByUser: Boolean = true
) {

    companion object {
        val MOCK = StandardDialogData(
            title = "Title".desc(),
            message = "Message".desc(),
            confirmButton = DialogButton(
                text = "Next".desc(),
                action = {}
            ),
            dismissButton = DialogButton(
                text = "Cancel".desc(),
                action = {}
            )
        )
    }
}

data class DialogButton(
    val text: StringDesc,
    val action: () -> Unit
)