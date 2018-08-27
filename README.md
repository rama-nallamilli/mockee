# MocKee
[![Build Status](https://travis-ci.org/rama-nallamilli/mockee.svg?branch=master)](https://travis-ci.org/rama-nallamilli/mockee)
A HTTP layer mocking framework for Kotlin, inspired by Wiremock.  This library is currently a work in progress.

- a Kotlin fluent DSL
- low dependency framework

## Mocking a GET request

### Create a mock request
```
val myMockedRequest =
mock {
    get {
        url("/my-app/users")
        header("x-session-id", UUID.randomUuid)
        header("x-client-id", "customer-a")

        response {
            header("Content-Type", "application/json")
            status(200)
            stringBody("{ LOL BODY }") | jackson(a: T) | fn(request -> {})
        }
    }
}
```

### Submit mock request to run
```
    val mockServer = Server(port = 1234)

    // mock is removed after lambda runs (successful or not)
    mock(requests = myMockedRequest, server = mockServer) {
        val service = MyService()
        service.call()
    }

    //mock stays persistent until user runs remove mock
    globalMock(requests = myMockedRequest, server = mockServer)


    //clears mock
    clearMock(requests = myMockedRequest, server = mockServer)

```
## Work in progress
See [work in progress](wip.md)
