package ru.mobileup.template.features

actual fun readTestResource(path: String): String {
    val stream = TestResourceMarker::class.java.classLoader
        ?.getResourceAsStream(path)
        ?: error(
            "Resource not found at path: $path. " +
                "Expected location: features/src/commonTest/resources/$path"
        )

    return stream
        .bufferedReader()
        .use { it.readText() }
}

private object TestResourceMarker
