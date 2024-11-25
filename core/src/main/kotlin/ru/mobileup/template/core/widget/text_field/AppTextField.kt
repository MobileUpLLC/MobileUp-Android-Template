package ru.mobileup.template.core.widget.text_field

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.localized
import dev.icerock.moko.resources.desc.desc
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import ru.mobileup.kmm_form_validation.control.InputControl
import ru.mobileup.kmm_form_validation.options.KeyboardOptions
import ru.mobileup.kmm_form_validation.toCompose
import ru.mobileup.template.core.theme.AppTheme
import ru.mobileup.template.core.theme.custom.CustomTheme
import androidx.compose.foundation.text.KeyboardOptions as ComposeKeyboardOptions
import ru.mobileup.kmm_form_validation.options.VisualTransformation as KmmVisualTransformation

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppTextField(
    text: String,
    onTextChange: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    supportingText: String? = null,
    errorText: String? = null,
    headerText: String? = null,
    isError: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    singleLine: Boolean = minLines == 1,
    keyboardOptions: ComposeKeyboardOptions = ComposeKeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardActions: KeyboardActions? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    hasFocus: Boolean = false,
    scrollToItEvent: Flow<Unit> = emptyFlow(),
    moveCursorEvent: Flow<Int> = emptyFlow(),
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    if (hasFocus) {
        SideEffect {
            focusRequester.requestFocus()
        }
    }

    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    LaunchedEffect(key1 = scrollToItEvent) {
        scrollToItEvent.collectLatest {
            bringIntoViewRequester.bringIntoView()
        }
    }

    val currentValue by rememberUpdatedState(text)
    var currentSelection by rememberSaveable(stateSaver = TextRangeSaver) {
        mutableStateOf(TextRange(0))
    }
    var currentComposition by rememberSaveable(stateSaver = NullableTextRangeSaver) {
        mutableStateOf(null)
    }
    val currentTextFieldValue by remember {
        derivedStateOf {
            TextFieldValue(currentValue, currentSelection, currentComposition)
        }
    }
    LaunchedEffect(moveCursorEvent) {
        moveCursorEvent.collectLatest {
            currentSelection = TextRange(it)
        }
    }

    Column(
        modifier = modifier
            .animateContentSize()
            .bringIntoViewRequester(bringIntoViewRequester)
    ) {
        headerText?.let {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = it,
                color = CustomTheme.colors.text.primary,
                style = CustomTheme.typography.title.regular
            )
        }

        TextField(
            modifier = Modifier
                .border(
                    border = AppTextFieldDefaults.borderStroke(isError, hasFocus),
                    shape = AppTextFieldDefaults.shape
                )
                .focusRequester(focusRequester)
                .onFocusChanged { onFocusChange(it.isFocused) },
            value = currentTextFieldValue,
            onValueChange = {
                onTextChange(it.text)
                currentSelection = it.selection
                currentComposition = it.composition
            },
            enabled = isEnabled,
            readOnly = !isEnabled,
            textStyle = AppTextFieldDefaults.textStyle,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions ?: KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            label = label?.let {
                {
                    Text(text = it, style = AppTextFieldDefaults.labelStyle)
                }
            },
            placeholder = placeholder?.let {
                {
                    Text(text = it, style = AppTextFieldDefaults.textStyle)
                }
            },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            shape = AppTextFieldDefaults.shape,
            isError = isError,
            colors = AppTextFieldDefaults.textFieldColors,
        )

        Crossfade(
            targetState = errorText ?: supportingText,
            label = "supporting text animation"
        ) { textToDisplay ->
            if (!textToDisplay.isNullOrEmpty()) {
                Text(
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                    text = textToDisplay,
                    color = if (textToDisplay == errorText) {
                        CustomTheme.colors.text.error
                    } else {
                        CustomTheme.colors.text.primary
                    },
                    style = CustomTheme.typography.caption.regular
                )
            }
        }
    }
}

