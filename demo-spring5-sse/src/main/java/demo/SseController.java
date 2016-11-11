package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.io.IOException;

@RestController
public class SseController {

    @Autowired
    private ScheduledService scheduledService;

    @Autowired
    private ReactiveService reactiveService;

    @GetMapping(name = "/sse/infinite", produces = "text/event-stream")
    public Flux<ServerSentEvent<Message>> getInfiniteMessages() {
        return scheduledService.getMessages();
    }

    @GetMapping(path = "/sse/finite/{count}", produces = "text/event-stream")
    public Flux<Message> getFiniteMessages(@PathVariable int count) throws IOException {
        return reactiveService.processMany(count);
    }
}
