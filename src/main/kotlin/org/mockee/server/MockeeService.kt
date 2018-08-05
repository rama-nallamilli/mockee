package org.mockee.server

import io.javalin.Context
import io.javalin.Javalin
import org.mockee.http.model.MockRequest
import org.mockee.serialisation.DecodeObjectError
import org.mockee.serialisation.decodeToObject
import java.time.LocalDateTime
import java.util.*

class MockeeService(port: Int,
                    requestStore: RequestStore) {
    private val app = Javalin.start(port)

    private val storeMockRequestHandler: (Context) -> Unit = { ctx: Context ->
        try {

            //todo: add a test for handlers
            val body = ctx.body()
            println(body)
            val request = decodeToObject<MockRequest>(ctx.body())
            println(request)
            requestStore.saveRequest(request)

            val response = """
                METHOD  = ${request.method}
                URL     = ${request.url}
                STATUS  = [${request.status}]
                HEADERS = [${request.requestHeaders}]
            """.trimIndent()

            ctx.status(200)
            ctx.result(response)

        } catch(e: DecodeObjectError) {
            ctx.result("Failed to decode request, ${e.message}")
            ctx.status(400)
        }
    }

    fun start() {
        app.post("/_admin_/_mock", storeMockRequestHandler)
//
//        val path = "/*"
//        app.get(path, loadMockRequestHandler)
//        app.put(path, loadMockRequestHandler)
//        app.post(path, loadMockRequestHandler)
//        app.delete(path, loadMockRequestHandler)
    }

}

fun main(args: Array<String>) {

    val requestStore = BasicRequestStore(
            genUUID = { UUID.randomUUID() },
            genDateTime = { LocalDateTime.now() })

    MockeeService(port = 8081, requestStore = requestStore).start()
}