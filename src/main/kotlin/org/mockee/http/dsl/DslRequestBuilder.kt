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
    var requestStringBody: String? = null

    var responseStringBody: String? = null
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
        dslData.path = url //todo validate path, all start with /
    }

    fun header(key: String, value: String) {
        dslData.requestHeaders[key] = value
    }

    fun stringBody(content: String) {
        dslData.requestStringBody = content
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
        data.responseStringBody = content
    }

    fun status(status: Int) {
        data.statusCode = StatusCode(status)
    }
}

class GetDsl : MockedDslRequestBuilderImpl() {
    override val requestMethod = RequestMethod.GET
}

class PutDsl : MockedDslRequestBuilderImpl() {
    override val requestMethod = RequestMethod.PUT
}


class PostDsl : MockedDslRequestBuilderImpl() {
    override val requestMethod = RequestMethod.POST
}


class DeleteDsl : MockedDslRequestBuilderImpl() {
    override val requestMethod = RequestMethod.DELETE
}

class MockDsl {

    lateinit var request: Either<InvalidMockRequest,MockRequest>

    fun get(init: GetDsl.() -> Unit): GetDsl {
        val get = GetDsl()
        get.init()
        request = get.build()
        return get
    }

    fun put(init: PutDsl.() -> Unit): PutDsl {
        val put = PutDsl()
        put.init()
        request = put.build()
        return put
    }

    fun post(init: PostDsl.() -> Unit): PostDsl {
        val post = PostDsl()
        post.init()
        request = post.build()
        return post
    }

    fun delete(init: DeleteDsl.() -> Unit): DeleteDsl {
        val delete = DeleteDsl()
        delete.init()
        request = delete.build()
        return delete
    }

}

fun mock(init: MockDsl.() -> Unit): MockDsl {
    val mock = MockDsl()
    mock.init()
    return mock
}

