package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;

    @Transient
    @JsonIgnore
    private CardList parentCardList;

    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Task> tasks;

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
        tasks = new ArrayList<>();
    }

    /**
     * Constructor for cards also including tasks and descriptions
     * @param name the name of the card
     * @param cardList the cardList to which the card belongs
     * @param tasks the tasks making up the card
     * @param description the description of the card
     */
    public Card(String name, CardList cardList, List<Task> tasks,
                String description) {
        this.name = name;
        this.tasks = tasks;
        parentCardList = cardList;
        this.description = description;
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
     * Setter for the id (Used for server tests)
     * @param id the new id of the board
     */
    public void setId(long id) {
        this.id = id;
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
     * Getter for the description
     * @return the description of the card
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for the description
     * @param description the new description of the card
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Checks if a given card has a description
     * @return true if the card has a description, false otherwise
     */
    public boolean hasDescription() {
        return !(this.description == null) && !this.description.isEmpty();
    }

    /**
     * getter for the tasks
     * @return the tasks of this card
     */
    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * Adds a new task to the card
     * @param task the new task to be added
     */
    public void addTask(Task task) {
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        tasks.add(task);
    }

    /**
     * Generates the label that shows the progress of the tasks
     * @return (completed/total tasks done)
     */
    public String tasksLabel() {
        if (this  == null || tasks == null || tasks.isEmpty()) {
            return "";
        } else {
            int done = 0;
            for (Task t: tasks) {
                if (t.getStatus())
                    done++;
            }
            return "(" + done + "/" +tasks.size() + " done)";
        }
    }

    /**
     * Setter for the "tasks" field
     * @param tasks the new list of cards to replace the old one
     */
    public void setTasks(List<Task> tasks){
        this.tasks = tasks;
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
     * toString method for the Card class
     * @return this as a String
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
