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
    @SuppressWarnings("unused")
    public Board getBoard() {
        User u1 = new User("a");
        return new Board(u1, boardName.getText());
    }

    public void createNewBoard() {
        User u1 = new User("C");
        Board newBoard = new Board(u1, boardName.getText());
        u1.addBoard(newBoard);
        newBoard.addUser(u1);

        try {
            server.addBoard(newBoard);
//            server.addUser(u1);
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        mainCtrl.showOverview();
    }
}

