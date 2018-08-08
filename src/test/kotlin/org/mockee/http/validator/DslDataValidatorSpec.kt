package org.mockee.http.validator

import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import org.mockee.func.Left
import org.mockee.func.Right
import org.mockee.http.dsl.DslData
import org.mockee.http.model.MockRequest
import org.mockee.http.model.RequestMethod
import org.mockee.http.model.StatusCode

class DslDataValidatorSpec : WordSpec({
    "validate" should {

        "should return a Mock Request when all parameters are valid" {
            val request = DslData(RequestMethod.GET)
            request.statusCode = StatusCode(200)
            request.path = "/my-app/users"
            request.stringBody = """ { "status" : "OK" } """
            request.requestHeaders.put("X-Session-Id", "1234")
            request.responseHeaders.put("Content-Type", "application/json")

            val expected = Right<InvalidMockRequest, MockRequest>(MockRequest(
                    method = RequestMethod.GET,
                    url = "/my-app/users",
                    status = StatusCode(200),
                    requestHeaders = mapOf("X-Session-Id" to "1234"),
                    responseHeaders = mapOf("Content-Type" to "application/json"),
                    responseBody = """ { "status" : "OK" } """
            ))

            DslDataValidator.validateRequest(request) shouldBe expected
        }

        "should return a InvalidMockRequest failure for a missing path" {
            val request = DslData(RequestMethod.GET)
            request.statusCode = StatusCode(200)

            val error = InvalidMockRequest("missing url path required")
            val expected = Left<InvalidMockRequest, MockRequest>(error)
            DslDataValidator.validateRequest(request) shouldBe expected
        }

        "should return a InvalidMockRequest failure for a missing status code" {
            val request = DslData(RequestMethod.GET)
            request.path = "/my-app/users"

            val error = InvalidMockRequest("missing statusCode required")
            val expected = Left<InvalidMockRequest, MockRequest>(error)
            DslDataValidator.validateRequest(request) shouldBe expected
        }
    }
})
