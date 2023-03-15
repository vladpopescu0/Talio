package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class CardList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY) //should have optional = false
    private Board board;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Card> cards;

    public CardList() {

    }

    public CardList(String name,Board board) {
        this.name = name;
        this.board = board;
        this.cards = new ArrayList<>();
    }

    /**
     * constructor for testing frontend
     * @param name name of board list
     * @param board the current board (idk if it works properly)
     * @param id set id for testing
     */
    @SuppressWarnings("unused")
    public CardList(String name,Board board,long id) {
        this.name = name;
        this.board = board;
        this.cards = new ArrayList<>();
        this.id=id;//temporary
    }
    /**
     * @param name name of the specific CardList
     * @param cards the cards in the CardList
     */
    @SuppressWarnings("unused")
    public CardList(String name, List<Card> cards){
        this.id = id;
        this.name = name;
        this.cards = cards;
    }

    @SuppressWarnings("unused")
    public CardList(String name, List<Card> cards, Board board){
        this.name = name;
        this.cards = cards;
        this.board = board;
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
            return true;
        }
    }

    /** Sets a new name for a CardList object
     * @param name name that needs to be appended to object
     */
    public void setName(String name) {
        this.name = name;
    }

    /** Sets a new board for a CardList object
     * @param board board that needs to be appended to object
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }

}
