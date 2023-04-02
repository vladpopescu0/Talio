package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import com.google.inject.Inject;
import commons.Tag;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

public class CreateTagCtrl {

    private final MainCtrl mainCtrl;
    private ServerUtils server;

    private Board board;

    @FXML
    private TextField name;

    /**
     * Constructor for the CreateTagCtrl class
     * @param mainCtrl the mainCtrl of the application
     * @param board the board to which the Tag is supposed to be added
     * @param server the server utilities
     */
    @Inject
    public CreateTagCtrl(MainCtrl mainCtrl, Board board,
                         ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.board = board;
        this.server = server;
    }

    /**
     * Adds support for keyboard shortcuts
     */
    @FXML
    private void handleShortcuts(KeyEvent event) {
        switch(event.getCode()) {
            case ENTER: createTag();
                break;
            case ESCAPE: cancel();
                break;
        }
    }

    /**
     * Getter for the name of the tag
     * @return the name of the tag, as entered by the user
     */
    public String getName(){
        return name.getText();
    }

    /**
     * Setter for the board
     * @param board the board to which the tag is added
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Clears the name field
     */
    private void clearField() {
        name.clear();
    }

    /**
     * Creates the tag, when the corresponding button is pressed
     */
    public void createTag(){
        try{
            if (!isNullOrEmpty(getName())) {
                Tag tag = new Tag(getName());
                board = server.getBoardByID(board.getId());
                board.addTag(tag);
                board = server.updateBoard(board);
                tag.setId(board.getTags().get(board.getTags().size() - 1).getId());
            }
        } catch (WebApplicationException e){
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        clearField();
        mainCtrl.closeSecondaryStage();
        mainCtrl.getViewTagsCtrl().refreshEdit();
    }

    /**
     * Checks whether a string is "" or null
     * @param s the string to be tested
     * @return true if the string is as described, false otherwise
     */
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * Redirects to the board Page
     */
    public void cancel(){
        clearField();
        mainCtrl.closeSecondaryStage();
        mainCtrl.showViewTags(board);
    }
}