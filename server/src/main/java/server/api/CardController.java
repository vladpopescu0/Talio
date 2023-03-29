package server.api;


import commons.Card;
import commons.CardList;
import commons.Tag;
import commons.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.database.CardListRepository;
import server.database.CardRepository;
import server.database.TagRepository;
import server.database.TaskRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cards")
public class CardController {
    private final CardRepository repo;
    private final CardListRepository clRepo;
    private final TagRepository tagRepo;

    private final SimpMessagingTemplate msgs;
    private final TaskRepository taskRepo;

    /**
     * Constructor for the CardController
     * @param repo the Card repository to be used
     * @param clRepo the Card List repository to be used
     * @param msgs the messaging template
     * @param taskRepo the repository containing all tasks
     * @param tagRepo the repository containing all Tags
     */
    public CardController(CardRepository repo,
                          CardListRepository clRepo, SimpMessagingTemplate msgs,
                          TaskRepository taskRepo, TagRepository tagRepo){
        this.repo = repo;
        this.clRepo = clRepo;
        this.msgs = msgs;
        this.taskRepo = taskRepo;
        this.tagRepo = tagRepo;
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
    public ResponseEntity<Card> modifyName(@PathVariable("id") long id, @RequestBody String name){
        if(!repo.existsById(id)){
            return ResponseEntity.badRequest().build();
        }
        Card newChangedCard = repo.findById(id).get();
        newChangedCard.setName(name);
        repo.save(newChangedCard);
        msgs.convertAndSend("/topic/boardsRenameDeleteAdd", id);
        return ResponseEntity.ok(newChangedCard);
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
        clRepo.saveAll(lists);
        msgs.convertAndSend("/topic/updateParent", id);
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
     * Removes a card from the database
     * @param id the id of the card
     * @return the removed card
     */
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Card> removeCard(@PathVariable("id") long id){
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Card c = repo.findById(id).get();
        repo.deleteById(id);
        return ResponseEntity.ok(c);
    }

    /**
     * Updates the details of the card
     * @param id the id of the card
     * @param card the card after changes
     * @return a response entity containing the new card
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Card> updateCardDetails(@PathVariable("id") long id,
                                                  @RequestBody Card card) {
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        repo.save(card);
        return ResponseEntity.ok(card);
    }

    /**
     * Deletes a task from a card
     * @param id the id of the card
     * @param taskId the id of the task
     * @return 200 -OK if the task was successfully deleted
     */
    @DeleteMapping(path = "/{id}/delete/{taskId}")
    public ResponseEntity<Card> deleteTaskFromCard (
            @PathVariable("id") long id, @PathVariable("taskId") long taskId) {
        if (repo.existsById(id)) {
            Card card = repo.findById(id).get();
            var filteredTasks = card.getTasks().stream()
                    .filter(task -> task.getId() != taskId).collect(Collectors.toList());
            card.setTasks(filteredTasks);
            repo.save(card);
            return ResponseEntity.ok(repo.getById(id));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Adds a task to a card
     * @param id the id of the card
     * @param task the task to be added
     * @return a response entity containing the task
     */
    @PostMapping("addTask/{id}")
    public ResponseEntity<Task> addTaskToCard(@PathVariable("id") long id,
                                              @RequestBody Task task) {
        if (task == null || task.getTitle() == null
                || task.getTitle().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Card card = repo.getById(id);
        card.addTask(task);
        repo.save(card);
        return ResponseEntity.ok(task);
    }

    /**
     * Adds the Tags from the list to the specified Card
     * @param id ID of the Card
     * @param tags list of Tags to be added to the Card
     * @return updated Card
     */
    @PostMapping("addTags/{id}")
    public ResponseEntity<Card> addTags(@PathVariable("id") long id,
                                        @RequestBody List<Tag> tags) {
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        if (tags == null || tags.size() == 0) {
            return ResponseEntity.badRequest().build();
        } else {
            for(Tag t: tags) {
                if (!tagRepo.existsById(t.getId())) {
                    return ResponseEntity.badRequest().build();
                }
            }
        }

        Card card = repo.getById(id);
        for(Tag t: tags) {
            card.addTag(t);
        }
        repo.save(card);
        return ResponseEntity.ok(card);
    }

    /**
     * Removes the Tag from the list of the specified Card
     * @param id ID of the Card
     * @param tag Tag to be removed from the Card
     * @return updated Card
     */
    @PutMapping("removeTag/{id}")
    public ResponseEntity<Card> removeTag(@PathVariable("id") long id,
                                          @RequestBody Tag tag) {
        if (!repo.existsById(id) || tag == null || !tagRepo.existsById(tag.getId())) {
            return ResponseEntity.badRequest().build();
        }

        Card card = repo.getById(id);
        card.removeTag(tag);
        repo.save(card);
        return ResponseEntity.ok(card);
    }

    /*/**
     * Returns the CardList which is the parent of the Card of given ID
     * @param id ID of the Card the parent of which is to be looked for
     * @return the CardList which is the parent of the Card of given ID
     */
    /*@GetMapping(path = "/getParent/{id}")
    public ResponseEntity<CardList> getParentById(@PathVariable("id") long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(new CardList());
    }*/

}
