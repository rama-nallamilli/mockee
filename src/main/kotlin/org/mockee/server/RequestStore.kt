package org.mockee.server

import org.mockee.http.model.MockRequest
import org.mockee.http.model.RequestMethod
import org.mockee.http.model.StatusCode
import java.time.LocalDateTime
import java.util.*

data class StoredRequest(val uuid: UUID,
                         val createdDateTime: LocalDateTime,
                         val method: RequestMethod,
                         val url: String,
                         val status: StatusCode, //TODO fix this, should be seperate model
                         val requestHeaders: Map<String, String>,
                         val responseHeaders: Map<String, String>,
                         val responseBody: String?)

interface RequestStore {
    fun saveRequest(request: MockRequest)
    fun removeRequest(request: MockRequest)

    fun getRequestByUrlAndHeaders(method: RequestMethod,
                                  url: String,
                                  headers: Map<String, String>,
                                  status: Int): StoredRequest?
}

class BasicRequestStore(private val genUUID: () -> UUID,
                        private val genDateTime: () -> LocalDateTime) : RequestStore {

    data class RequestKey(val method: RequestMethod, val status: Int)

    private val requests = mutableMapOf<RequestKey, MutableList<StoredRequest>>()

    override fun saveRequest(request: MockRequest) {
        val key = RequestKey(request.method, request.status.code)
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

    override fun getRequestByUrlAndHeaders(method: RequestMethod,
                                           url: String,
                                           headers: Map<String, String>,
                                           status: Int): StoredRequest? {
        val key = RequestKey(method, status)
        val requestsByKey = requests[key]

        val matching = requestsByKey?.filter { f ->
            f.url == url && f.requestHeaders == headers
        }

        return matching?.lastOrNull()
    }
}

