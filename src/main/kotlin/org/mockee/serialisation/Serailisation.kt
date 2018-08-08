package org.mockee.serialisation

import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*

class DecodeObjectError(message: String, cause: Throwable) : RuntimeException(message, cause)
class EncodeObjectError(message: String, cause: Throwable) : RuntimeException(message, cause)

fun <T> encodeToString(obj: T): String {
    try {
        val bytes = ByteArrayOutputStream().use { it ->
            ObjectOutputStream(it).writeObject(obj)
            it.toByteArray()
        }

        return Base64.getEncoder().encodeToString(bytes)
    } catch (e: Exception) {
        throw EncodeObjectError("Could not encode object", e)
    }
}

inline fun <reified T> decodeToObject(bytes: ByteArray): T {
    try {
        return ObjectInputStream(bytes.inputStream()).readObject() as T
    } catch (e: Exception) {
        throw DecodeObjectError("Could not decode object", e)
    }
}

inline fun <reified T> decodeToObject(base64: String): T {
    val bytes = Base64.getDecoder().decode(base64)
    return decodeToObject(bytes)
}