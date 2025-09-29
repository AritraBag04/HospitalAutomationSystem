package com.liquidator.app.controller;

import com.liquidator.app.entity.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Controller
public class ChatController{
    @MessageMapping("/chat")
    @SendTo("/topic/public")
    public String sendMessage(ChatMessage message) throws InterruptedException {
        log.info("Message received: " + message.getMessage() + " from: " + message.getSender());
        if(message.getMessage().startsWith("/form")) {
            HashMap<String, List<String>> form = decodeCommand(message.getMessage());

            HashMap<String, String> mpp = new HashMap<>();
            mpp.put("Name", "text");

            String htmlContent = "<h1>Hello</h1>";
            return HtmlUtils.htmlEscape(htmlContent);

        }
        // if /form -c CT_Scan MRI Chemotherapy then send a form back to the user
        // hashmap { compulsory: [], optional: [] }
        // 1. What tests to do?
        // 2. Do you accept the terms and conditions? This is for the patient
        // I want like a bot which will send a message in this chatroom with the form which the patient must accept
        // stub-based architecture -> gRPC ( Microservices communication)
        Thread.sleep(1000);
        String sender = (message.getSender() == null || message.getSender().trim().isEmpty()) ? "Anonymous" : message.getSender().trim();
        String text = (message.getMessage() == null) ? "" : message.getMessage();
        return HtmlUtils.htmlEscape(sender + ": " + text);
    }
    public HashMap<String, List<String>> decodeCommand(String input) {
        HashMap<String, List<String>> result = new HashMap<>();
        result.put("compulsory", new ArrayList<>());
        result.put("optional", new ArrayList<>());

        String[] tokens = input.trim().split("\\s+");
        boolean compulsory = false;

        for (String token : tokens) {
            if (token.equals("-c")) {
                compulsory = true;
            } else if (token.startsWith("/")) {
                // skip the command name (like /form)
                continue;
            } else {
                if (compulsory) {
                    result.get("compulsory").add(token);
                    compulsory = false; // reset after consuming one compulsory arg
                } else {
                    result.get("optional").add(token);
                }
            }
        }

        return result;
    }
}
