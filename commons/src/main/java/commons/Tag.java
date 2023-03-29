package commons;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tags")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Card> cards;

    /**
     * Empty constructor
     */
    public Tag(){

    }

    /**
     * Constructor for the Tag class
     * @param name name of the Tag
     */
    public Tag(String name) {
        this.name = name;
    }

    /**
     * Getter for the name
     * @return name of the Tag
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the name
     * @param name the new name of the Tag
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the ID of the Tag
     * @return id of the Tag
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for the ID of the Tag
     * It is used for backend testing
     * @param id the new id of the Tag
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Equals method for the Tag class
     * @param obj the obj to be compared to this
     * @return true iff this == obj
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * Hash code method for the Tag
     * @return a generated hash code
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * toString method for the Tag class
     * @return this as a String
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
