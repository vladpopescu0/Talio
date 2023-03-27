package server.api;

import commons.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.TaskRepository;

import java.util.List;

@RestController
@RequestMapping("api/tasks")
public class TaskController {
    private final TaskRepository repo;

    /**
     * Constructor for the task controller
     * @param repo the task repository used
     */
    public TaskController(TaskRepository repo) {
        this.repo = repo;
    }

    /**
     * Gets all tasks in the repo
     * @return a list containing all tasks
     */
    @GetMapping(path = {"", "/"})
    public List<Task> getAll() { return repo.findAll(); }

    /**
     * Gets a task with a specified id
     * @param id the id of the task
     * @return a response entity containing the task, if that exists
     */
    @GetMapping(path =  "/{id}")
    public ResponseEntity<Task> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * Adds a new task
     * @param task the task to be added
     * @return a response entity containing the added task
     */
    @PostMapping("/add")
    public ResponseEntity<Task> add(@RequestBody Task task) {
        if (task == null || task.getTitle() == null ||
                task.getTitle().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Task saved = repo.save(task);
        return ResponseEntity.ok(saved);
    }

    /**
     * Updates a task
     * @param id the id of the task
     * @param task the updated task
     * @return a response entity with the updated task
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable("id") long id,
                                           @RequestBody Task task) {
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        repo.save(task);
        return ResponseEntity.ok(task);
    }

    /**
     * Removes a task from the repository
     * @param id the id of the task
     * @return a response entity containing the removed task
     */
    @DeleteMapping(path = "delete/{id}")
    public ResponseEntity<Task> removeTask(@PathVariable("id") long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Task t = repo.findById(id).get();
        repo.deleteById(id);
        return ResponseEntity.ok(t);
    }
}
