package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.CardList;
import com.google.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

public class CreateListCtrl {

    private final MainCtrl mainCtrl;
    private ServerUtils server;

    private Board board;

    @FXML
    private TextField name;

    /**
     * Constructor for the CreateListCtrl class
     * @param mainCtrl the mainCtrl of the application
     * @param board    the board to which the cardList is supposed to be added
     * @param server   the server utilities
     */
    @Inject
    public CreateListCtrl(MainCtrl mainCtrl,
                          Board board, ServerUtils server) {
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
            case ENTER: createList();
                break;
            case ESCAPE: cancel();
                break;
        }
    }

    /**
     * Getter for the name of the list
     *
     * @return the name of the list, as entered by the user
     */
    public String getName() {
        return name.getText();
    }

    /**
     * Setter for the board
     *
     * @param board the board to which the list is added
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
     * Creates the list, when the corresponding button is pressed
     */
    public void createList() {
        try {
            CardList list = new CardList(getName());
            server.addListToBoard(list, board.getId());
            board.addList(list);
            this.board = server.updateBoard(board);
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        clearField();
//        mainCtrl.showBoardView(board);
        mainCtrl.closeSecondaryStage();
        mainCtrl.showBoardView(board);
    }

    /**
     * Redirects to the board Page
     */
    public void cancel() {
        clearField();
        mainCtrl.closeSecondaryStage();
    }

    /**
     * Returns a String describing page-specific shortcuts
     * @return String description of page-specific shortcuts
     */
    public String additionalHelp() {
        return "Add List specific shortcuts:\n"
                + "Enter - Create a list\n"
                + "Escape - Close the page";
    }
}