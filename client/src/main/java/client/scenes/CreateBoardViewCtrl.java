package client.scenes;

import client.utils.ServerUtils;

import commons.Board;
import commons.User;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javax.inject.Inject;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
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
     * Creates a new board and adds it to the database when the create button is pressed
     */
    public void createNewBoard() {
        if (boardName.getText().isEmpty() || boardName.getText() == null) {
            errorLabel.setVisible(true);
        } else {
            User u1 = new User("AD");
            server.addUser(u1);
            System.out.println(u1);
            System.out.println(mainCtrl.getCurrentUser());
            Board newBoard = new Board(u1, boardName.getText());
            //mainCtrl.getCurrentUser().addBoard(newBoard);
            //newBoard.addUser(mainCtrl.getCurrentUser());
            try {
                server.addBoard(newBoard);
//                server.addUser(u1);
            } catch (WebApplicationException e) {
                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return;
            }
            //mainCtrl.getCurrentUser().addBoard(newBoard);
            mainCtrl.showBoardView(newBoard);
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
        mainCtrl.showOverview();
    }

}

