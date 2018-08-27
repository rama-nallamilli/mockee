package org.mockee.http.validator

import org.mockee.func.Either
import org.mockee.func.Left
import org.mockee.func.Right
import org.mockee.func.flatMap
import org.mockee.http.dsl.DslData
import org.mockee.http.model.MockRequest

data class InvalidMockRequest(val msg: String)

object DslDataValidator {

    fun validateRequest(data: DslData): Either<InvalidMockRequest, MockRequest> {

        val validatedPath = validateInitialised(
                getter = { data.path },
                errorMsg = "missing url path required")

        val validatedStatusCode = validateInitialised(
                getter = { data.statusCode },
                errorMsg = "missing statusCode required")

        return validatedPath.flatMap { path ->
            validatedStatusCode.flatMap { statusCode ->
                Right<InvalidMockRequest, MockRequest>(
                        MockRequest(method = data.requestMethod.javaClass.simpleName,
                                status = statusCode,
                                url = path,
                                requestHeaders = data.requestHeaders,
                                responseHeaders = data.responseHeaders,
                                responseBody = data.responseStringBody,
                                requestBody = data.requestStringBody
                        ))
            }
        }
    }

    private fun <T> validateInitialised(getter: () -> T,
                                        errorMsg: String): Either<InvalidMockRequest, T> {
        return try {
            Right(getter())
        } catch (e: UninitializedPropertyAccessException) {
            Left(InvalidMockRequest(errorMsg))
        }
    }
}