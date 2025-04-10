package ru.mobileup.template.core.utils

import com.arkivanov.decompose.ComponentContext
import dev.icerock.moko.resources.desc.StringDesc
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
    visualTransformation: VisualTransformation = VisualTransformation.None
): InputControl = InputControl(
    componentScope,
    initialText,
    singleLine,
    maxLength,
    keyboardOptions,
    textTransformation,
    visualTransformation
)

fun <T> ComponentContext.PickerControl(
    initialSelected: T? = null,
    displayMapper: (T?) -> StringDesc?,
): PickerControl<T> = PickerControl(componentScope, initialSelected, displayMapper)

fun ComponentContext.CheckControl(
    initialChecked: Boolean = false
): CheckControl = CheckControl(componentScope, initialChecked)

fun ComponentContext.formValidator(
    buildBlock: FormValidatorBuilder.() -> Unit
): FormValidator = componentScope.formValidator(buildBlock)
