package org.lyaaz.fuckgram

import io.github.libxposed.api.XposedInterface
import java.lang.reflect.Method
import java.lang.reflect.Modifier

object HookUtils {
    fun logHookError(className: String, methodName: String, t: Throwable) {
        HookModule.log("Failed to hook $className::$methodName", t)
    }

    fun hookMethods(clazz: Class<*>, methodName: String, hooker: XposedInterface.Hooker): Boolean {
        return runCatching {
            findMethodsByName(clazz, methodName).forEach { HookModule.hook(it, hooker) }
        }.onFailure {
            logHookError(clazz.name, methodName, it)
        }.isSuccess
    }

    fun hookConstructors(clazz: Class<*>, hooker: XposedInterface.Hooker): Boolean {
        return runCatching {
            clazz.declaredConstructors.forEach { HookModule.hook(it, hooker) }
        }.onFailure {
            logHookError(clazz.name, "<init>", it)
        }.isSuccess
    }

    private fun findMethodsByName(clazz: Class<*>, methodName: String): List<Method> {
        val result = LinkedHashSet<Method>()
        var current: Class<*>? = clazz
        while (current != null && current != Any::class.java) {
            current.declaredMethods.forEach { method ->
                if (method.name == methodName && !Modifier.isAbstract(method.modifiers)) {
                    result.add(method)
                }
            }
            current = current.superclass
        }
        return result.toList()
    }
}
