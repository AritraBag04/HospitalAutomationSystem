package com.liquidator.app.chat.controller;

import com.liquidator.app.chat.entity.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Slf4j
@Controller
public class ChatController {
    @MessageMapping("/chat")
    @SendTo("/topic/public")
    public String sendMessage(ChatMessage message) throws InterruptedException {
        log.info("Message received: " + message.getMessage() + " from: " + message.getSender());
        Thread.sleep(1000);
        return HtmlUtils.htmlEscape(message.getMessage());
    }
}
