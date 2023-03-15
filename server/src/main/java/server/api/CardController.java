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
        //I would like to have a query to get the list but i
        //do not think it is really necessary and should
        //put the list in the frontend
        if(cardList==null || !repo.existsById(id)){
            return ResponseEntity.badRequest().build();
        }

        Card updatedCard = repo.getById(id);
        updatedCard.setList(cardList);
        return ResponseEntity.ok(updatedCard);
    }


    /**
     * Removes a card from a list(????)
     * @param id the id of the card
     */
    //Maybe you mean removeCard??
    @DeleteMapping(path = "/delete/{id}")
    public void removeCard(@PathVariable("id") long id){
        repo.deleteById(id);
    }

}
