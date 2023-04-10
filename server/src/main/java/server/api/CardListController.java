package server.api;

import commons.Card;
import commons.CardList;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.database.CardListRepository;

import server.database.CardRepository;

import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        if (list == null || list.getName() == null
                || list.getName().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        CardList saved = repo.save(list);
        return ResponseEntity.ok(saved);
    }


    /**
     * @param id the id of the list that is deleted
     * @return a ResponseEntity with the status OK if the deletion is successful,
     * else a ResponseEntity with the BAD_REQUEST status
     */
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<CardList> removeList(@PathVariable("id") long id){
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

    /**
     * Moves a Card with the second given ID in front of a Card with the first given ID
     * @param ids IDs of the Cards to be moved from and moved to
     * @return Response entity stating whether the operation was successful
     */
    @PutMapping(path = "/moveCard")
    public ResponseEntity<Boolean> moveCard(@RequestBody List<Long> ids) {
        if (ids == null || ids.size() != 2 || !cardRepository.existsById(ids.get(0))
                || !cardRepository.existsById(ids.get(1)) || !repo.existsByCards_Id(ids.get(0))
                || !repo.existsByCards_Id(ids.get(1))) {
            return ResponseEntity.badRequest().build();
        }

        CardList originCardList = repo.findByCards_Id(ids.get(0));
        CardList destinationCardList = repo.findByCards_Id(ids.get(1));
        Card origin = cardRepository.getById(ids.get(0));
        Card destination = cardRepository.getById(ids.get(1));
        OptionalInt index = IntStream.range(0, originCardList.getCards().size())
                .filter(x -> origin.getId() == originCardList.getCards().get(x).getId())
                .findFirst();

        if (index.isPresent()) {
            int originIndex = index.getAsInt();
            if (Objects.equals(originCardList.getId(), destinationCardList.getId())) {
                index = IntStream.range(0, originCardList.getCards().size())
                        .filter(x -> destination.getId()
                                == originCardList.getCards().get(x).getId())
                        .findFirst();
                if (index.isPresent()) {
                    Card replaced = originCardList.getCards().remove(originIndex);
                    originCardList.getCards().add(index.getAsInt(), replaced);
                    repo.save(originCardList);
                }
            } else {
                index = IntStream.range(0, destinationCardList.getCards().size())
                        .filter(x -> destination.getId()
                                == destinationCardList.getCards().get(x).getId())
                        .findFirst();
                if (index.isPresent()) {
                    Card replaced = originCardList.getCards().remove(originIndex);
                    destinationCardList.getCards().add(index.getAsInt(), replaced);
                    repo.save(originCardList);
                    repo.save(destinationCardList);
                }
            }
        }
        msgs.convertAndSend("/topic/moveCard", ids);
        return ResponseEntity.ok(true);
    }

    /**
     * Moves a Card with the given ID to the end of a CardList with the given ID
     * @param id ID of the CardList
     * @param cardId ID of the Card
     * @return Response entity stating whether the operation was successful
     */
    @PutMapping(path = "/moveToCardList/{id}")
    public ResponseEntity<Boolean> moveCardToCardList(@PathVariable("id") long id,
                                                      @RequestBody long cardId) {
        if (!repo.existsById(id) || !cardRepository.existsById(cardId) ||
                !repo.existsByCards_Id(cardId)) {
            return ResponseEntity.badRequest().build();
        }

        CardList originCardList = repo.findByCards_Id(cardId);
        CardList destinationCardList = repo.getById(id);

        OptionalInt index = IntStream.range(0, originCardList.getCards().size())
                .filter(x -> cardId == originCardList.getCards().get(x).getId())
                .findFirst();
        if (index.isPresent()) {
            Card replaced = originCardList.getCards().remove(index.getAsInt());
            destinationCardList.getCards().add(replaced);
            repo.save(originCardList);
            repo.save(destinationCardList);
        }

        repo.save(originCardList);
        repo.save(destinationCardList);
        msgs.convertAndSend("/topic/moveToCardList", cardId);
        return ResponseEntity.ok(true);
    }
}

