package org.mockee.server

import org.mockee.http.model.MockRequest
import org.mockee.http.model.RequestMethod
import org.mockee.http.model.StatusCode
import java.net.URI
import java.time.LocalDateTime
import java.util.*

data class StoredRequest(val uuid: UUID,
                         val createdDateTime: LocalDateTime,
                         val method: RequestMethod,
                         val url: String,
                         val status: StatusCode, //TODO should be abstracted away from the Request model
                         val requestHeaders: Map<String, String>,
                         val responseHeaders: Map<String, String>,
                         val responseBody: String?)

interface RequestStore {
    fun saveRequest(request: MockRequest)
    fun removeRequest(request: MockRequest)

    fun getRequestByUrlAndHeaders(method: String,
                                  url: String,
                                  headers: Map<String, String>): StoredRequest?
}

class BasicRequestStore(private val genUUID: () -> UUID,
                        private val genDateTime: () -> LocalDateTime) : RequestStore {

    data class RequestKey(val method: String, val url: String)

    private val requests = mutableMapOf<RequestKey, MutableList<StoredRequest>>()

    override fun saveRequest(request: MockRequest) {
        val key = RequestKey(request.method.javaClass.simpleName, request.url)
        val requestsByKey = requests.getOrPut(key = key, defaultValue = { mutableListOf() })

        val toStore = StoredRequest(uuid = genUUID(),
                createdDateTime = genDateTime(),
                method = request.method,
                url = request.url,
                status = request.status,
                requestHeaders = request.requestHeaders,
                responseHeaders = request.responseHeaders,
                responseBody = request.responseBody)
        requestsByKey.add(toStore)
    }

    override fun removeRequest(request: MockRequest): Unit = throw NotImplementedError()

    override fun getRequestByUrlAndHeaders(method: String,
                                           url: String,
                                           headers: Map<String, String>): StoredRequest? {
        val sanitizedPath = sanitizeUrlPath(url)
        val key = RequestKey(method, sanitizedPath)
        val requestsByKey = requests[key]

        val matching = requestsByKey?.filter { f ->
            //todo: header check should be contains all? not equal as client can add extra headers such as Accept, Host etc
            f.url == url && f.requestHeaders == headers
        }

        return matching?.lastOrNull()
    }

    private fun sanitizeUrlPath(url: String): String {
        return URI(url).normalize().path
    }
}

