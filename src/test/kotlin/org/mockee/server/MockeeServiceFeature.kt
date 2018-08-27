package org.mockee.server

import io.javalin.Javalin
import io.kotlintest.Description
import io.kotlintest.Spec
import io.kotlintest.extensions.TestListener
import io.kotlintest.matchers.maps.mapcontain
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.FeatureSpec
import org.json.JSONObject
import org.mockee.http.dsl.mock
import java.time.LocalDateTime
import java.util.*

class MockeeServiceFeature : FeatureSpec({
    val url = "http://localhost:8000/"

    feature("Mockee Service") {
        scenario("should accept and resolve mock requests") {

            val mockRequest = mock {
                get {
                    path("/my-app/users")
                    header(key = "X-App-Id", value = "my-app")
                    stringBody("request body")

                    response {
                        header("X-Test", "123")
                        header("X-Trace", "ABCD")
                        status(200)
                        stringBody("Pow! Wow!")
                    }
                }
            }.request.right()

            val configureMockResponse = khttp.post(
                    url = url + "_admin_/_mock",
                    data = JSONObject(mockRequest),
                    headers = mapOf("Content-Type" to "application/json"))
            configureMockResponse.statusCode shouldBe 200

            val getUsersResponse = khttp.get(
                    url = url + "/my-app/users",
                    headers = mapOf("X-App-Id" to "my-app"))

            getUsersResponse.statusCode shouldBe 200
            getUsersResponse.text shouldBe "Pow! Wow!"
            getUsersResponse.headers should mapcontain("X-Test", "123")
            getUsersResponse.headers should mapcontain("X-Trace", "ABCD")
        }
    }

}) {
    override fun listeners(): List<TestListener> = listOf(MockeeApp)
}


object MockeeApp : TestListener {

    lateinit var app: Javalin
    val appPort = 8000

    override fun beforeSpec(description: Description, spec: Spec) {
        try {
            val requestStore = BasicRequestStore(
                    genUUID = { UUID.randomUUID() },
                    genDateTime = { LocalDateTime.now() })
            app = MockeeService(appPort, requestStore).start()
        } finally {
            super.beforeSpec(description, spec)
        }
    }

    override fun afterSpec(description: Description, spec: Spec) {
        try {
            app.stop()
        } finally {
            super.afterSpec(description, spec)
        }
    }
}