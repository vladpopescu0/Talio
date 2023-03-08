package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Board {

    /**
     * The boards are identified by their name,
     * so the name is the primary key is their name
     */
    @Id
    private String name;

    /**
     * Each board is associated with the person that created it.
     */
    @OneToOne(cascade = CascadeType.PERSIST)
    private Person creator;

    /**
     * Each board has multiple lists of cards
     */
    @ElementCollection
    private List<CardList> list;

    /**
     * Constructor for the Board class
     * @param person the creator of the board
     * @param name the name of the board
     * @param list a CardList
     */
    @SuppressWarnings("unused")
    private Board(Person person, List<CardList> list, String name) {
        this.creator = person;
        this.list = list;
        this.name = name;
    }
    //empty constructor was necessary since post requests do not work for some reasons
    //also when creating a post request, the first name and last name of the person are set to null
    private Board() {
        // for object mappers
    }

    /**
     * Constructor for the Board class without a given list
     * @param person the creator of the board
     * @param name the name of the board
     */
    public Board(Person person, String name) {
        this.creator = person;
        this.name = name;
        this.list = new ArrayList<>();
    }

    /**
     * Getter for the creator
     * @return the creator as a Person object
     */
    public Person getCreator() {
        return this.creator;
    }

    /**
     * Getter for the name
     * @return the name of the board
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for the name
     * @param name the new name of the board
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the list of CardLists
     * @return the CardLists
     */
    public List<CardList> getList() {
        return list;
    }

    /**
     * Adds a CardList to the board(used for drag and drop feature)
     * @param cardList the card to be added
     */
    public void addList(CardList cardList) {
        list.add(cardList);
    }

    /**
     * Adds a new, empty CardList to the board
     */
    public void addEmptyList() {
        list.add(new CardList());
    }

    /**
     * Equals method for the Board class
     * @param obj the object whose equality we test
     * @return true if-f obj == this
     */
    @Override
    public boolean equals(Object obj) { return EqualsBuilder.reflectionEquals(this, obj); }

    /**
     * Hash Code method for Board
     * @return hash code
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * toString method for Board
     * @return the Board object presented as a String
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
