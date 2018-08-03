package org.mockee.serialisation

import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
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
    }
})
