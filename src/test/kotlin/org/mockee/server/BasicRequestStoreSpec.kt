package org.mockee.server

import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import org.mockee.http.model.MockRequest
import org.mockee.http.model.StatusCode
import java.time.LocalDateTime
import java.util.*

class BasicRequestStoreSpec : WordSpec({
    "request dispatcher" should {

        "match requests by url and headers" {
            val uuid = UUID.randomUUID()
            val dateTime = LocalDateTime.now()

            val dispatcher = BasicRequestStore({ uuid }, { dateTime })

            val request1 = MockRequest(method = "GET",
                    url = "/my-app/users",
                    status = StatusCode(200),
                    requestHeaders = mapOf("X-App-Id" to "my-app", "X-Trace-Id" to "abc"),
                    responseHeaders = mapOf("Content-Type" to "text/plain"),
                    responseBody = "Pow! Wow!",
                    requestBody = "Request body!")

            val request2 = request1.copy(method = "PUT")
            val request3 = request1.copy(url = "/my-app/users/rama")

            dispatcher.saveRequest(request1)
            dispatcher.saveRequest(request2)
            dispatcher.saveRequest(request3)

            val expected = StoredRequest(uuid = uuid,
                    createdDateTime = dateTime,
                    method = request1.method,
                    url = request1.url,
                    status = request1.status.code,
                    requestHeaders = request1.requestHeaders,
                    responseHeaders = request1.responseHeaders,
                    responseBody = request1.responseBody,
                    requestBody = request1.requestBody)

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

            val request = MockRequest(method = "GET",
                    url = "/my-app/users",
                    status = StatusCode(200),
                    requestHeaders = mapOf("X-App-Id" to "my-app", "X-Trace-Id" to "abc"),
                    responseHeaders = mapOf("Content-Type" to "text/plain"),
                    responseBody = "Pow! Wow!",
                    requestBody = "Request body!")

            dispatcher.saveRequest(request)

            dispatcher.getRequestByUrlAndHeaders(method = "GET",
                    url = "/my-app/users",
                    headers = emptyMap()) shouldBe null
        }
    }
})
