package ch.wiss.m223.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.wiss.m223.model.ChatGroup;
import ch.wiss.m223.model.GroupMember;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    List<GroupMember> findByUserId(Long userId);

    GroupMember findByUserIdAndGroupId(Long userId, Long groupId);

    boolean existsByUserIdAndGroupId(Long userId, Long groupId);

    void deleteByGroup(ChatGroup group);
}