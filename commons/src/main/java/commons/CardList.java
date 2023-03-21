package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class CardList implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Card> cards;

    /**
     * Empty constructor for the cardList class
     */
    public CardList() {

    }

    /** Constructor for CardList class
     * @param name the name of the list
     */
    public CardList(String name) {
        this.name = name;
        this.cards = new ArrayList<>();
    }
    /**
     * constructor for testing frontend
     * @param name name of board list
     * @param id set id for testing
     */
    @SuppressWarnings("unused")
    public CardList(String name,long id) {
        this.name = name;
        this.cards = new ArrayList<>();
        this.id=id;
    }

    /**
     * @param name name of the specific CardList
     * @param cards the cards in the CardList
     */
    @SuppressWarnings("unused")
    public CardList(String name, List<Card> cards){
        this.name = name;
        this.cards = cards;
        for(Card c: this.cards) {
            c.setParentCardList(this);
        }
    }
    /**
     * @return the name of the CardList
     */
    public String getName() {
        return name;
    }
    /**
     * @return the cards in the list
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * @return the id of the CardList
     */
    public Long getId() {
        return id;
    }

    /**
     * @param card the card to be added
     * @return true if the card is successfully added in the CardList, else false
     */
    @SuppressWarnings("unused")
    public boolean addCard(Card card){
        if(card == null){
            return false;
        } else {
            this.getCards().add(card);
            card.setParentCardList(this);
            return true;
        }
    }

    /**
     * Removes given Card from the CardList
     * @param card Card to be removed
     */
    public void removeCard(Card card) {
        cards.remove(card);
    }

    /** Sets a new name for a CardList object
     * @param name name that needs to be appended to object
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Equals method for the CardList class
     * @param obj the obj to be compared with this
     * @return true if-f this == obj
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * Hash code method for the cardList class
     * @return a generated hashCode
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * toString method for the CardList class
     * @return the CardList as a String
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }

    /** Sets the id of the cardlist
     * @param id the id to be set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * setter for the cards field, useful when deleting cards
     * on the backend
     * @param cards
     */
    public void setCards(List<Card> cards) {
        this.cards=cards;
    }
}
