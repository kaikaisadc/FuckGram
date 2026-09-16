package org.lyaaz.fuckgram

import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier

/**
 * Minimal reflection helpers replacing the legacy `XposedHelpers` field / static method accessors,
 * which the libxposed API does not provide.
 */
object Reflect {

    fun findField(clazz: Class<*>, name: String): Field {
        var current: Class<*>? = clazz
        while (current != null) {
            try {
                return current.getDeclaredField(name).apply { isAccessible = true }
            } catch (_: NoSuchFieldException) {
                current = current.superclass
            }
        }
        throw NoSuchFieldException("${clazz.name}.$name")
    }

    fun getObjectField(obj: Any, name: String): Any? = findField(obj.javaClass, name).get(obj)

    fun getBooleanField(obj: Any, name: String): Boolean = findField(obj.javaClass, name).getBoolean(obj)

    fun getIntField(obj: Any, name: String): Int = findField(obj.javaClass, name).getInt(obj)

    fun setIntField(obj: Any, name: String, value: Int) {
        findField(obj.javaClass, name).setInt(obj, value)
    }

    fun setBooleanField(obj: Any, name: String, value: Boolean) {
        findField(obj.javaClass, name).setBoolean(obj, value)
    }

    fun getStaticIntField(clazz: Class<*>, name: String): Int = findField(clazz, name).getInt(null)

    fun callStaticMethod(clazz: Class<*>, name: String, vararg args: Any?): Any? {
        val candidates = sequenceOf(clazz.methods.asSequence(), clazz.declaredMethods.asSequence())
            .flatten()
            .filter { Modifier.isStatic(it.modifiers) && it.name == name && it.parameterCount == args.size }
        val method: Method = candidates.firstOrNull { m ->
            m.parameterTypes.indices.all { i -> accepts(m.parameterTypes[i], args[i]) }
        } ?: throw NoSuchMethodException("${clazz.name}.$name(${args.joinToString { it?.javaClass?.name ?: "null" }})")
        method.isAccessible = true
        return method.invoke(null, *args)
    }

    private fun accepts(param: Class<*>, arg: Any?): Boolean {
        if (arg == null) return !param.isPrimitive
        return box(param).isInstance(arg)
    }

    private fun box(type: Class<*>): Class<*> = when (type) {
        java.lang.Boolean.TYPE -> java.lang.Boolean::class.java
        java.lang.Byte.TYPE -> java.lang.Byte::class.java
        java.lang.Character.TYPE -> java.lang.Character::class.java
        java.lang.Short.TYPE -> java.lang.Short::class.java
        java.lang.Integer.TYPE -> java.lang.Integer::class.java
        java.lang.Long.TYPE -> java.lang.Long::class.java
        java.lang.Float.TYPE -> java.lang.Float::class.java
        java.lang.Double.TYPE -> java.lang.Double::class.java
        else -> type
    }
}
