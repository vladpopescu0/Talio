package client.scenes;

import client.utils.ServerUtils;
import commons.User;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

import javax.inject.Inject;
import java.util.ArrayList;

public class ChangeServerCtrl {

    private final MainCtrl mainCtrl;
    private ServerUtils server;

    @FXML
    private TextField serverField;


    /**
     * Injects mainCtrl and ServerUtils to controller
     *
     * @param mainCtrl Injected main controller
     * @param server   Injected server utils
     */
    @Inject
    public ChangeServerCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Change the server to address in serverField
     * Logs in to user in new server, if it exists, else creates a new User on that server
     */
    public void changeServer() {
        String newServer = serverField.getText();

        if (newServer.isEmpty() || newServer == null) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("You need to input a server!");
            alert.showAndWait();
            return;
        }

        ServerUtils.setServer(newServer);
        String currUsername = mainCtrl.getCurrentUser().getUsername();

        if (server.getUserByUsername(currUsername).isEmpty()) {
            User newUser = new User(currUsername);
            try {
                server.addUser(newUser);
            } catch (WebApplicationException e) {
                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return;
            }
            mainCtrl.setCurrentUser(server.getUserByUsername(currUsername).get(0));
            mainCtrl.getCurrentUser().setBoardList(new ArrayList<>());
        } else {
            mainCtrl.setCurrentUser(server.getUserByUsername(currUsername).get(0));
            mainCtrl.getCurrentUser().setBoardList(server.getBoardsByUserId(
                    mainCtrl.getCurrentUser().getId()));
        }
        mainCtrl.showOverview();
    }

    /**
     * Goes back to the board overview page
     */
    public void cancel() {
        serverField.clear();
        mainCtrl.showOverview();
    }
}
