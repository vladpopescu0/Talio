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
            User u1 = new User("a");
            Board newBoard = new Board(u1, boardName.getText());
            System.out.println(newBoard);
            System.out.println(u1);
            u1.addBoard(newBoard);
            try{
                server.addUser(u1);
                server.addBoard(newBoard);
            }catch (Error e){
                e.printStackTrace();
            }

            mainCtrl.showBoardView(newBoard);
        }
    }

}
