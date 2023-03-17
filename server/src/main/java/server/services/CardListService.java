package server.services;

import commons.Card;
import commons.CardList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.CardListRepository;

import java.util.List;

@Service
public class CardListService extends GenericService<CardList> {

    /**
     * Constructor for the CardListService class
     * @param repo the repository used
     */
    @Autowired
    public CardListService(CardListRepository repo) {
        super(repo);
    }

    /**
     * @return all CardList entities
     */
    public List<CardList> getAll() {
        return repo.findAll();
    }

    /**
     * @param cl CardList that needs to be added
     * @return null if the CardList is null or has null field, else saves the cardlist
     */
    public CardList add(CardList cl) {
        if (cl == null || cl.getName() == null) {
            return null;
        }

        return repo.save(cl);
    }

    /**
     * @param target the CardList whose name needs to be modified
     * @param name   the name that will substitute the old name
     * @return true if change is successful, else false
     */
    public CardList changeName(CardList target, String name) {
        if (target != null && repo.existsById(target.getId())) {
            target.setName(name);
            return repo.save(target);
        }

        return null;

    }

    /**
     * @param id the id of the list that needs to be deleted
     * @return true if the deletion was successful, false otherwise
     */
    public boolean delete(long id) {
        if (id < 0 || !repo.existsById(id)) {
            return false;
        } else {
            repo.deleteById(id);
            return true;
        }
    }

    /**
     * Adds a card to targeted list
     *
     * @param card the card added
     * @param id the id of the cardlist in which the card needs to be added
     * @return true if successful, otherwise false
     */
    public boolean addCard(long id, Card card) {
        if (repo.existsById(id) && card != null) {
            CardList cl = repo.getById(id);
            cl.addCard(card);
            repo.save(cl);
            return true;
        }
        return false;
    }

}
