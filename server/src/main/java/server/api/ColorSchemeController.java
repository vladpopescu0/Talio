package server.api;

import commons.ColorScheme;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.database.ColorSchemeRepository;

@RestController
@RequestMapping("api/colors")
public class ColorSchemeController {
    private final ColorSchemeRepository repo;
    private SimpMessagingTemplate msg;

    /**
     * constructor for controller
     * @param repo the used repository
     * @param msg the messaging template
     */
    public ColorSchemeController(ColorSchemeRepository repo,SimpMessagingTemplate msg){
        this.repo = repo;
        this.msg = msg;
    }

    /**
     * Adds a colorScheme if possible
     * @param colorScheme the colorScheme to be added
     * @return ok if the colorScheme is added, a bad request page if the colorScheme is null
     */
    @PostMapping("/add")
    public ResponseEntity<ColorScheme> add(@RequestBody ColorScheme colorScheme) {
        if (colorScheme == null) {
            return ResponseEntity.badRequest().build();
        }
        ColorScheme saved = repo.save(colorScheme);
        msg.convertAndSend("/topic/colors", colorScheme);
        return ResponseEntity.ok(saved);
    }
    /**
     * Updates a Color Scheme
     * @param id the id of the color Scheme to be updated
     * @param colorScheme the new version of the Color Scheme
     * @return a response entity containing the updated color scheme, if the update is possible
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<ColorScheme> updateColorScheme(@PathVariable("id") long id,
                                                         @RequestBody ColorScheme colorScheme) {
        if (!repo.existsById(id) || colorScheme==null) {
            return ResponseEntity.badRequest().build();
        }
        repo.save(colorScheme);
        msg.convertAndSend("/topic/colorsUpdate",colorScheme);
        return ResponseEntity.ok(colorScheme);
    }
}
