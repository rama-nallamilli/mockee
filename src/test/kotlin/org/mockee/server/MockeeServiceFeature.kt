package org.mockee.server

import io.javalin.Javalin
import io.kotlintest.Description
import io.kotlintest.Spec
import io.kotlintest.extensions.TestListener
import io.kotlintest.shouldBe
import io.kotlintest.specs.FeatureSpec
import org.mockee.http.dsl.mock
import org.mockee.serialisation.encodeToString
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

                    response {
                        header("Content-Type", "text/plain")
                        status(200)
                        stringBody("Pow! Wow!")
                    }
                }
            }.request.right()

            //TODO: Can we just encode lambdas rather than whole data class, i.e use JSON for data class and base64 encoding for lambda?
            val configureMockResponse = khttp.post(url = url + "_admin_/_mock", data = encodeToString(mockRequest))
            configureMockResponse.statusCode shouldBe 200

            val getUsersResponse = khttp.get(
                    url = url + "/my-app/users",
                    headers = mapOf("X-App-Id" to "my-app"))

            getUsersResponse.content shouldBe "Pow! Wow!".toByteArray()
            getUsersResponse.statusCode shouldBe 200
            getUsersResponse.headers shouldBe mapOf("X-App-Id" to "my-app")

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
            app = MockeeService(appPort, requestStore).init()
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