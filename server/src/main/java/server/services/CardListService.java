package server.services;

import commons.Card;
import commons.CardList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.CardListRepository;

import java.util.stream.Collectors;

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
     * @param card the card added
     * @return true if successful, otherwise false
     */
    public boolean addCard(long id, Card card){
        if(repo.existsById(id) && card!=null){
            CardList cl = repo.getById(id);
            cl.addCard(card);
            repo.save(cl);
            return true;
        }
        return false;
    }
    public boolean removeCard(long id, long cardId){
        if(repo.existsById(id)){
            CardList cardList = repo.getById(id);
            var filteredList = cardList.getCards().stream().filter(card -> card.getId()!=cardId).collect(Collectors.toList());
            if(cardList.getCards().size()==filteredList.size()){
                return false;
            }
            cardList.setCards(filteredList);
            repo.save(cardList);
            return true;
        }
        return false;
    }

}
