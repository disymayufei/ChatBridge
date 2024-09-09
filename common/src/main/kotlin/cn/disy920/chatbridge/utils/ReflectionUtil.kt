package cn.disy920.chatbridge.utils

import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

object ReflectionUtil {

    inline fun <reified T : Any> getSubClasses(): List<KClass<out T>> {
        return getSubClassesInPackage(T::class.java.`package`.name)
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Any> getSubClassesInPackage(packageName: String): List<KClass<out T>> {
        val classes: MutableList<KClass<out T>> = ArrayList()

        try {
            val classLoader = Thread.currentThread().contextClassLoader
            val path = packageName.replace('.', '/')
            val resources: Enumeration<URL> = classLoader.getResources(path)
            while (resources.hasMoreElements()) {
                val resource = resources.nextElement()
                val resourcePath = URLDecoder.decode(resource.file, StandardCharsets.UTF_8.name())
                val directory = File(resourcePath)
                if (!directory.exists()) continue
                val files = directory.listFiles() ?: continue

                for (file in files) {
                    if (!file.getName().endsWith(".class")) continue
                    val className = packageName + "." + file.getName().substring(0, file.getName().lastIndexOf('.'))
                    val kClass = Class.forName(className).kotlin
                    if (T::class.isSuperclassOf(kClass) && T::class != kClass) classes.add(kClass as KClass<out T>)
                }
            }
        } catch (e: Exception) {
            val logger = LoggerFactory.getLogger(ReflectionUtil::class.java)
            logger.error("Error while finding the sub class of ${T::class.simpleName}: ", e)
        }

        return classes
    }
}