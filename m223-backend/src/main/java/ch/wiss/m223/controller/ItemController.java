package ch.wiss.m223.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.wiss.m223.model.Item;
import ch.wiss.m223.repository.ItemRepository;
import ch.wiss.m223.repository.UserRepository;
import ch.wiss.m223.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemRepository repo;
    private final UserRepository userRepo;

    public ItemController(ItemRepository repo,UserRepository userRepo ) {
        this.repo = repo;
        this.userRepo=userRepo;
    }

    @PostMapping
    public Item addItem(@RequestBody Item item,
                        @AuthenticationPrincipal UserDetailsImpl user) {

        item.setOwner(null); // 🔥 WICHTIG
        return repo.save(item);
    }

    @GetMapping
    public List<Item> getMyItems(@AuthenticationPrincipal UserDetailsImpl user) {
        return repo.findByOwnerId(user.getId());
    }

    @PutMapping("/{id}")
    public Item updateItem(@PathVariable Integer id,
                           @RequestBody Item updatedItem,
                           @AuthenticationPrincipal UserDetailsImpl user) {

        Item item = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        // 🔥 SECURITY CHECK
        if (!item.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("Forbidden");
        }

        item.setTitel(updatedItem.getTitel());
        item.setDescription(updatedItem.getDescription());

        return repo.save(item);
    }
}
