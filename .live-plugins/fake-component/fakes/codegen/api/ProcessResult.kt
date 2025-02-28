package fakes.codegen.api

import com.squareup.kotlinpoet.CodeBlock

sealed interface ProcessingResult {
    val codeBlock: CodeBlock
}

object TODOResult : ProcessingResult {
    override val codeBlock = CodeBlock.of("TODO()")
}

data class MOCKResult(
    val nameInFile: String
) : ProcessingResult {
    override val codeBlock = CodeBlock.of("$nameInFile.MOCK")
}

data class CustomResult(
    override val codeBlock: CodeBlock
) : ProcessingResult

fun result(
    data: Any
): ProcessingResult = CustomResult(
    CodeBlock.of(data.toString())
)
