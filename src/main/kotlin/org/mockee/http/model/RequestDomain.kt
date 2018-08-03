package org.mockee.http.model

import org.mockee.http.dsl.StatusCode

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

