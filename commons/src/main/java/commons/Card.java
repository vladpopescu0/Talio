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

    @ManyToOne(optional = false,fetch = FetchType.EAGER)
    private CardList cllist;

    private String name;

    public Card(){

    }

    public Card(String name,CardList cardList){
        this.name = name;
        this.cllist = cardList;
    }
    public Card(String name){
        this.name = name;
        this.cllist = new CardList();
    }

    public String getName() { return this.name; }
    public CardList getCardList(){
        return this.cllist;
    }
    public CardList setCardList(CardList cardList){
        this.cllist=cardList;
        return cardList;
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
