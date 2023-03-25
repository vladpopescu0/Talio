package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.io.Serializable;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

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
     * Getter for the status of the task
     * @return the status of the task
     */
    public Boolean getStatus() {
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

    /**
     * toString method for the task class
     * @return this as a String
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
