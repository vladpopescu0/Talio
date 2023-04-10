package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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


    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private List<Tag> tags;

    @OneToOne(cascade = CascadeType.ALL)
    private ColorScheme colors;
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
        tags = new ArrayList<>();
        colors = new ColorScheme("black","black","black","black");
    }

    /**
     * Constructor for cards also including tasks and descriptions
     * @param name the name of the card
     * @param cardList the cardList to which the card belongs
     * @param tasks the tasks making up the card
     * @param tags the Tags attached to the Card
     * @param description the description of the card
     * @param colors the custom Colorscheme of the card
     */
    public Card(String name, CardList cardList, List<Task> tasks,
                List<Tag> tags, String description, ColorScheme colors) {
        this.name = name;
        this.tasks = tasks;
        parentCardList = cardList;
        this.tags = tags;
        this.description = description;
        this.colors = colors;
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
        if (tasks == null || tasks.isEmpty()) {
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
     * Getter for the Tags attached to the Card
     * @return list of Tags
     */
    public List<Tag> getTags() {
        return tags;
    }

    /**
     * Setter for the Tags attached to the Card
     * @param tags list of Tags to be attached to the Card
     */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    /**
     * Adds a Tag to the Card
     * @param tag Tag to be added
     */
    public void addTag(Tag tag) {
        tags.add(tag);
    }

    /**
     * Removes the specified Tag from the list of Tags attached to the Card
     * @param tag Tag to be removed from the list
     */
    public void removeTag(Tag tag) {
        tags = tags.stream().filter(t -> t.getId() != tag.getId()).collect(Collectors.toList());
    }

    /**
     * Setter for the colors field
     * @param colors the new colorScheme
     */
    public void setColors(ColorScheme colors){
        this.colors = colors;
    }

    /**
     * getter for the colors field
     * @return the color scheme of the card
     */
    public ColorScheme getColors(){
        return colors;
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
