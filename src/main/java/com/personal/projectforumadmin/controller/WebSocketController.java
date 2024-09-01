package com.personal.projectforumadmin.controller;

import com.personal.projectforumadmin.dto.websocket.WebSocketMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class WebSocketController {

    @MessageMapping("/hello")
    @SendTo("/topic/chat")
    public WebSocketMessage chat(WebSocketMessage message, Principal principal) throws Exception {
        Thread.sleep(1000); // 대화하는 느낌을 시뮬레이션

        return WebSocketMessage.of("Hello " + principal.getName() + "! " + message.content() + " is what you said?");
    }
}
