package org.mockee.http.model

import java.io.Serializable

sealed class RequestMethod {
    object GET : RequestMethod()
    object PUT : RequestMethod()
    object DELETE : RequestMethod()
    object POST : RequestMethod()
}

data class StatusCode(val code: Int): Serializable

data class MockRequest(val method: String,
                       val url: String,
                       val status: StatusCode,
                       val requestHeaders: Map<String, String>,
                       val responseHeaders: Map<String, String>,
                       val responseBody: String?): Serializable

