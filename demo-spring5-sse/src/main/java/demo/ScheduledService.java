package demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.file.dsl.Files;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.Date;

@Component
public class ScheduledService {

    private final EmitterProcessor<ServerSentEvent<Message>> emitter;

    @Bean
    SubscribableChannel filesChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    IntegrationFlow integrationFlow(@Value("${input-dir:file://C:\\Users\\Jhines\\Desktop\\sse-in}") File in) {
        return IntegrationFlows.from(Files.inboundAdapter(in).autoCreateDirectory(true),
                poller -> poller.poller(spec -> spec.fixedRate(1000L)))
                .transform(File.class, File::getAbsolutePath)
                .channel(filesChannel())
                .get();
    }

    public ScheduledService() {
        emitter = EmitterProcessor.create();
    }

    public Flux<ServerSentEvent<Message>> getMessages() {
        return emitter.log();
    }

    //@Scheduled(fixedRate = 1000)
    void timerHandler() {
        filesChannel().subscribe(msg -> {
            try {
                emitter.onNext(
                    ServerSentEvent.builder(
                        new Message(new Date().toString())
                    ).build()
                );
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }
}
