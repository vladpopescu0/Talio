package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Tag;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;


public class EditTagCtrl {
    @FXML
    private TextField title;

    private Board board;
    private Tag tag;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    /**
     * Constructor for EditTagCtrl
     * @param server the server to be used
     * @param mainCtrl the mainCtrl of the application
     * @param board the Board the Tag of which is supposed to be modified
     * @param tag the Tag to be modified
     */
    @Inject
    public EditTagCtrl(ServerUtils server, MainCtrl mainCtrl,
                       Board board, Tag tag) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.board = board;
        this.tag = tag;
    }

    /**
     * Button for cancelling the edit Tag scene, returning to the Board
     */
    public void cancel() {
        clearFields();
        mainCtrl.closeSecondaryStage();
        mainCtrl.showViewTags(mainCtrl.getViewTagsCtrl().getBoard());
    }

    /**
     * The function connected to the add Tag button, posts the Tag in the
     * database by adding it to a Board with a given ID
     */
    public void ok() {
        try {
            if (!isNullOrEmpty(getTitle())) {
                Tag toBeAdded = generateTag();
                server.modifyTag(tag.getId(), toBeAdded);
                clearFields();
                mainCtrl.closeSecondaryStage();
                mainCtrl.showViewTags(mainCtrl.getViewTagsCtrl().getBoard());
            }
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Updates all fields for the Tag
     */
    public void updateFields(){
        title.setText(tag.getName());
    }

    /**
     * Create a Tag object with fields as the text in the edit Tag scene
     * @return the string as a parametrized object
     */
    private Tag generateTag() {
        return new Tag(title.getText());
    }

    /**
     * Checks whether a string is "" or null
     * @param s the string to be tested
     * @return true if the string is as described, false otherwise
     */
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    private String getTitle() {
        return title.getText();
    }

    /**
     * Clears the title text field
     */
    private void clearFields() {
        title.clear();
    }

    /**
     * Setter method for tag
     * @param tag the Tag to update the tag
     */
    public void setTag(Tag tag) {
        this.tag = tag;
    }
}