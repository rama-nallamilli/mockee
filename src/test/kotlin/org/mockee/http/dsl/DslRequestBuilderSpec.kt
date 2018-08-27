package org.mockee.http.dsl

import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import org.mockee.http.model.MockRequest
import org.mockee.http.model.StatusCode
import java.util.*

class DslDataValidatorSpec : WordSpec({
    "DslDataValidator" should {

        "should build a valid GET request" {
            val traceId = UUID.randomUUID().toString()
            val sessionId = UUID.randomUUID().toString()
            val mockRequest = mock {

                get {
                    path("/my-app/users")
                    header(key = "X-App-Id", value = "my-app")
                    header(key = "X-Trace-Id", value = traceId)

                    response {
                        header("Content-Type", "text/plain")
                        header("X-Session-Id", sessionId)
                        status(200)
                        stringBody("Pow! Wow!")
                    }
                }
            }.request.right()

            mockRequest shouldBe MockRequest(method = "GET",
                    url = "/my-app/users",
                    status = StatusCode(200),
                    requestHeaders = mapOf("X-App-Id" to "my-app", "X-Trace-Id" to traceId),
                    responseHeaders = mapOf("Content-Type" to "text/plain", "X-Session-Id" to sessionId),
                    responseBody = "Pow! Wow!")
        }

        "should build a valid PUT request" {
            val traceId = UUID.randomUUID().toString()
            val sessionId = UUID.randomUUID().toString()

            val mockRequest = mock {

                put {
                    path("/my-app/users/rama")
                    header(key = "X-App-Id", value = "my-app")
                    header(key = "X-Trace-Id", value = traceId)
                    stringBody(""" { "age" : 21 } """)

                    response {
                        header("Content-Type", "text/plain")
                        header("X-Session-Id", sessionId)
                        status(200)
                        stringBody("Updated!")
                    }
                }
            }.request.right()

            mockRequest shouldBe MockRequest(method = "PUT",
                    url = "/my-app/users/rama",
                    status = StatusCode(200),
                    requestHeaders = mapOf("X-App-Id" to "my-app", "X-Trace-Id" to traceId),
                    responseHeaders = mapOf("Content-Type" to "text/plain", "X-Session-Id" to sessionId),
                    responseBody = "Updated!",
                    requestBody = """ { "age" : 21 } """)
        }

        "should build a valid POST request" {

            val traceId = UUID.randomUUID().toString()
            val sessionId = UUID.randomUUID().toString()

            val mockRequest = mock {

                post {
                    path("/my-app/users/rama/session")
                    header(key = "X-App-Id", value = "my-app")
                    header(key = "X-Trace-Id", value = traceId)
                    stringBody(""" { "sessionType" : "temp" } """)

                    response {
                        header("Content-Type", "text/plain")
                        header("X-Session-Id", sessionId)
                        status(201)
                        stringBody("""{ "sessionId" : "1234-5678" }""")
                    }
                }
            }.request.right()

            mockRequest shouldBe MockRequest(method = "POST",
                    url = "/my-app/users/rama/session",
                    status = StatusCode(201),
                    requestHeaders = mapOf("X-App-Id" to "my-app", "X-Trace-Id" to traceId),
                    responseHeaders = mapOf("Content-Type" to "text/plain", "X-Session-Id" to sessionId),
                    responseBody = """{ "sessionId" : "1234-5678" }""",
                    requestBody = """ { "sessionType" : "temp" } """)

        }


        "should build a valid DELETE request" {

            val traceId = UUID.randomUUID().toString()
            val sessionId = UUID.randomUUID().toString()

            val mockRequest = mock {

                delete {
                    path("/my-app/users/rama")
                    header(key = "X-App-Id", value = "my-app")
                    header(key = "X-Trace-Id", value = traceId)
                    stringBody(""" { "age" : 21 } """)

                    response {
                        header("Content-Type", "text/plain")
                        header("X-Session-Id", sessionId)
                        status(200)
                        stringBody("Deleted!")
                    }
                }
            }.request.right()

            mockRequest shouldBe MockRequest(method = "DELETE",
                    url = "/my-app/users/rama",
                    status = StatusCode(200),
                    requestHeaders = mapOf("X-App-Id" to "my-app", "X-Trace-Id" to traceId),
                    responseHeaders = mapOf("Content-Type" to "text/plain", "X-Session-Id" to sessionId),
                    responseBody = "Deleted!",
                    requestBody = """ { "age" : 21 } """)

        }


    }
})