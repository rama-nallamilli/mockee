package org.mockee.http.model

import java.io.Serializable

object RequestDomainVersion {
    const val version = 1L
}

sealed class RequestMethod : Serializable {
    object GET : RequestMethod()
    object PUT : RequestMethod()
    object DELETE : RequestMethod()
    object POST : RequestMethod()

    companion object {
        private val serialVersionUID: Long = RequestDomainVersion.version
    }
}

data class StatusCode(val code: Int): Serializable {
    companion object {
        private val serialVersionUID: Long = RequestDomainVersion.version
    }
}

data class MockRequest(val method: RequestMethod,
                       val url: String,
                       val status: StatusCode,
                       val requestHeaders: Map<String, String>,
                       val responseHeaders: Map<String, String>,
                       val responseBody: String?): Serializable {
    companion object {
        private val serialVersionUID: Long = RequestDomainVersion.version
    }
}
