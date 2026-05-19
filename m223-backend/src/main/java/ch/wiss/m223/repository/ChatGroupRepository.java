package ch.wiss.m223.repository;

import ch.wiss.m223.model.ChatGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatGroupRepository 
        extends JpaRepository<ChatGroup, Long> {
}