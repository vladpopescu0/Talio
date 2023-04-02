package server.api;

import commons.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import server.database.CardRepository;
import server.database.TagRepository;

import java.util.List;

@RestController
@RequestMapping(path = "api/tags")
public class TagController {
    private final TagRepository repo;
    private final CardRepository cardRepo;

    private final SimpMessagingTemplate msgs;

    /**
     * Constructor for the TagController
     * @param repo the repository that is used
     * @param cardRepo Card Repository
     * @param msgs messaging template
     */
    public TagController(TagRepository repo, CardRepository cardRepo, SimpMessagingTemplate msgs){
        this.repo = repo;
        this.cardRepo = cardRepo;
        this.msgs = msgs;
    }


    /**
     * @return all the Tag objects on the server
     */
    @GetMapping(path = { "", "/all" })
    @SuppressWarnings("unused")
    public List<Tag> getAll() {
        return repo.findAll();
    }

    /**
     * @param id id of the Tag to be retrieved
     * @return a ResponseEntity with the status OK
     * and the value of the Tag
     * if the Tag with the searched id is found,
     * else a ResponseEntity with the BAD_REQUEST status
     */
    @GetMapping("/{id}")
    public ResponseEntity<Tag> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * @param tag the Tag that is posted
     * @return a ResponseEntity with the status OK and the value of the Tag
     * if the addition is successful, else a ResponseEntity with the BAD_REQUEST status
     */
    @PostMapping(path = "/add")
    public ResponseEntity<Tag> add(@RequestBody Tag tag) {
        if (tag == null || tag.getName() == null
                || tag.getName().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Tag saved = repo.save(tag);
        return ResponseEntity.ok(saved);
    }

    /**
     * @param id ID of the Tag to be deleted
     * @return a ResponseEntity with the status OK if the deletion is successful,
     * else a ResponseEntity with the BAD_REQUEST status
     */
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Tag> removeTag(@PathVariable("id") long id){
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Tag tag = repo.getById(id);
        cardRepo.findByTags_Id(id).forEach(c -> {
            c.removeTag(tag);
            cardRepo.save(c);
        });
        repo.deleteById(id);
        msgs.convertAndSend("/topic/tags2", id);
        return ResponseEntity.ok(tag);
    }

    /**
     * Modifies the Tag of the given ID to the given Tag
     * @param id the id of the Tag to be modified
     * @param tag Tag that the target Tag will be modified to
     * @return OK if the modification is successful, false otherwise
     */
    @PutMapping(path = "/{id}")
    public ResponseEntity<Tag> modifyTag(@PathVariable("id") long id,
                                               @RequestBody Tag tag){
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Tag newTag = repo.getById(id);
        newTag.setName(tag.getName());
        newTag.setColor(tag.getColor());
        repo.save(newTag);
        msgs.convertAndSend("/topic/tags2", id);
        return ResponseEntity.ok(newTag);
    }
}