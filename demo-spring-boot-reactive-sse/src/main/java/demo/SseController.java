package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class SseController {

    @Autowired
    private MessageService messageService;

    @GetMapping(name = "/sse/messages", produces = "text/event-stream")
    public Flux<Message> getMessages() {
        return messageService.getMessages();
    }
}
