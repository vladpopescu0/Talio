package commons;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


import javax.inject.Inject;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String username;

    @JsonBackReference
    @ManyToMany(targetEntity = User.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Board> boardList;

    /**
     * Constructor for object mapper
     */
    @SuppressWarnings("unused")
    public User() {

    }

    /**
     * Constructor for a user
     * @param username the username of the user
     */
    @Inject
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
     * Setter for the id of the user(used for server testing)
     * @param id the new id of the user
     */
    public void setId(long id) {
        this.id = id;
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
        if (this.boardList == null) {
            this.boardList = new ArrayList<>();
        }
        if (this.boardList.contains(board)) {
            return;
        }
        this.boardList.add(board);
        board.getUsers().add(this);
    }

    /**
     * Getter for the list of boards of a user
     * @return the list of boards of a user
     */
    @SuppressWarnings("unused")
    public List<Board> getBoardList() {
        return boardList;
    }

    /**
     * Removes a board from the boards a user has joined
     * @param board the board to be removed
     */
    @SuppressWarnings("unused")
    public void removeBoard(Board board) {
        this.boardList.remove(board);
    }

    /**
     * Equals method for User
     * @param obj another object
     * @return true if-f o is equal to this
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
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
     * Setter for the boardList
     * @param boardList the new list of boards of a user
     */
    public void setBoardList(List<Board> boardList) {
        this.boardList = boardList;
    }
}
