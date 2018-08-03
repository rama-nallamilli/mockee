package org.mockee.server

import io.javalin.Context
import io.javalin.Javalin

class MockeeService(port: Int) {
    private val app = Javalin.start(port)

    val requestHandler: (Context) -> Unit = { ctx: Context ->
        ctx.result("${ctx.method()} ${ctx.uri()}")
    }

    fun start() {
        app.post("/admin/mock") { ctx ->
            val headers = ctx.headerMap()
            val body = ctx.bodyAsBytes()
            ctx.result("Hello World $headers")
        }

        app.get("/*", requestHandler)
        app.put("/*", requestHandler)
        app.post("/*", requestHandler)
        app.delete("/*", requestHandler)
    }

}

fun main(args: Array<String>) {
    MockeeService(8081).start()
}