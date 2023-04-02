package client.scenes;

import client.utils.ServerUtils;
import commons.User;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

import javax.inject.Inject;
import java.util.ArrayList;

public class ChangeServerCtrl {

    private final MainCtrl mainCtrl;
    private ServerUtils server;

    @FXML
    private TextField serverField;

    @FXML
    private Label errorLabel;


    /**
     * Injects mainCtrl and ServerUtils to controller
     * @param mainCtrl Injected main controller
     * @param server Injected server utils
     */
    @Inject
    public ChangeServerCtrl(MainCtrl mainCtrl, ServerUtils server){
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Adds support for keyboard shortcuts
     */
    @FXML
    private void handleShortcuts(KeyEvent event) {
        switch(event.getCode()) {
            case ENTER: changeServer();
                break;
            case ESCAPE: cancel();
                break;
        }
    }

    /**
     * Initializes the scene with the current server in the server field,
     * as well as setting the error message to be invisible
     */
    public void initialize(){
        serverField.setText(ServerUtils.getServer());
        errorLabel.setVisible(false);
    }

    /**
     * Change the server to address in serverField
     * Logs in to user in new server, if it exists, else creates a new User on that server
     * If server doesn't exist, set error label to be visible and change server back to old
     * address
     */
    public void changeServer() {
        String newServer = serverField.getText();
        String oldServer = ServerUtils.getServer();
        ServerUtils.setServer(newServer);
        String currUsername = mainCtrl.getCurrentUser().getUsername();
        boolean notfound;
        try {
            notfound = server.getUserByUsername(currUsername).isEmpty();
        } catch (ProcessingException e) {
            ServerUtils.setServer(oldServer);
            errorLabel.setVisible(true);
            return;
        }

        if (newServer == null || newServer.isEmpty()) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("You need to input a server!");
            alert.showAndWait();
            return;
        }

        ServerUtils.setServer(newServer);

        if (notfound) {
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
        errorLabel.setVisible(false);
        mainCtrl.closeSecondaryStage();
        mainCtrl.getOverviewCtrl().refresh();
    }

    /**
     * Goes back to the board overview page
     */
    public void cancel(){
        serverField.clear();
        errorLabel.setVisible(false);
        mainCtrl.closeSecondaryStage();
    }
}
