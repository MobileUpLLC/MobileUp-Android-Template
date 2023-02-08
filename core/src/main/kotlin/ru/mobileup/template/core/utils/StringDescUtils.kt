package ru.mobileup.template.core.utils

import androidx.annotation.StringRes
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.ResourceFormattedStringDesc
import dev.icerock.moko.resources.desc.ResourceStringDesc
import dev.icerock.moko.resources.desc.StringDesc

@Suppress("FunctionName")
fun StringDesc.Companion.Resource(@StringRes resourceId: Int): ResourceStringDesc {
    return ResourceStringDesc(StringResource(resourceId))
}

@Suppress("FunctionName")
fun StringDesc.Companion.ResourceFormatted(
    @StringRes resourceId: Int,
    args: List<Any>
) = ResourceFormattedStringDesc(StringResource(resourceId), args)

@Suppress("FunctionName")
fun StringDesc.Companion.ResourceFormatted(
    @StringRes resourceId: Int,
    vararg args: Any
) = ResourceFormattedStringDesc(StringResource(resourceId), args.asList())
