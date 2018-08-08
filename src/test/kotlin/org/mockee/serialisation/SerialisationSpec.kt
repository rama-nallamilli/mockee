package org.mockee.serialisation

import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import org.mockee.http.model.MockRequest
import org.mockee.http.model.RequestMethod
import org.mockee.http.model.StatusCode
import java.io.Serializable

class SerialisationSpec : WordSpec({
    "encodeToString and decodeToObject" should {

        "write and read a simple object" {
            val obj = "foo"
            val encodedStr = encodeToString(obj)

            encodedStr shouldBe "rO0ABXQAA2Zvbw=="

            val decodedObj = decodeToObject<String>(encodedStr)
            decodedObj shouldBe obj
        }

        "write/read a data class" {
            data class Foo(val bar: String, val i: Int) : Serializable

            val obj = Foo(bar = "foo", i = 1000)

            val encodedStr = encodeToString(obj)
            encodedStr shouldBe "rO0ABXNyADRvcmcubW9ja2VlLnNlcmlhbGlzYXRpb24uU2VyaWFsaXNhdGlvblNwZWMkMSQxJDIkRm9vWQI3634iLlICAAJJAAFpTAADYmFydAASTGphdmEvbGFuZy9TdHJpbmc7eHAAAAPodAADZm9v"

            val decodedObj = decodeToObject<Foo>(encodedStr)
            decodedObj shouldBe obj
        }

        "write/read a class with a lambda" {
            data class Bar(val foo: String, val fn: (String) -> Int) : Serializable

            val obj = Bar(foo = "hello", fn = { str: String -> str.toInt() * 2 })

            val encodedStr = encodeToString(obj)
            val decodedObj = decodeToObject<Bar>(encodedStr)
            decodedObj.foo shouldBe "hello"
            decodedObj.fn("10") shouldBe 20
        }

        "write/read a MockRequest" {
            val mock = MockRequest(
                    method = RequestMethod.GET,
                    url = "/my-app/users",
                    status = StatusCode(200),
                    requestHeaders = mapOf("X-Session-Id" to "1234"),
                    responseHeaders = mapOf("Content-Type" to "application/json"),
                    responseBody = """ { "status" : "OK" } """
            )

            val encoded = encodeToString(mock)

            val decoded = decodeToObject<MockRequest>(encoded)
            decoded.method.shouldBeInstanceOf<RequestMethod.GET>()
            decoded.url shouldBe "/my-app/users"
            decoded.status shouldBe StatusCode(200)
            decoded.requestHeaders shouldBe mapOf("X-Session-Id" to "1234")
            decoded.responseHeaders shouldBe mapOf("Content-Type" to "application/json")
            decoded.responseBody shouldBe """ { "status" : "OK" } """

        }
    }
})
