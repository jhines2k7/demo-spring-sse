package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.file.dsl.Files;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.IOException;

@RestController
public class SseController {

    @Autowired
    private ScheduledService scheduledService;

    @Autowired
    private ReactiveService reactiveService;

    @Bean
    SubscribableChannel filesChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    IntegrationFlow integrationFlow(@Value("${input-dir:file://C:\\Users\\James\\Desktop\\sse-in}") File in) {
        return IntegrationFlows.from(Files.inboundAdapter(in).autoCreateDirectory(true),
                poller -> poller.poller(spec -> spec.fixedRate(1000L)))
                .transform(File.class, File::getAbsolutePath)
                .channel(filesChannel())
                .get();
    }

    @GetMapping(value = "/files/{name}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<String> files(@PathVariable String name) {
        return Flux.create(
                s -> {
                    MessageHandler handler = msg -> s.next(String.class.cast(msg.getPayload()));
                    filesChannel().subscribe(handler);
                    //s.complete();
                });
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
