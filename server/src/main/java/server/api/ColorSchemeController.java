package server.api;

import commons.ColorScheme;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ColorSchemeRepository;

@RestController
@RequestMapping("api/colors")
public class ColorSchemeController {
    private final ColorSchemeRepository repo;

    /**
     * constructor for controller
     * @param repo the used repository
     */
    public ColorSchemeController(ColorSchemeRepository repo){
        this.repo = repo;
    }

//    /**
//     * get from database by id given in the path
//     * @param id the id of the ColorScheme
//     * @return the colorScheme which was requested or an exception
//     */
//    @GetMapping("/{id}")
//    public ResponseEntity<ColorScheme> getById(@PathVariable("id") long id) {
//        if (id < 0 || !repo.existsById(id)) {
//            return ResponseEntity.badRequest().build();
//        }
//        return ResponseEntity.ok(repo.getById(id));
//    }
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
        return ResponseEntity.ok(saved);
    }
//    /**
//     * Deletes a colorScheme if possible
//     * @param id the id of the colorScheme
//     * @return ok if the colorScheme is deleted, a bad request if the colorScheme is not found
//     */
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<ColorScheme> deleteColorSchemeById(@PathVariable("id") long id) {
//        if (!repo.existsById(id)) {
//            return ResponseEntity.badRequest().build();
//        }
//        ColorScheme deleted = repo.getById(id);
//        repo.deleteById(id);
//        return ResponseEntity.ok(deleted);
//    }
}