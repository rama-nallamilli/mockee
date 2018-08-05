package org.mockee.http.dsl

import org.mockee.func.Either
import org.mockee.http.model.MockRequest
import org.mockee.http.model.RequestMethod
import org.mockee.http.model.StatusCode
import org.mockee.http.validator.DslDataValidator
import org.mockee.http.validator.InvalidMockRequest

class DslData(val requestMethod: RequestMethod) {
    lateinit var statusCode: StatusCode
    lateinit var path: String

    var stringBody: String? = null
    val responseHeaders = mutableMapOf<String, String>()
    val requestHeaders = mutableMapOf<String, String>()
}

interface MockedDslRequestBuilder {
    fun build(): Either<InvalidMockRequest,MockRequest>
}

abstract class MockedDslRequestBuilderImpl : MockedDslRequestBuilder {
    abstract val requestMethod: RequestMethod

    val dslData by lazy {
        DslData(requestMethod)
    }

    fun path(url: String) {
        dslData.path = url
    }

    fun header(key: String, value: String) {
        dslData.requestHeaders[key] = value
    }

    fun response(init: ResponseDsl.() -> Unit): ResponseDsl {
        val responseBody = ResponseDsl(dslData)
        responseBody.init()
        return responseBody
    }

    override fun build(): Either<InvalidMockRequest, MockRequest> {
        return DslDataValidator.validateRequest(dslData)
    }
}

class ResponseDsl(private val data: DslData) {

    fun header(key: String, value: String) {
        data.responseHeaders[key] = value
    }

    fun stringBody(content: String) {
        data.stringBody = content
    }

    fun status(status: Int) {
        data.statusCode = StatusCode(status)
    }
}

class GetDsl : MockedDslRequestBuilderImpl() {
    override val requestMethod = RequestMethod.GET
}

class MockDsl {

    lateinit var request: Either<InvalidMockRequest,MockRequest>

    fun get(init: GetDsl.() -> Unit): GetDsl {
        val get = GetDsl()
        get.init()
        request = get.build()
        return get
    }

}

fun mock(init: MockDsl.() -> Unit): MockDsl {
    val mock = MockDsl()
    mock.init()
    return mock
}

