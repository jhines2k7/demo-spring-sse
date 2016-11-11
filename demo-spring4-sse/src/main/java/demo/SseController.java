package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
public class SseController {

    @Autowired
    private ScheduledService scheduledService;

    @Autowired
    private ReactiveService reactiveService;

    @RequestMapping(path = "/sse/infinite", method = RequestMethod.GET)
    public SseEmitter getInfiniteMessages() throws IOException {
        return scheduledService.getMessages();
    }

    @RequestMapping(path = "/sse/finite/{count}", method = RequestMethod.GET)
    public SseEmitter getFiniteMessages(@PathVariable int count) throws IOException {
        return reactiveService.processMany(count);
    }
}
