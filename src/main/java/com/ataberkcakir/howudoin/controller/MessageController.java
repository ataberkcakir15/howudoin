package com.ataberkcakir.howudoin.controller;

import com.ataberkcakir.howudoin.dto.MessageDTO;
import com.ataberkcakir.howudoin.dto.MessageRequest;
import com.ataberkcakir.howudoin.model.Message;
import com.ataberkcakir.howudoin.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody @Valid MessageRequest messageRequest, Authentication authentication) {
        String fromEmail = authentication.getName();
        String toEmail = messageRequest.getToEmail();
        String content = messageRequest.getContent();

        String result = messageService.sendMessage(fromEmail, toEmail, content);
        if (result.equals("User not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        } else {
            return ResponseEntity.ok(result);
        }
    }

    // Get messages between two users
    @GetMapping("/between")
    public ResponseEntity<?> getMessagesBetweenUsers(@RequestParam String otherUserEmail,
                                                     Authentication authentication) {
        String userEmail = authentication.getName();
        List<Message> messages = messageService.getMessagesBetweenUsers(userEmail, otherUserEmail);

        // Convert to DTOs
        List<MessageDTO> messageDTOs = messages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(messageDTOs);
    }

    private MessageDTO convertToDTO(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setSenderEmail(message.getSender().getEmail());
        dto.setReceiverEmail(message.getReceiver().getEmail());
        dto.setContent(message.getContent());
        dto.setTimestamp(message.getTimestamp());
        return dto;
    }
}
