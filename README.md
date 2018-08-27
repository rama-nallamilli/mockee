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
        path("/my-app/users")
        header("X-App-Id", "my-app")
        stringBody("request body")

        response {
            header("X-Test", "123")
            header("X-Trace", "ABCD")
            status(200)
            stringBody("Pow! Wow!")
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
