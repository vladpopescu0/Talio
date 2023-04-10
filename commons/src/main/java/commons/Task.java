package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;

    private Boolean status;

    /**
     * Empty Constructor
     */
    public Task() {

    }

    /**
     * Constructor for the task Class
     * @param title the title of the task
     */
    public Task(String title) {
        this.title = title;
        status = false;
    }

    /**
     * Setter for the title of the task
     * @param title the new title of the task
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for the title of the task
     * @return the title of the task
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter for the ID of the task
     * @return the id of the task
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for the id of the task (Used for testing the server)
     * @param id the new id of the task
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter for the status of the task
     * @return the status of the task
     */
    public boolean getStatus() {
        return status;
    }

    /**
     * Changes the status of the task.
     * If a task is complete(TRUE),
     * it gets marked as incomplete(FALSE)
     * and vice versa
     */
    public void changeStatus() {
        status = !status;
    }

    /**
     * Equals method for the Task class
     * @param obj the obj to be compared to this
     * @return true if-f this == obj
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * Hash code method for the task
     * @return a generated hash code
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
