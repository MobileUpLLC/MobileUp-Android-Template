package ru.mobileup.template.features

fun readTestResource(path: String): String {
    val stream = TestResourceMarker::class.java.classLoader
        ?.getResourceAsStream(path)
        ?: Thread.currentThread().contextClassLoader
            ?.getResourceAsStream(path)
        ?: ClassLoader.getSystemResourceAsStream(path)
        ?: error(
            "Resource not found at path: $path. " +
                "Expected location: features/src/test/resources/$path"
        )

    return stream
        .bufferedReader()
        .use { it.readText() }
}

private object TestResourceMarker
