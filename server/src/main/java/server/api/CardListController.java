package server.api;

import commons.Card;
import commons.CardList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.CardListService;

import java.util.List;

@RestController
@RequestMapping(path = "api/lists")
public class CardListController {
    private final CardListService CLService;

    public CardListController(CardListService CLService){
        this.CLService = CLService;
    }


    /**
     * @return all the CardList objects on the server
     */
    @GetMapping(path = { "", "/" })
    @SuppressWarnings("unused")
    public List<CardList> getAll() {
        return CLService.getAll();
    }

    /**
     * @param id id of the CardList to be retrieved
     * @return a ResponseEntity with the status OK and the value of the CardList if the CardList with the searched id is found, else a ResponseEntity with the BAD_REQUEST status
     */
    @GetMapping("/{id}")
    @SuppressWarnings("unused")
    public ResponseEntity<CardList> getById(@PathVariable("id") long id) {
        CardList list = CLService.getById(id);
        if(list == null){
            System.out.println("this is null");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(list);
    }

    @PostMapping("/addCard/{id}")
    public ResponseEntity<Card> addCardToList(@PathVariable("id") long id,@RequestBody Card card){
        if(card==null){
            System.out.println("this is null");
            return ResponseEntity.badRequest().build();
        }
        System.out.println(card +"this is card");
        CLService.addCard(id,card);
        return ResponseEntity.ok(card);
    }

    /**
     * @param list the list that is posted
     * @return a ResponseEntity with the status OK and the value of the CardList if the addition is successful, else a ResponseEntity with the BAD_REQUEST status
     */
    @PostMapping(path = "/add")
    public ResponseEntity<CardList> add(@RequestBody CardList list) {
//        if (list == null || isNullOrEmpty(list.getName())){
//            return ResponseEntity.badRequest().build();
//        }
//
//        Quote saved = repo.save(quote);
//        return ResponseEntity.ok(saved);
        CardList addedList = CLService.add(list);
        if(addedList == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(addedList);
    }


    /**
     * @param id the id of the list that is deleted
     * @return a ResponseEntity with the status OK if the deletion is successful, else a ResponseEntity with the BAD_REQUEST status
     */
    @DeleteMapping(path = "/delete/{id}")
    @SuppressWarnings("unused")
    public ResponseEntity<CardList> removeList(@PathVariable("id") long id){
        if(!CLService.delete(id)){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/{id}")
    @SuppressWarnings("unused")
    public ResponseEntity<CardList> modifyName(@PathVariable("id") long id, @RequestBody String name){
        if(!CLService.changeName(CLService.getById(id),name)){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }


    }
