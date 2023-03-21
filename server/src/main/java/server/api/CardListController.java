package server.api;

import commons.Card;
import commons.CardList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.CardRepository;
import server.services.CardListService;

import java.util.List;

@RestController
@RequestMapping(path = "api/lists")
public class CardListController {

    private final CardRepository repo;
    private final CardListService cLService;

    /**
     * Constructor for the CardListController
     * @param cLService the service that is used
     * @param repo Card Repository
     */
    public CardListController(CardListService cLService, CardRepository repo){
        this.cLService = cLService;
        this.repo = repo;
    }


    /**
     * @return all the CardList objects on the server
     */
    @GetMapping(path = { "", "/all" })
    @SuppressWarnings("unused")
    public ResponseEntity<List<CardList>> getAll() {
        List<CardList> list = cLService.getAll();
        if(list == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(list);
    }

    /**
     * @param id id of the CardList to be retrieved
     * @return a ResponseEntity with the status OK
     * and the value of the CardList
     * if the CardList with the searched id is found,
     * else a ResponseEntity with the BAD_REQUEST status
     */
    @GetMapping("/{id}")
    public ResponseEntity<CardList> getById(@PathVariable("id") long id) {
        CardList list = cLService.getById(id);
        if(list == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(list);
    }

    /**
     * @param id id of the cardlist
     * @param card the card that needs to be added
     * @return a ResponseEntity verifying the card is added to the list
     */
    @PostMapping("/addCard/{id}")
    public ResponseEntity<Card> addCardToList(@PathVariable("id") long id,@RequestBody Card card){
        if(card==null){
            return ResponseEntity.badRequest().build();
        }
        cLService.addCard(id,card);
        return ResponseEntity.ok(card);
    }

    /**
     * @param list the list that is posted
     * @return a ResponseEntity with the status OK and the value of the CardList
     * if the addition is successful, else a ResponseEntity with the BAD_REQUEST status
     */
    @PostMapping(path = "/add")
    public ResponseEntity<CardList> add(@RequestBody CardList list) {
        CardList addedList = cLService.add(list);
        if(addedList == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(addedList);
    }


    /**
     * @param id the id of the list that is deleted
     * @return a ResponseEntity with the status OK if the deletion is successful,
     * else a ResponseEntity with the BAD_REQUEST status
     */
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<CardList> removeList(@PathVariable("id") long id){
        if(!cLService.delete(id)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Modifies the name of a cardList
     * @param id the id of the list to be renamed
     * @param name the new name
     * @return ok if the modification goes through, false otherwise
     */
    @PutMapping(path = "/{id}")
    @SuppressWarnings("unused")
    public ResponseEntity<CardList> modifyName(@PathVariable("id") long id,
                                               @RequestBody String name){
        CardList cl = cLService.changeName(cLService.getById(id),name);
        if(cl == null){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(cl);
    }

    /**
     * Moves the second given Card in front of the first given Card in CardList of provided ID
     * @param id ID of the CardList to be updated
     * @param cards two Cards to be moved
     */
    @PutMapping("/moveCard/{id}")
    public void moveCard(@PathVariable("id") long id, @RequestBody List<Card> cards) {
        if (cards == null || !cLService.exists(id) || cards.size() < 2
                || !repo.existsById(cards.get(0).getId())
                || !repo.existsById(cards.get(1).getId())) {
            return;
        }

        CardList cl = cLService.getById(id);
        Card origin = cards.get(0);
        Card destination = cards.get(1);
        int originIndex = 0;
        for(int x = 0; x < cl.getCards().size(); x++) {
            if (cl.getCards().get(x).getId() == origin.getId()) {
                originIndex = x;
                break;
            }
        }
        for(int x = 0; x < cl.getCards().size(); x++) {
            if (cl.getCards().get(x).getId() == destination.getId()) {
                Card replaced = cl.getCards().remove(originIndex);
                cl.getCards().add(x, replaced);
                break;
            }
        }
        cLService.save(cl);
    }
}

