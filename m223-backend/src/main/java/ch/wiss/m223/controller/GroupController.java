package ch.wiss.m223.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.wiss.m223.model.ChatGroup;
import ch.wiss.m223.model.GroupMember;
import ch.wiss.m223.model.GroupRole;
import ch.wiss.m223.model.Message;
import ch.wiss.m223.model.User;
import ch.wiss.m223.model.dto.CreateGroupRequest;
import ch.wiss.m223.model.dto.MessageResponse;
import ch.wiss.m223.model.dto.SendMessageRequest;
import ch.wiss.m223.repository.ChatGroupRepository;
import ch.wiss.m223.repository.GroupMemberRepository;
import ch.wiss.m223.repository.MessageRepository;
import ch.wiss.m223.repository.UserRepository;
import ch.wiss.m223.security.UserDetailsImpl;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private ChatGroupRepository chatGroupRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    // -------------------------
    // CREATE GROUP
    // -------------------------
    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody CreateGroupRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow();

        ChatGroup group = new ChatGroup();
        group.setName(request.getName());

        chatGroupRepository.save(group);

        GroupMember member = new GroupMember();
        member.setUser(user);
        member.setGroup(group);
        member.setRole(GroupRole.OWNER);

        groupMemberRepository.save(member);

        return ResponseEntity.ok(group);
    }

    // -------------------------
    // GET ALL GROUPS
    // -------------------------
    @GetMapping
    public ResponseEntity<?> getAllGroups() {
        return ResponseEntity.ok(chatGroupRepository.findAll());
    }

    // -------------------------
    // JOIN GROUP
    // -------------------------
    @PostMapping("/{groupId}/join")
    public ResponseEntity<?> joinGroup(@PathVariable Long groupId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow();

        ChatGroup group = chatGroupRepository.findById(groupId)
                .orElse(null);

        if (group == null) {
            return ResponseEntity.badRequest().body("Group not found");
        }

        if (groupMemberRepository.existsByUserIdAndGroupId(user.getId(), group.getId())) {
            return ResponseEntity.badRequest().body("Already a member");
        }

        GroupMember member = new GroupMember();
        member.setUser(user);
        member.setGroup(group);
        member.setRole(GroupRole.MEMBER);

        groupMemberRepository.save(member);

        return ResponseEntity.ok("Joined group");
    }

    // -------------------------
    // MY GROUPS (FIXED)
    // -------------------------
    @GetMapping("/my")
    public ResponseEntity<?> getMyGroups() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<GroupMember> memberships =
                groupMemberRepository.findByUserId(userDetails.getId());

        var groups = memberships.stream()
                .map(GroupMember::getGroup)
                .toList();

        return ResponseEntity.ok(groups);
    }

    // -------------------------
    // MY MEMBERSHIPS
    // -------------------------
    @GetMapping("/my-memberships")
    public ResponseEntity<?> getMyMemberships() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<GroupMember> memberships =
                groupMemberRepository.findByUserId(userDetails.getId());

        return ResponseEntity.ok(memberships);
    }

    // -------------------------
    // GET MEMBERS
    // -------------------------
    @GetMapping("/{groupId}/members")
    public ResponseEntity<?> getMembers(@PathVariable Long groupId) {

        ChatGroup group = chatGroupRepository.findById(groupId)
                .orElseThrow();

        var members = groupMemberRepository.findAll()
                .stream()
                .filter(m -> m.getGroup().getId().equals(group.getId()))
                .toList();

        return ResponseEntity.ok(members);
    }

    // -------------------------
    // DELETE GROUP
    // -------------------------
    @Transactional
    @DeleteMapping("/{groupId}")
    public ResponseEntity<?> deleteGroup(@PathVariable Long groupId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow();

        ChatGroup group = chatGroupRepository.findById(groupId)
                .orElse(null);

        if (group == null) {
            return ResponseEntity.badRequest().body("Group not found");
        }

        GroupMember membership = groupMemberRepository.findByUserIdAndGroupId(user.getId(), group.getId());
        if (membership == null || membership.getRole() != GroupRole.OWNER) {
            return ResponseEntity.status(403).body("Only group owners can delete this group");
        }

        messageRepository.deleteByGroup(group);
        groupMemberRepository.deleteByGroup(group);
        chatGroupRepository.delete(group);

        return ResponseEntity.ok("Group deleted");
    }

    // -------------------------
    // SEND MESSAGE
    // -------------------------
    @PostMapping("/{groupId}/messages")
    public ResponseEntity<?> sendMessage(
            @PathVariable Long groupId,
            @RequestBody SendMessageRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow();

        ChatGroup group = chatGroupRepository.findById(groupId)
                .orElse(null);

        if (group == null) {
            return ResponseEntity.badRequest().body("Group not found");
        }

        boolean isMember = groupMemberRepository.existsByUserIdAndGroupId(user.getId(), group.getId());

        if (!isMember) {
            return ResponseEntity.status(403).body("You are not a member");
        }

        Message message = new Message();
        message.setContent(request.getContent());
        message.setSender(user);
        message.setGroup(group);

        messageRepository.save(message);

        return ResponseEntity.ok(
                new MessageResponse(
                        message.getId(),
                        message.getContent(),
                        message.getSender().getUsername()
                )
        );
    }

    // -------------------------
    // GET MESSAGES
    // -------------------------
    @GetMapping("/{groupId}/messages")
    public ResponseEntity<?> getMessages(@PathVariable Long groupId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow();

        ChatGroup group = chatGroupRepository.findById(groupId)
                .orElse(null);

        if (group == null) {
            return ResponseEntity.badRequest().body("Group not found");
        }

        boolean isMember = groupMemberRepository.existsByUserIdAndGroupId(user.getId(), group.getId());

        if (!isMember) {
            return ResponseEntity.status(403).body("You are not a member");
        }

        var messages = messageRepository.findByGroupOrderByIdAsc(group)
                .stream()
                .map(m -> new MessageResponse(
                        m.getId(),
                        m.getContent(),
                        m.getSender().getUsername()
                ))
                .toList();

        return ResponseEntity.ok(messages);
    }
}