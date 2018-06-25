Tasks/WIP/Ideas:

Add project to travis CI

Add project to distribution repo e.g bintray or other

Decide on REST server - investigate options, i.e jetty?  Or go low level - e.g Netty or HTTP Servlet
    - avoid heavyweight dependency tree
    - preference for non blocking
    - mock requests to be serailized on client, then sent to server for processing
        - what serialisation framework?  avro?
    - server handles request matching and responses

Implement server side request processing

Implement server side request matching 

API to request server state
    - request history
    - unmatched requests
    - current active mocks
    - server configuration

Allow some configurations to be global instead of per endpoint - i.e Jittered/delayed response

Log unmatched requests as errors or throws exception


Matching url based on lambda, regex or string
```
    url("/users")
    url(Regex("(.*)"))
    url(url -> { url.beginWith("/users/silver") })
```

Matching request body based on string, lambda, regex, json (for PUT, POST, DELETE only)
```
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

Jittered/delayed responses
```
    get {
        response {
            delay(stategy = RandomDistrubution(min = 1ms, max = 200ms))
            delay(stategy = Linear(wait = 10ms))
            //allow custom strategy implementation?  must be serailizable
            delay(strategy = DelayStrategy(() -> Stream(1,2,3,4)) //Stream represents wait times

        }
    }
```