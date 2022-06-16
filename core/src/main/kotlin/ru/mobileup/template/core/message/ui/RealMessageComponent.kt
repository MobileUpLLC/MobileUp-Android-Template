package ru.mobileup.template.core.message.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.mobileup.template.core.message.data.MessageService
import ru.mobileup.template.core.message.domain.MessageData
import ru.mobileup.template.core.utils.componentCoroutineScope

class RealMessageComponent(
    componentContext: ComponentContext,
    private val messageService: MessageService,
) : ComponentContext by componentContext, MessageComponent {

    companion object {
        private const val SHOW_TIME = 4000L
    }

    private val coroutineScope = componentCoroutineScope()

    override var visibleMessageData by mutableStateOf<MessageData?>(null)
        private set

    private var autoDismissJob: Job? = null

    init {
        lifecycle.doOnCreate(::collectMessages)
    }

    override fun onActionClick() {
        autoDismissJob?.cancel()
        visibleMessageData?.action?.invoke()
        visibleMessageData = null
    }

    private fun collectMessages() {
        coroutineScope.launch {
            messageService.messageFlow.collect { messageData ->
                showMessage(messageData)
            }
        }
    }

    private fun showMessage(messageData: MessageData) {
        autoDismissJob?.cancel()
        visibleMessageData = messageData
        autoDismissJob = coroutineScope.launch {
            delay(SHOW_TIME)
            visibleMessageData = null
        }
    }
}