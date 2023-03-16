package server.api;


import commons.Card;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.CardRepository;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {
    private final CardRepository repo;

    public CardController(CardRepository repo){
        this.repo = repo;
    }
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    @GetMapping(path = { "", "/" })
    public List<Card> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    @GetMapping("/addlist/{id}")
    public ResponseEntity<List<Card>> getCardsByListId(@PathVariable("id") long id){
        if(id<0 || !repo.existsById(id)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(null);
    }

    @PostMapping("/add")
    public ResponseEntity<Card> add(@RequestBody Card card) {
        System.out.println(card + "\nthis was BEFORE card should add\n");
        if (isNullOrEmpty(card.getName())) {
            return ResponseEntity.badRequest().build();
        }
        System.out.println(card + "\nthis was card should add\n");
        Card saved = repo.save(card);
        return ResponseEntity.ok(saved);
    }
    @PutMapping(path = "/{id}")
    @SuppressWarnings("unused")
    public ResponseEntity<Card> modifyName(@PathVariable("id") long id, @RequestBody String name){
        if(!repo.existsById(id)){
            return ResponseEntity.badRequest().build();
        }
        Card newChangedCard = repo.getById(id);
        newChangedCard.setName(name);
        repo.save(newChangedCard);
        return ResponseEntity.ok().build();
    }

    /**
     * Changes the parent list of a card, could be used when dragged and dropped
     * @param id id of card that is changed
     * @return response of request
     */

    @DeleteMapping(path = "/delete/{id}")
    public void removeCard(@PathVariable("id") long id){
        repo.deleteById(id);
    }

}
