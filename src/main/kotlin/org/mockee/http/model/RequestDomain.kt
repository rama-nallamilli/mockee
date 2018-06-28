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

