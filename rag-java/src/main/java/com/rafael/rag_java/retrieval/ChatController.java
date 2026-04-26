package com.rafael.rag_java.retrieval;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private final  ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<String> chat(@RequestBody String question) {
        String answer = chatService.retrieval(question);
        return ResponseEntity.ok(answer);
    }
}
