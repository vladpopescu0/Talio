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
     * @param board the board whose name will be changed
     */
    @Inject
    public CheckBoardPasswordViewCtrl(ServerUtils server, MainCtrl mainCtrl, Board board) {
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
    public void checkPass() {
        if (pass.getText().isEmpty() || pass.getText() == null) {
            errorLabel.setText("Please enter a valid password");
            errorLabel.setVisible(true);
        } else {
            //board.setPassword(newPass.getText());
            boolean check = server.checkBoardPassword(pass.getText(), board.getId());

            if (check) {
                board.addUser(mainCtrl.getCurrentUser());
                board = server.updateBoard(board);
                mainCtrl.getCurrentUser().setBoardList(server.
                        getBoardsByUserId(mainCtrl.getCurrentUser().getId()));
                mainCtrl.showBoardView(board);
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
}
