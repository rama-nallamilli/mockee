package org.mockee.server

import io.javalin.Context
import io.javalin.Javalin
import org.mockee.http.model.MockRequest
import org.mockee.serialisation.DecodeObjectError

class MockeeService(private val port: Int,
                    requestStore: RequestStore) {

    private val storeMockRequestHandler: (Context) -> Unit = { ctx: Context ->
        try {

            val request = ctx.bodyAsClass(MockRequest::class.java)
            requestStore.saveRequest(request)

            val response = """
                METHOD  = ${request.method.javaClass.simpleName}
                URL     = ${request.url}
                STATUS  = [${request.status}]
                HEADERS = [${request.requestHeaders}]
            """.trimIndent()

            ctx.status(200)
            ctx.result(response)

        } catch (e: DecodeObjectError) {
            ctx.result("Failed to decode request, ${e.message}")
            ctx.status(400)
        }
    }

    private val lookupMockRequestHandler: (Context) -> Unit = { ctx: Context ->
        val storedRequest = requestStore.getRequestByUrlAndHeaders(
                method = ctx.method(),
                url = ctx.url(),
                headers = ctx.headerMap()
        )

        val headers = storedRequest?.responseHeaders ?: emptyMap()
        val statusCode = storedRequest?.status ?: 404
        val body = storedRequest?.responseBody

        headers.forEach { k, v ->  ctx.header(k, v) }
        body?.let { ctx.result(it) }
        ctx.status(statusCode)
    }

    fun init(): Javalin {

        val app = Javalin.create().apply {
            port(port)
            exception(Exception::class.java) { e, _ -> e.printStackTrace() } //todo add logger
        }.start()

        app.routes {
            app.post("/_admin_/_mock", storeMockRequestHandler)
            app.get("/*", lookupMockRequestHandler)
        }

        return app
    }
}

