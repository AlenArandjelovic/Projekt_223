package ch.wiss.m223.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.wiss.m223.model.Item;



public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByOwnerId(Long ownerId);
}
