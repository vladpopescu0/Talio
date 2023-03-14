package server.services;

import commons.Card;
import commons.CardList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.CardListRepository;

@Service
public class CardListService extends GenericService<CardList> {

    @Autowired
    public CardListService(CardListRepository repo){
        super(repo);
    }

    /**
     * @param cl CardList that needs to be added
     * @return null if the CardList is null or has null field, else saves the cardlist
     */
    public CardList add(CardList cl){
        if(cl == null || cl.getName() == null){
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
            repo.save(target);
            return true;
        }

        return false;

    }

    /**
     * Adds a card to targeted list
     * @param target the list to which the card is added
     * @param card the card added
     * @return true if successful, otherwise false
     */
    public boolean addCard(CardList target, Card card){
        if(target != null && repo.existsById(target.getId()) && card!=null){
            target.addCard(card);
            return true;
        }
        return false;
    }

}
