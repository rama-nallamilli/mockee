package org.mockee.serialisation

import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*

fun <T> encodeToString(obj: T): String {
    val bytes = ByteArrayOutputStream().use { it ->
        ObjectOutputStream(it).writeObject(obj)
        it.toByteArray()
    }

    return Base64.getEncoder().encodeToString(bytes)
}

inline fun <reified T> decodeToObject(base64: String): T {
    val bytes = Base64.getDecoder().decode(base64)
    return ObjectInputStream(bytes.inputStream()).readObject() as T
}