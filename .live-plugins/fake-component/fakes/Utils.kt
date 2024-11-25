package fakes

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asClassName
import org.jetbrains.kotlin.name.FqName
import kotlin.reflect.KClass

inline fun <reified T> packageFor(): String {
    return T::class.asClassName().toString()
}

fun packageFor(klass: KClass<*>): String {
    return klass.asClassName().toString()
}

fun nameWithParents(klass: KClass<*>): String {
    return klass.asClassName().simpleNames.joinToString(".")
}

val ClassName.simpleNamesJoined get() = simpleNames.joinToString(".")

val FqName.packageName
    get() = pathSegments().run {
        take(size - 1).joinToString(".")
    }

fun KClass<*>.fqName() = FqName(asClassName().toString())
