package server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public class GenericService<T> {

    final JpaRepository<T, Long> repo;

    @Autowired
    public GenericService(JpaRepository<T, Long> repo) { this.repo = repo;}

    /**
     * Gets all the entities in the repository
     * @return all the entities in the repo as a list
     */
    public List<T> getAll() {return repo.findAll();}

    /**
     * Gets an entity with a specific id, if existing
     * @param id the id of the searched entity
     * @return the entity if it exists, null otherwise
     */
    public T getById(long id) {
        if(repo == null || repo.findById(id).isEmpty()){
            return null;
        } else {
            return repo.findById(id).get();
        }
    }


    /**
     * @param id the id of the list that needs to be deleted
     * @return false if a list with the id exists, else true
     */
    public boolean delete(long id) {
        if(id < 0 || !repo.existsById(id)){
            return false;
        } else {
            repo.deleteById(id);
            return true;
        }
    }

}
