package server.services;

import commons.CardList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.CardListRepository;

import java.util.List;

@Service
public class CardListService extends GenericService<CardList> {

    @Autowired
    public CardListService(CardListRepository repo){
        super(repo);
    }

    public List<CardList> getAll(){
        return repo.findAll();
    }

    /**
     * @param cl CardList that needs to be added
     * @return null if the CardList is null or has null field, else saves the cardlist
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
    public CardList changeName(CardList target, String name){
        if(target != null && repo.existsById(target.getId())) {
            target.setName(name);
            return repo.save(target);
        }

        return null;

    }

    public boolean delete(long id) {
        System.out.println(id);
        if(id < 0 || !repo.existsById(id)){
            return false;
        } else {
            repo.deleteById(id);
            return true;
        }
    }

}
