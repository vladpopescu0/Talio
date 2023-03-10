package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String username;

    private List<Board> boardList;

    /**
     * Constructor for object mapper
     */
    @SuppressWarnings("unused")
    private User() {

    }

    /**
     * Constructor for a user
     * @param username the username of the user
     */
    @SuppressWarnings("unused")
    public User(String username) {
        this.username = username;
        this.boardList = new ArrayList<>();
    }

    /**
     * Getter for the ID
     * @return  the ID of the user
     */
    public long getId() {
        return id;
    }

    /**
     * Getter for the username
     * @return the username of the user
     */
    @SuppressWarnings("unused")
    public String getUsername() {
        return username;
    }

    /**
     * Setter for the username
     * @param username the new username of the User
     */
    @SuppressWarnings("unused")
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Method that adds a new board to the user boards
     * @param board the board to be added
     */
    @SuppressWarnings("unused")
    public void addBoard(Board board) {
        if (this.boardList.contains(board)) {
            return;
        }
        this.boardList.add(board);
    }

    /**
     * Equals method for User
     * @param o another object
     * @return true if-f o is equal to this
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(username, user.username);
    }

    /**
     * Hash method for the User class
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

    /**
     * toString method for the User class
     * @return the current User object as a String
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