@Composable
fun AppTextField(
    inputControl: InputControl,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: KmmVisualTransformation? = null,
    keyboardActions: KeyboardActions? = null,
    supportingText: String? = null,
    headerText: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    val hasFocus by inputControl.hasFocus.collectAsState()
    val error by inputControl.error.collectAsState()
    val enabled by inputControl.enabled.collectAsState()
    val text by inputControl.text.collectAsState()

    AppTextField(
        modifier = modifier,
        text = text,
        label = label,
        placeholder = placeholder,
        isError = error != null,
        supportingText = supportingText,
        headerText = headerText,
        errorText = error?.localized(),
        isEnabled = enabled,
        onTextChange = inputControl::onTextChanged,
        onFocusChange = inputControl::onFocusChanged,
        singleLine = minLines == 1,
        keyboardOptions = inputControl.keyboardOptions.toCompose(),
        keyboardActions = keyboardActions,
        visualTransformation = (visualTransformation
            ?: KmmVisualTransformation.None).toCompose(),
        hasFocus = hasFocus,
        scrollToItEvent = inputControl.scrollToItEvent,
        moveCursorEvent = inputControl.moveCursorEvent,
        minLines = minLines,
        maxLines = maxLines,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
    )
}

private val TextRangeSaver = listSaver(
    save = { listOf(it.start, it.end) },
    restore = { TextRange(it[0], it[1]) }
)

private val NullableTextRangeSaver = listSaver<TextRange?, Int>(
    save = { if (it != null) listOf(it.start, it.end) else emptyList() },
    restore = { TextRange(it[0], it[1]) }
)

@Composable
@Preview(showSystemUi = true)
@OptIn(DelicateCoroutinesApi::class)
private fun AppTextFieldPreview() {
    val inputControl = InputControl(GlobalScope)
    val inputControl2 = InputControl(
        coroutineScope = GlobalScope,
        initialText = "Some text",
        keyboardOptions = KeyboardOptions()
    ).apply {
        error.value = "Error".desc()
    }
    val inputControl6 = InputControl(
        coroutineScope = GlobalScope,
        initialText = "Some text",
        keyboardOptions = KeyboardOptions()
    ).apply {
        error.value = "Error".desc()
    }
    val inputControl3 = InputControl(
        coroutineScope = GlobalScope,
        initialText = "Some text",
        keyboardOptions = KeyboardOptions()
    ).apply {
        enabled.value = false
    }
    val inputControl4 = InputControl(GlobalScope)
    val inputControl5 = InputControl(GlobalScope)

    AppTheme {
        Column(
            modifier = Modifier
                .background(CustomTheme.colors.background.screen)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AppTextField(
                modifier = Modifier.fillMaxWidth(),
                inputControl = inputControl2,
                label = "Label",
                placeholder = "Placeholder",
                supportingText = "Footer",
                headerText = "Header",
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null
                    )
                }
            )

            AppTextField(
                modifier = Modifier.fillMaxWidth(),
                inputControl = inputControl,
                label = "Заголовок",
                placeholder = "Текст",
                supportingText = "Supporting"
            )

            AppTextField(
                modifier = Modifier.fillMaxWidth(),
                inputControl = inputControl4,
                label = "Заголовок",
                placeholder = "Текст",
                minLines = 3,
                maxLines = 3,
            )

            AppTextField(
                modifier = Modifier.fillMaxWidth(),
                inputControl = inputControl6,
                label = "Заголовок",
                placeholder = "Текст"
            )

            AppTextField(
                modifier = Modifier.fillMaxWidth(),
                inputControl = inputControl3,
                label = "Заголовок",
                placeholder = "Текст"
            )

            AppTextField(
                modifier = Modifier.fillMaxWidth(),
                inputControl = inputControl5,
                placeholder = "000 000 00 00",
                label = "Phone",
            )
        }
    }
}
