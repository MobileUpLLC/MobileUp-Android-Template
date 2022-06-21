package ru.mobileup.template.utils

/**
 * Helper for testing of Decompose component outputs.
 */
class OutputCaptor<T : Any> : (T) -> Unit {
    private var _outputs = mutableListOf<T>()
    val outputs get(): List<T> = _outputs

    override operator fun invoke(output: T) {
        _outputs.add(output)
    }
}