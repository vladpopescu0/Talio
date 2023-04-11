package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class EditBoardPasswordViewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Board board;
    @FXML
    private TextField newPass;
    @FXML
    private Label errorLabel;

    /**
     * Constructor for the EditBoardNameCtrl class
     * @param server the server to be used
     * @param mainCtrl the main controller of the application
     * @param board the board whose password will be changed
     */
    @Inject
    public EditBoardPasswordViewCtrl(ServerUtils server, MainCtrl mainCtrl, Board board) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.board = board;
    }

    /**
     * Adds support for keyboard shortcuts
     */
    @FXML
    private void handleShortcuts(KeyEvent event) {
        switch(event.getCode()) {
            case ENTER: editPass();
                break;
            case DELETE:
            case BACK_SPACE:
                removePass();
                break;
            case ESCAPE: cancel();
                break;
        }
    }

    /**
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorLabel.setVisible(false);
    }

    /**
     * Setter for the board
     * @param board the board whose password is to be changed
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Getter for Board
     * @return board
     */
    public Board getBoard() {return board;}

    /**
     * Edits the password of the board to that entered by the User
     */
    public void editPass() {
        if (newPass.getText().isEmpty() || newPass.getText() == null) {
            errorLabel.setVisible(true);
        } else {
            //board.setPassword(newPass.getText());
            server.setBoardPassword(newPass.getText(), board.getId());
            mainCtrl.updatePassword(board.getId(), newPass.getText());
            mainCtrl.closeSecondaryStage();

            BoardViewCtrl ctrl = mainCtrl.getBoardViewCtrl();
            ctrl.setBoard(board);
            ctrl.refresh();
            ctrl.checkUser();
        }
    }

    /**
     * Removes the password from the current board
     */
    public void removePass(){
        server.deleteBoardPassword(board.getId());
        mainCtrl.closeSecondaryStage();

        BoardViewCtrl ctrl = mainCtrl.getBoardViewCtrl();
        ctrl.setBoard(board);
        ctrl.refresh();
        ctrl.checkUser();
    }

    /**
     * Redirects the user to the BoardView
     */
    public void cancel() {
        mainCtrl.closeSecondaryStage();
    }

    /**
     * Returns a String describing page-specific shortcuts
     * @return String description of page-specific shortcuts
     */
    public String additionalHelp() {
        return "Edit Board Password specific shortcuts:\n"
                + "Enter - Set the password\n"
                + "Delete / Backspace - Remove the password\n"
                + "Escape - Close the page";
    }
}
