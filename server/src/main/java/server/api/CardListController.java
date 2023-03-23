package server.api;

import commons.Card;
import commons.CardList;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.database.CardListRepository;

import server.database.CardRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/lists")
public class CardListController {
    private final CardRepository cardRepository;
    private final SimpMessagingTemplate msgs;

    private final CardListRepository repo;

    /**
     * Constructor for the CardListController
     * @param repo the repository that is used
     * @param msgs simpmsgstemplate
     * @param cardRepository the card repository
     */
    public CardListController(CardListRepository repo,
                              CardRepository cardRepository, SimpMessagingTemplate msgs){
        this.repo = repo;
        this.msgs = msgs;
        this.cardRepository = cardRepository;
    }
    /**
     * @return all the CardList objects on the server
     */
    @GetMapping(path = { "", "/all" })
    @SuppressWarnings("unused")
    public List<CardList> getAll() {
        return repo.findAll();
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
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * @param id id of the cardlist
     * @param card the card that needs to be added
     * @return a ResponseEntity verifying the card is added to the list
     */
    @PostMapping("/addCard/{id}")
    public ResponseEntity<Card> addCardToList(@PathVariable("id") long id,@RequestBody Card card){
        //if(card==null){
        //    return ResponseEntity.badRequest().build();
        //}
        //cLService.addCard(id,card);
        //return ResponseEntity.ok(card);
        if (card == null || card.getName() == null
                || card.getName().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        CardList cl = getById(id).getBody();
        cl.addCard(card);
        repo.save(cl);
        msgs.convertAndSend("/topic/boardsRenameDeleteAdd", id);
        return ResponseEntity.ok(card);
    }

    /**
     * @param list the list that is posted
     * @return a ResponseEntity with the status OK and the value of the CardList
     * if the addition is successful, else a ResponseEntity with the BAD_REQUEST status
     */
    @PostMapping(path = "/add")
    public ResponseEntity<CardList> add(@RequestBody CardList list) {
        //CardList addedList = cLService.add(list);
        //if(addedList == null){
        //    return ResponseEntity.badRequest().build();
        //}
        //return ResponseEntity.ok(addedList);
        if (list == null || list.getName() == null
                || list.getName().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        CardList saved = repo.save(list);
        msgs.convertAndSend("/topic/lists", list);
        return ResponseEntity.ok(saved);
    }

//    private static Map<Object, Consumer<CardList>> listeners = new HashMap<>();
//    @GetMapping(path = {"/updates"})
//    public DeferredResult<ResponseEntity<CardList>> getCLUpdates(){
//        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//        var res = new DeferredResult<ResponseEntity<CardList>>(5000L,noContent);
//
//        var key = new Object();
//        listeners.put(key,q ->
//            res.setResult(ResponseEntity.ok(q))
//        );
//        res.onCompletion(() -> listeners.remove(key));
//
//        return res;
//    }


    /**
     * @param id the id of the list that is deleted
     * @return a ResponseEntity with the status OK if the deletion is successful,
     * else a ResponseEntity with the BAD_REQUEST status
     */
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<CardList> removeList(@PathVariable("id") long id){
        //if(!cLService.delete(id)){
        //    return ResponseEntity.badRequest().build();
        //}
        //return ResponseEntity.ok().build();
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        CardList list = repo.getById(id);
        repo.deleteById(id);
        msgs.convertAndSend("/topic/boardsRenameDeleteAdd", id);
        return ResponseEntity.ok(list);
    }

    /**
     * Modifies the name of a cardList
     * @param id the id of the list to be renamed
     * @param name the new name
     * @return ok if the modification goes through, false otherwise
     */
    @PutMapping(path = "/{id}")
    public ResponseEntity<CardList> modifyName(@PathVariable("id") long id,
                                               @RequestBody String name){
        //CardList cl = cLService.changeName(cLService.getById(id),name);
        //if(cl == null){
        //    return ResponseEntity.badRequest().build();
        //}

        //return ResponseEntity.ok(cl);
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        CardList newList = repo.findById(id).get();;
        newList.setName(name);
        repo.save(newList);
        msgs.convertAndSend("/topic/boardsRenameDeleteAdd", id);
        return ResponseEntity.ok(newList);
    }

    /**
     * Moves the second given Card in front of the first given Card in CardList of provided ID
     * @param id ID of the CardList to be updated
     * @param cards two Cards to be moved
     * @return the new cardList
     */
    @PutMapping("/moveCard/{id}")
    public ResponseEntity<CardList> moveCard(@PathVariable("id") long id,
                                             @RequestBody List<Card> cards) {
        if (cards == null || !repo.existsById(id) || cards.size() < 2
                || !repo.existsById(cards.get(0).getId())
                || !cardRepository.existsById(cards.get(1).getId())) {
            return ResponseEntity.badRequest().build();
        }

        CardList cl = repo.getById(id);
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
        repo.save(cl);
        msgs.convertAndSend("/topic/updateParent", id);
        return ResponseEntity.ok(cl);
    }

    /**
     * The mapping of the delete card from list
     * @param id of the list from which to delete the card
     * @param cardId the id of the card that should be deleted
     * @return a response with a card list object and 1) 404 if not found
     * 2) 200 if the card was properly deleted
     */
    @DeleteMapping(path = "/{id}/delete/{cardId}")
    public ResponseEntity<CardList> deleteCardFromList
    (@PathVariable("id") long id, @PathVariable("cardId") long cardId) {
        if (repo.existsById(id)) {
            CardList cardList = repo.getById(id);
            var filteredList = cardList.getCards().stream()
                    .filter(card -> card.getId() != cardId).collect(Collectors.toList());
            if (cardList.getCards().size() == filteredList.size()) {
                return ResponseEntity.notFound().build();
            }
            cardList.setCards(filteredList);
            repo.save(cardList);
            msgs.convertAndSend("/topic/boardsRenameDeleteAdd", id);
            return ResponseEntity.ok(repo.getById(id));
        }
        return ResponseEntity.notFound().build();
    }
}

