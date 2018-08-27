
```
curl -X POST -d -H "Content-Type:application/json" -d '
{
  "requestHeaders": {"X-App-Id": "my-app"},
  "responseHeaders": {
    "X-Trace": "ABCD",
    "X-Test": "123"
  },
  "responseBody": "Pow! Wow!",
  "method": "GET",
  "url": "/my-app/users",
  "status": {"code": 200}
}' http://localhost:8080/_admin_/_mock
```

```
curl -H "X-App-Id:my-app" -v http://localhost:8080/my-app/users
```