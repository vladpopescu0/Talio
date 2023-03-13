package client.scenes;

import client.utils.ServerUtils;

import commons.Board;
import commons.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javax.inject.Inject;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateBoardViewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private TextField boardName;
    @FXML
    private Label errorLabel;

    @Inject
    public CreateBoardViewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boardName.setText("Board");
        errorLabel.setVisible(false);
    }

    public void createNewBoard() {
        if (boardName.getText().isEmpty()) {
            errorLabel.setVisible(true);
        } else {
            errorLabel.setVisible(false);
            Board newBoard = new Board(new User("a"), boardName.getText());
            mainCtrl.showBoardView(newBoard);
        }
    }

}
