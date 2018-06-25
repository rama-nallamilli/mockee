package org.mockee.http.dsl

import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import org.mockee.http.model.RequestMethod
import org.mockee.http.model.MockRequest
import java.util.*

class DslDataValidatorSpec : WordSpec({
    "build get request" should {

        "should build a valid get request" {
            val traceId = UUID.randomUUID().toString()
            val mockRequest = mock {

                get {
                    path("/my-app/users")
                    header(key = "X-App-Id", value = "my-app")
                    header(key = "X-Trace-Id", value = traceId)

                    response {
                        header("Content-Type", "text/plain")
                        status(200)
                        stringBody("Pow! Wow!")
                    }
                }
            }.request.right()

            mockRequest shouldBe MockRequest(method = RequestMethod.GET,
                    url = "/my-app/users",
                    status = StatusCode(200),
                    requestHeaders = mapOf("X-App-Id" to "my-app", "X-Trace-Id" to traceId),
                    responseHeaders = mapOf("Content-Type" to "text/plain"),
                    responseBody = "Pow! Wow!")
        }
    }
})