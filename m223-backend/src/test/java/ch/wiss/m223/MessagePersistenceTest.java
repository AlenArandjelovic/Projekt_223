package ch.wiss.m223;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import ch.wiss.m223.controller.GroupController;
import ch.wiss.m223.model.ChatGroup;
import ch.wiss.m223.model.GroupMember;
import ch.wiss.m223.model.GroupRole;
import ch.wiss.m223.model.Message;
import ch.wiss.m223.model.User;
import ch.wiss.m223.model.dto.SendMessageRequest;
import ch.wiss.m223.repository.ChatGroupRepository;
import ch.wiss.m223.repository.GroupMemberRepository;
import ch.wiss.m223.repository.MessageRepository;
import ch.wiss.m223.repository.UserRepository;
import ch.wiss.m223.security.UserDetailsImpl;

@SpringBootTest
@Transactional
public class MessagePersistenceTest {

    @Autowired
    private GroupController groupController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatGroupRepository chatGroupRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Test
    public void whenMemberSendsMessage_thenPersisted() {
        // arrange: create user and group and membership
        User user = new User("sender", "s@example.com", "pwd");
        user = userRepository.save(user);

        ChatGroup group = new ChatGroup();
        group.setName("Msg Group");
        group = chatGroupRepository.save(group);

        GroupMember member = new GroupMember();
        member.setUser(user);
        member.setGroup(group);
        member.setRole(GroupRole.MEMBER);
        groupMemberRepository.save(member);

        // set authentication principal
        UserDetailsImpl principal = UserDetailsImpl.build(user);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())
        );

        // act: send message via controller
        SendMessageRequest req = new SendMessageRequest();
        req.setContent("Hello DB");

        groupController.sendMessage(group.getId(), req);

        // assert: message present in repository
        var msgs = messageRepository.findByGroupOrderByIdAsc(group);
        assertThat(msgs).isNotEmpty();
        Message m = msgs.get(0);
        assertThat(m.getContent()).isEqualTo("Hello DB");
        assertThat(m.getSender().getId()).isEqualTo(user.getId());
    }
}
