package client.scenes;

import client.utils.ServerUtils;

import commons.Board;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javax.inject.Inject;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateBoardViewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private TextField boardName;
    @FXML
    private Label errorLabel;

    /**
     * Constructor for the CreateBoardViewCtrl class
     * @param server the server to be used
     * @param mainCtrl the main controller of the application
     */
    @Inject
    public CreateBoardViewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initializer for the createBoard scene
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boardName.setText("Board");
        errorLabel.setVisible(false);
    }

    /**
     * Adds support for keyboard shortcuts
     */
    @FXML
    private void handleShortcuts(KeyEvent event) {
        switch(event.getCode()) {
            case ENTER: createNewBoard();
                break;
            case ESCAPE: toBoardsOverview();
                break;
        }
    }

    /**
     * Creates a new board and adds it to the database when the create button is pressed
     */
    public void createNewBoard() {
        if (boardName.getText().isEmpty() || boardName.getText() == null) {
            errorLabel.setVisible(true);
        } else {
            Board newBoard = new Board(server.getUserById(mainCtrl.getCurrentUser().getId()),
                    boardName.getText());
            try {
                server.addBoard(newBoard);
            } catch (WebApplicationException e) {
                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return;
            }
            mainCtrl.getCurrentUser().setBoardList(server.
                    getBoardsByUserId(mainCtrl.getCurrentUser().getId()));
            mainCtrl.getOverviewCtrl().refresh();
            mainCtrl.closeSecondaryStage();
        }
    }

    /**
     * Resets the field to 'Board'
     */
    public void resetField() {
        boardName.setText("Board");
    }

    /**
     * Redirects the user back to the overview page and
     * resets the board name field
     */
    public void toBoardsOverview() {
        resetField();
        mainCtrl.closeSecondaryStage();
    }

    /**
     * Returns a String describing page-specific shortcuts
     * @return String description of page-specific shortcuts
     */
    public String additionalHelp() {
        return "Add Board specific shortcuts:\n"
                + "Enter - Create a board\n"
                + "Escape - Close the page";
    }
}

