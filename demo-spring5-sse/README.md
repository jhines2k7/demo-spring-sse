# demo-spring5-sse

Demo with Spring Boot 2.0.0.BUILD-SNAPSHOT (Spring 5.0.0.BUILD-SNAPSHOT) and Server-Sent Events powered by reactor.core.publisher.Flux
 
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
