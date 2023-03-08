package server.api;

import commons.CardList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import server.database.CardListRepository;
import server.services.CardListService;

@RestController
@RequestMapping(path = "/api/lists")
public class CardListController {
    private final CardListRepository repo;

    private CardListService CLService; // im not sure if we should use that

    public CardListController(CardListRepository repo){
        this.repo = repo;
    }
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    @GetMapping(path = { "", "/" })
    public List<CardList> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardList> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    @PostMapping(path = { "", "/" })
    public ResponseEntity<CardList> add(@RequestBody CardList list) {

        if (isNullOrEmpty(list.getName()) || list.getCards() == null || list.getCards().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        CardList saved = repo.save(list);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping(path = "/delete/{id}")
    public void removeList(@PathVariable("id") long id){
        repo.deleteById(id);
    }

}
