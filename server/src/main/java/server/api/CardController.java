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

    /**
     * Constructor for the CardController
     * @param repo the repository to be used
     */
    public CardController(CardRepository repo){
        this.repo = repo;
    }

    /**
     * Checks whether a string is null or empty
     * @param s the string in test
     * @return true if s == null or s == ''
     */
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * Gets all cards
     * @return a list of all cards
     */
    @GetMapping(path = { "", "/" })
    public List<Card> getAll() {
        return repo.findAll();
    }

    /**
     * Gets a card with a specific id
     * @param id the id in search
     * @return the card if existent, a badRequest page otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Card> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * Adds a card if possible
     * @param card the card to be added
     * @return ok if the card is added, a bad request page if the card has empty fields
     */
    @PostMapping("/add")
    public ResponseEntity<Card> add(@RequestBody Card card) {
        if (card == null || isNullOrEmpty(card.getName())) {
            return ResponseEntity.badRequest().build();
        }
        Card saved = repo.save(card);
        return ResponseEntity.ok(saved);
    }

    /**
     * @param id the id of the searched card
     * @param name name that needs to be substituted
     * @return a ResponseEntity verifying the card's name is changed
     */
    @PutMapping(path = "/{id}")
    @SuppressWarnings("unused")
    public ResponseEntity<Card> modifyName(@PathVariable("id") long id, @RequestBody String name){
        if(!repo.existsById(id)){
            return ResponseEntity.badRequest().build();
        }
        Card newChangedCard = repo.getById(id);
        newChangedCard.setName(name);
        repo.save(newChangedCard);
        return ResponseEntity.ok(newChangedCard);
    }


//     /**
//     * Changes the parent list of a card, could be used when dragged and dropped
//     * @param id id of card that is changed
//     * @return response of request
//     */
//    @PutMapping("/updateParent/{id}")
//    public ResponseEntity<Card> updateParent(@PathVariable("id") long id, CardList cardList){
//        //I would like to have a query to get the list but i
//        //do not think it is really necessary and should
//        //put the list in the frontend
//        if(cardList==null || !repo.existsById(id)){
//            return ResponseEntity.badRequest().build();
//        }
//
//        Card updatedCard = repo.getById(id);
//        updatedCard.setList(cardList);
//        return ResponseEntity.ok(updatedCard);
//    }


    /**
     * Removes a card from a list(????)
     * @param id the id of the card
     * @return the removed card
     */
    //Maybe you mean removeCard??
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Card> removeCard(@PathVariable("id") long id){
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Card c = repo.findById(id).get();
        repo.deleteById(id);
        return ResponseEntity.ok(c);
    }

}
