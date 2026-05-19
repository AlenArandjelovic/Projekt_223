package ch.wiss.m223.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.wiss.m223.model.ChatGroup;
import ch.wiss.m223.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByGroupOrderByIdAsc(ChatGroup group);

    void deleteByGroup(ChatGroup group);
}