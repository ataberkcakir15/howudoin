package com.ataberkcakir.howudoin.controller;

import com.ataberkcakir.howudoin.dto.GroupCreationRequest;
import com.ataberkcakir.howudoin.dto.GroupDTO;
import com.ataberkcakir.howudoin.dto.GroupMessageDTO;
import com.ataberkcakir.howudoin.model.Group;
import com.ataberkcakir.howudoin.model.GroupMessage;
import com.ataberkcakir.howudoin.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    // Create a new group
    @PostMapping("/create")
    public ResponseEntity<?> createGroup(@RequestBody GroupCreationRequest groupRequest,
                                         Authentication authentication) {
        String creatorEmail = authentication.getName();
        try {
            Group group = groupService.createGroup(creatorEmail, groupRequest.getName(), groupRequest.getMemberEmails());
            return ResponseEntity.ok(convertToDTO(group));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Send a message to a group
    @PostMapping("/{groupId}/messages")
    public ResponseEntity<?> sendGroupMessage(@PathVariable String groupId,
                                              @RequestParam String content,
                                              Authentication authentication) {
        String senderEmail = authentication.getName();
        try {
            GroupMessage message = groupService.sendGroupMessage(senderEmail, groupId, content);
            return ResponseEntity.ok(convertToDTO(message));
        } catch (IllegalArgumentException | SecurityException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Get messages for a group
    @GetMapping("/{groupId}/messages")
    public ResponseEntity<?> getGroupMessages(@PathVariable String groupId,
                                              Authentication authentication) {
        String userEmail = authentication.getName();
        try {
            List<GroupMessage> messages = groupService.getGroupMessages(groupId, userEmail);
            List<GroupMessageDTO> messageDTOs = messages.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(messageDTOs);
        } catch (IllegalArgumentException | SecurityException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    private GroupDTO convertToDTO(Group group) {
        GroupDTO dto = new GroupDTO();
        dto.setId(group.getId());
        dto.setName(group.getName());
        dto.setCreatorEmail(group.getCreator().getEmail());
        dto.setMemberEmails(group.getMembers().stream()
                .map(member -> member.getEmail())
                .collect(Collectors.toSet()));
        return dto;
    }

    private GroupMessageDTO convertToDTO(GroupMessage message) {
        GroupMessageDTO dto = new GroupMessageDTO();
        dto.setId(message.getId());
        dto.setGroupId(message.getGroup().getId());
        dto.setSenderEmail(message.getSender().getEmail());
        dto.setContent(message.getContent());
        dto.setTimestamp(message.getTimestamp());
        return dto;
    }
}
