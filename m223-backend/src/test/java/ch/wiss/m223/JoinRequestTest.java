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
import ch.wiss.m223.model.User;
import ch.wiss.m223.repository.ChatGroupRepository;
import ch.wiss.m223.repository.GroupMemberRepository;
import ch.wiss.m223.repository.UserRepository;
import ch.wiss.m223.security.UserDetailsImpl;

@SpringBootTest
@Transactional
public class JoinRequestTest {

    @Autowired
    private GroupController groupController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatGroupRepository chatGroupRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Test
    public void whenUserJoinsGroup_thenMembershipSaved() {
        // arrange: create user and group
        User user = new User("tester", "tester@example.com", "pwd");
        user = userRepository.save(user);

        ChatGroup group = new ChatGroup();
        group.setName("Test Group");
        group = chatGroupRepository.save(group);

        // set authentication principal
        UserDetailsImpl principal = UserDetailsImpl.build(user);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())
        );

        // precondition
        assertThat(groupMemberRepository.existsByUserIdAndGroupId(user.getId(), group.getId())).isFalse();

        // act
        groupController.joinGroup(group.getId());

        // assert: membership present with role MEMBER
        boolean exists = groupMemberRepository.existsByUserIdAndGroupId(user.getId(), group.getId());
        assertThat(exists).isTrue();

        GroupMember membership = groupMemberRepository.findByUserIdAndGroupId(user.getId(), group.getId());
        assertThat(membership).isNotNull();
        assertThat(membership.getRole()).isEqualTo(GroupRole.MEMBER);
    }
}
