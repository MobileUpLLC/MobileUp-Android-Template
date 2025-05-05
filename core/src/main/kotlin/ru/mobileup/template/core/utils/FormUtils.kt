package ru.mobileup.template.core.utils

import com.arkivanov.decompose.ComponentContext
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.mobileup.kmm_form_validation.control.CheckControl
import ru.mobileup.kmm_form_validation.control.InputControl
import ru.mobileup.kmm_form_validation.control.PickerControl
import ru.mobileup.kmm_form_validation.options.KeyboardOptions
import ru.mobileup.kmm_form_validation.options.TextTransformation
import ru.mobileup.kmm_form_validation.options.VisualTransformation
import ru.mobileup.kmm_form_validation.validation.form.FormValidator
import ru.mobileup.kmm_form_validation.validation.form.FormValidatorBuilder
import ru.mobileup.kmm_form_validation.validation.form.formValidator

fun ComponentContext.InputControl(
    initialText: String = "",
    singleLine: Boolean = true,
    maxLength: Int = Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    textTransformation: TextTransformation? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
): InputControl = InputControl(
    componentScope,
    initialText,
    singleLine,
    maxLength,
    keyboardOptions,
    textTransformation,
    visualTransformation
)

@OptIn(DelicateCoroutinesApi::class)
fun fakeInputControl(initialText: String = "") = InputControl(GlobalScope, initialText)

fun <T> ComponentContext.PickerControl(
    initialSelected: T? = null,
    displayMapper: (T?) -> StringDesc?,
): PickerControl<T> = PickerControl(componentScope, initialSelected, displayMapper)

@OptIn(DelicateCoroutinesApi::class)
fun <T> fakePickerControl(
    initialSelected: T? = null,
    displayMapper: (T?) -> StringDesc? = { null },
) = PickerControl(GlobalScope, initialSelected, displayMapper)

fun ComponentContext.CheckControl(
    initialChecked: Boolean = false,
): CheckControl = CheckControl(componentScope, initialChecked)

@OptIn(DelicateCoroutinesApi::class)
fun fakeCheckControl(initialChecked: Boolean = false) = CheckControl(GlobalScope, initialChecked)

fun ComponentContext.formValidator(
    buildBlock: FormValidatorBuilder.() -> Unit,
): FormValidator = componentScope.formValidator(buildBlock)

@OptIn(FlowPreview::class)
fun <T> InputControl.debouncedValue(
    scope: CoroutineScope,
    timeoutMillis: Long = 500L,
    transform: (String) -> T,
): StateFlow<T> = value
    .debounce { if (it.isBlank()) 0L else timeoutMillis }
    .map(transform)
    .stateIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        initialValue = transform(value.value)
    )

fun InputControl.debouncedValue(
    scope: CoroutineScope,
    timeoutMillis: Long = 500L,
): StateFlow<String> = debouncedValue(scope, timeoutMillis) { it }
