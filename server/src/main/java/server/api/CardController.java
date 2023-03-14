package server.api;


import commons.Card;
import commons.CardList;
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

    @PostMapping("/add")
    public ResponseEntity<Card> add(@RequestBody Card card) {
        System.out.println(card + "\nthis was BEFORE card should add\n");
        if (isNullOrEmpty(card.getName()) || card.getCardList()==null) {
            return ResponseEntity.badRequest().build();
        }
        System.out.println(card + "\nthis was card should add\n");
        Card saved = repo.save(card);
        return ResponseEntity.ok(saved);
    }

    /**
     * Changes the parent list of a card, could be used when dragged and dropped
     * @param id id of card that is changed
     * @param cardList new parent card list
     * @return response of request
     */
    @PutMapping("/updateParent/{id}")
    public ResponseEntity<Card> updateParent(@PathVariable("id") long id, CardList cardList){
        //I would like to have a query to get the list but i do not think it is really necessary and should
        //put the list in the frontend
        if(cardList==null || !repo.existsById(id)){
            return ResponseEntity.badRequest().build();
        }

        Card updatedCard = repo.getById(id);
        updatedCard.setCardList(cardList);
        return ResponseEntity.ok(updatedCard);
    }


    @DeleteMapping(path = "/delete/{id}")
    public void removeCard(@PathVariable("id") long id){
        repo.deleteById(id);
    }

}
