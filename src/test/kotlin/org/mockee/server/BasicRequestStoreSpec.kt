package org.mockee.server

import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import org.mockee.http.model.MockRequest
import org.mockee.http.model.RequestMethod
import org.mockee.http.model.StatusCode
import java.time.LocalDateTime
import java.util.*

class BasicRequestStoreSpec : WordSpec({
    "request dispatcher" should {

        "match requests by url and headers" {
            val uuid = UUID.randomUUID()
            val dateTime = LocalDateTime.now()

            val dispatcher = BasicRequestStore({ uuid }, { dateTime })

            val request = MockRequest(method = RequestMethod.GET,
                    url = "/my-app/users",
                    status = StatusCode(200),
                    requestHeaders = mapOf("X-App-Id" to "my-app", "X-Trace-Id" to "abc"),
                    responseHeaders = mapOf("Content-Type" to "text/plain"),
                    responseBody = "Pow! Wow!")

            dispatcher.saveRequest(request)

            val expected = StoredRequest(uuid = uuid,
                    createdDateTime = dateTime,
                    method = request.method,
                    url = request.url,
                    status = request.status,
                    requestHeaders = request.requestHeaders,
                    responseHeaders = request.responseHeaders,
                    responseBody = request.responseBody)

            dispatcher.getRequestByUrlAndHeaders(
                    method = "GET",
                    url = "/my-app/users",
                    headers = mapOf(
                            "X-App-Id" to "my-app",
                            "X-Trace-Id" to "abc")) shouldBe expected
        }

        "return empty when no matches" {
            val uuid = UUID.randomUUID()
            val dateTime = LocalDateTime.now()

            val dispatcher = BasicRequestStore({ uuid }, { dateTime })

            val request = MockRequest(method = RequestMethod.GET,
                    url = "/my-app/users",
                    status = StatusCode(200),
                    requestHeaders = mapOf("X-App-Id" to "my-app", "X-Trace-Id" to "abc"),
                    responseHeaders = mapOf("Content-Type" to "text/plain"),
                    responseBody = "Pow! Wow!")

            dispatcher.saveRequest(request)

            dispatcher.getRequestByUrlAndHeaders(method = "GET",
                    url = "/my-app/users",
                    headers = emptyMap()) shouldBe null
        }
    }
})
