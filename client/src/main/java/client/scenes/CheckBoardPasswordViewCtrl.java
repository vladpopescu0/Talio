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

public class CheckBoardPasswordViewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Board board;
    @FXML
    private TextField pass;
    @FXML
    private Label errorLabel;

    /**
     * Constructor for the EditBoardNameCtrl class
     * @param server the server to be used
     * @param mainCtrl the main controller of the application
     * @param board the board whose password will be checked
     */
    @Inject
    public CheckBoardPasswordViewCtrl(ServerUtils server, MainCtrl mainCtrl, Board board) {
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
            case ENTER: checkPass();
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
     * @param board the board whose password is to be checked
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Getter for the board
     * @return the board
     */
    public Board getBoard() {return board;}

    /**
     * Checks the password of the board against the input password
     */
    public void checkPass() {
        if (pass.getText().isEmpty() || pass.getText() == null) {
            errorLabel.setText("Please enter a valid password");
            errorLabel.setVisible(true);
        } else {
            //board.setPassword(newPass.getText());
            boolean check = server.checkBoardPassword(pass.getText(), board.getId());

            if (check) {
                board.addUser(mainCtrl.getCurrentUser());
                mainCtrl.updatePassword(board.getId(), pass.getText());
                board = server.updateBoard(board);
                mainCtrl.getCurrentUser().setBoardList(server.
                        getBoardsByUserId(mainCtrl.getCurrentUser().getId()));
                mainCtrl.showBoardView(board);
                pass.clear();
                mainCtrl.closeSecondaryStage();
            }
            else {
                errorLabel.setText("Incorrect Password");
                errorLabel.setVisible(true);
            }
        }
    }

    /**
     * Redirects the user to the BoardOverview
     */
    public void cancel() {
        mainCtrl.closeSecondaryStage();
    }

    /**
     * Returns a String describing page-specific shortcuts
     * @return String description of page-specific shortcuts
     */
    public String additionalHelp() {
        return "Check Board Password specific shortcuts:\n"
                + "Enter - Submit the password\n"
                + "Escape - Close the page";
    }
}
