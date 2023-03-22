package server.api;


import commons.Card;
import commons.CardList;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.database.CardListRepository;
import server.database.CardRepository;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {
    private final CardRepository repo;
    private final CardListRepository clRepo;

    private final SimpMessagingTemplate msgs;

    /**
     * Constructor for the CardController
     * @param repo the Card repository to be used
     * @param clRepo the Card List repository to be used
     */
    public CardController(CardRepository repo, CardListRepository clRepo, SimpMessagingTemplate msgs){
        this.repo = repo;
        this.clRepo = clRepo;
        this.msgs = msgs;
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
     * @param id the id of the searched card
     * @return a ResponseEntity verifying the card is found
     */
    @GetMapping("/addlist/{id}")
    public ResponseEntity<List<Card>> getCardsByListId(@PathVariable("id") long id){
        if(id<0 || !repo.existsById(id)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(null);
    }

    /**
     * Adds a card if possible
     * @param card the card to be added
     * @return ok if the card is added, a bad request page if the card has empty fields
     */
    @PostMapping("/add")
    public ResponseEntity<Card> add(@RequestBody Card card) {
        if (isNullOrEmpty(card.getName())) {
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
     * Updates the parent CardList of a Card with provided ID
     * @param id ID of the Card to be updated
     * @param lists old and new CardList of the provided Card
     */
    @PutMapping("/updateParent/{id}")
    public void updateParent(@PathVariable("id") Long id, @RequestBody List<CardList> lists) {
        if (lists == null || !repo.existsById(id) || lists.size() < 2
                || !clRepo.existsById(lists.get(0).getId())
                || !clRepo.existsById(lists.get(1).getId())) {
            return;
        }

        Card card = repo.getById(id);
        CardList oldParent = clRepo.getById(lists.get(0).getId());
        CardList newParent = clRepo.getById(lists.get(1).getId());
        for(int x = 0; x < oldParent.getCards().size(); x++) {
            if (oldParent.getCards().get(x).getId() == id) {
                oldParent.getCards().remove(x);
                break;
            }
        }
        newParent.addCard(card);
        lists.set(0, oldParent);
        lists.set(1, newParent);
        msgs.convertAndSend("/topic/updateParent", id);
        clRepo.saveAll(lists);
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
     */
    //Maybe you mean removeCard??
    @DeleteMapping(path = "/delete/{id}")
    public void removeCard(@PathVariable("id") long id){
        repo.deleteById(id);
    }

}
