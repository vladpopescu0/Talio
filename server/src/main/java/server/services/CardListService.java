package server.services;

import commons.CardList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.CardListRepository;

import java.util.List;

@Service
public class CardListService {

    private final CardListRepository repo;

    @Autowired
    public CardListService(CardListRepository repo){
        this.repo = repo;
    }

    public List<CardList> getAll(){
        return repo.findAll();
    }

    /**
     * @param id the id of the searched list
     * @return null if the CardList is not found, else the searched CardList
     */
    public CardList getById(long id){
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
    public boolean delete(long id){
        if(id < 0 || !repo.existsById(id)){
            return false;
        } else {
            repo.deleteById(id);
            return true;
        }
    }

    /**
     * @param cl CardList that needs to be added
     * @return null if the CardList is null or has null field, else true
     */
    public CardList add(CardList cl){
        if(cl == null || cl.getName() == null || cl.getCards() == null){
            return null;
        }

        return repo.save(cl);
    }

    /**
     * @param target the CardList whose name needs to be modified
     * @param name the name that will substitute the old name
     * @return true if change is successful, else false
     */
    public boolean changeName(CardList target, String name){
        if(target != null && repo.existsById(target.getId())){
            target.setName(name);
            return true;
        }

        return false;

    }

}
