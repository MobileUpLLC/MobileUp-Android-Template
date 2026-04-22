package ru.mobileup.template.features.utils

import org.jetbrains.compose.resources.ExperimentalResourceApi
import ru.mobileup.template.features.generated.resources.Res

@OptIn(ExperimentalResourceApi::class)
suspend fun readTestResource(path: String): String {
    return Res.readBytes("files/test/$path").decodeToString()
}
