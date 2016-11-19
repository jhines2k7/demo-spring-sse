# demo-spring4-sse

Demo with Spring Boot 1.4.1.RELEASE (Spring 4.3.3.RELEASE) and Server-Sent Events powered by org.springframework.web.servlet.mvc.method.annotation.SseEmitter
  
```
gradlew bootRun
```

```
http://localhost:8080
```

```
curl -X GET http://localhost:8080/sse/infinite
curl -X GET http://localhost:8080/sse/finite/12
```
