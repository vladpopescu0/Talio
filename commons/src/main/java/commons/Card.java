package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

import java.io.Serializable;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Card implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;

    @Transient
    private CardList parentCardList;

    /**
     * Empty constructor
     */
    public Card(){

    }

    /**
     * Constructor for the Card class
     * @param name the name of the card
     */
    public Card(String name){
        this.name = name;
    }

    /**
     * Extended constructor for the Card class
     * @param name name of the Card
     * @param cardList CardList that Card belongs to
     */
    public Card(String name, CardList cardList) {
        this.name = name;
        parentCardList = cardList;
    }

    /**
     * Getter for the name
     * @return the name of the card
     */
    public String getName() { return this.name; }

    /**
     * @return the id of the card
     */
    public long getId(){
        return this.id;
    }

    /**
     * @param name new name for the card
     * @return the new name
     */
    public String setName(String name){
        this.name=name;
        return name;
    }

    /**
     * Sets the CardList that Card belongs to
     * @param cardList the CardList to set as parent
     */
    public void setParentCardList(CardList cardList) {
        parentCardList = cardList;
    }

    /**
     * Returns the CardList that Card belongs to
     * @return the CardList that Card belongs to
     */
    public CardList getParentCardList() {
        return parentCardList;
    }

    /**
     * Equals method for the Card class
     * @param obj the obj to be compared to this
     * @return true if-f this == obj
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * Hash code method for the card
     * @return a generated hash code
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * toString method for the card
     * @return the card presented as a String
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
