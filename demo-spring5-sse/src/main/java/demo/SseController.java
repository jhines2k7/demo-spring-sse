package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.IOException;

@RestController
public class SseController {

    @Autowired
    private ScheduledService scheduledService;

    @Autowired
    private ReactiveService reactiveService;

    @GetMapping(path = "/sse/files", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Message>> getFiles() throws IOException{
        /*return Flux.create(
            s -> {
                filesChannel().subscribe(msg -> s.next(String.class.cast(msg.getPayload())));
                s.complete();
            });*/
        return scheduledService.getMessages();
    }

    @GetMapping(name = "/sse/infinite", produces = "text/event-stream")
    public Flux<ServerSentEvent<Message>> getInfiniteMessages() {
        return scheduledService.getMessages();
    }

    @GetMapping(path = "/sse/finite/{count}", produces = "text/event-stream")
    public Flux<Message> getFiniteMessages(@PathVariable int count) throws IOException {
        return reactiveService.processMany(count);
    }
}
