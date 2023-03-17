package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;

    /**
     * Empty consrtuctor
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

//    /**
//     * Setter for the list
//     * @param cllist the list to which the card belongs
//     */
//    public void setList(CardList cllist) { this.cllist = cllist;}
//
//    /**
//     * Getter for the cardList
//     * @return the cardList to which this card belongs
//     */
//    public CardList getCardList(){
//        return this.cllist;
//    }
//>>>>>>> 01db2709de678a4967231964edfbe15ea8ac3175

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
