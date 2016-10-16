package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
public class SseController {

    @Autowired
    private MessageService messageService;

    @RequestMapping(path = "/sse/messages", method = RequestMethod.GET)
    public SseEmitter getMessages() throws IOException {
        return messageService.getMessages();
    }

}
