# Work in progress/Tasks/Ideas

1. Add project to travis CI

2. Add project to distribution repo e.g bintray or other

3. Decide on REST server - investigate options, i.e jetty?  Or go low level - e.g Netty or HTTP Servlet
    - avoid heavyweight dependency tree
    - preference for non blocking
    - mock requests to be serailized on client, then sent to server for processing
        - what serialisation framework?  avro?
    - server handles request matching and responses

4. Implement server side request processing

5. Implement server side request matching 

6. API to request server state
    - request history
    - unmatched requests
    - current active mocks
    - server configuration

7. Allow some configurations to be global instead of per endpoint - i.e Jittered/delayed response

8. Log unmatched requests as errors or throws exception


9. Matching url based on lambda, regex or string
```kotlin
    url("/users")
    url(Regex("(.*)"))
    url(url -> { url.beginWith("/users/silver") })
```

10. Matching request body based on string, lambda, regex, json (for PUT, POST, DELETE only)
```kotlin
    put {
        requestBody(str -> str.contains("foo"))
        requestBody(Regex("(.*)")

        requestBody[T](entity:T -> entity.user.type.equals("gold"))
        //would have to provide a deserialiser for this to work
        //possibly inefficient?  When request is recieved would have to try and
        //deserialise to see if request matchs, could this be optimised?
        //alternative = json path?
    }
```

11. Jittered/delayed responses
```kotlin
    get {
        response {
            delay(stategy = RandomDistrubution(min = 1ms, max = 200ms))
            delay(stategy = Linear(wait = 10ms))
            //allow custom strategy implementation?  must be serailizable
            delay(strategy = DelayStrategy(() -> Stream(1,2,3,4)) //Stream represents wait times

        }
    }
```
