package org.mockee.server

import java.time.LocalDateTime
import java.util.*

fun main(args: Array<String>) {

    val port = System.getProperty("mockee.http.port")?.toInt() ?: 8080

    val requestStore = BasicRequestStore(
            genUUID = { UUID.randomUUID() },
            genDateTime = { LocalDateTime.now() })

    val server = MockeeService(port, requestStore).start()

    Runtime.getRuntime().addShutdownHook(Thread(
            Runnable {
                server.stop()
            }))
}