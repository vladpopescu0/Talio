package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class EditBoardNameViewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Board board;
    @FXML
    private TextField newName;
    @FXML
    private Label errorLabel;

    /**
     * Constructor for the EditBoardNameCtrl class
     * @param server the server to be used
     * @param mainCtrl the main controller of the application
     * @param board the board whose name will be changed
     */
    @Inject
    public EditBoardNameViewCtrl(ServerUtils server, MainCtrl mainCtrl, Board board) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.board = board;
    }

    /**
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        newName.setText(board.getName());
        errorLabel.setVisible(false);
    }

    /**
     * Setter for the board
     * @param board the board whose name is to be changed
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Edits the name of the board to that entered by the User
     */
    public void editName() {
        if (newName.getText().isEmpty() || newName.getText() == null) {
            errorLabel.setVisible(true);
        } else {
            board.setName(newName.getText());
            server.updateBoard(board);
            mainCtrl.closeSecondaryStage();

            BoardViewCtrl ctrl = mainCtrl.getBoardViewCtrl();
            ctrl.setBoard(board);
            ctrl.refresh();
            ctrl.checkUser();
        }
    }

    /**
     * Redirects the user to the BoardView
     */
    public void cancel() {
        mainCtrl.closeSecondaryStage();
    }
}
