package org.mockee.http.model

import org.mockee.http.dsl.StatusCode
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.util.*

sealed class RequestMethod {
    object GET : RequestMethod()
    object PUT : RequestMethod()
    object DELETE : RequestMethod()
    object POST : RequestMethod()
}

data class MockRequest(val method: RequestMethod,
                       val url: String,
                       val status: StatusCode,
                       val requestHeaders: Map<String, String>,
                       val responseHeaders: Map<String, String>,
                       val responseBody: String?)


object Ser {
    fun <T> serializeToBytes(obj: T): ByteArray =
            ByteArrayOutputStream().use { it ->
                ObjectOutputStream(it).writeObject(obj)
                return it.toByteArray()
            }

    inline fun <reified T> deserialize(arr: ByteArray): T {
        return ObjectInputStream(arr.inputStream()).readObject() as T
    }
}

val ha: () -> Unit = {
    print("ha")
    throw RuntimeException("lol")
}

class RequestBody : Serializable {

    fun request(): () -> Int = { 1000 }

}

fun main(args: Array<String>) {
//    val ha: () -> Unit = { print("ha")
//        throw RuntimeException("lol")
//    }
//
//    val test = RequestBody()
//    val bytes = Ser.serializeToBytes(test)
//    println(Base64.getEncoder().encodeToString(bytes))
//    Ser.deserialize<() -> Unit>(bytes)()

    val by = Base64.getDecoder().decode("rO0ABXNyACFvcmcubW9ja2VlLmh0dHAubW9kZWwuUmVxdWVzdEJvZHmatFNwwLthWQIAAHhw")
    val result = Ser.deserialize<RequestBody>(by)
    println(result.request().invoke())
}

