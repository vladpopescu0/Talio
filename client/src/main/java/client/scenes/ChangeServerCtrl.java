package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

import javax.inject.Inject;

public class ChangeServerCtrl {

    private final MainCtrl mainCtrl;
    private ServerUtils server;

    @FXML
    private TextField serverField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button changeButton;

    @FXML
    private Button backButton;

    @FXML
    private Button selectServer;


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
        selectServer.setVisible(false);
    }

    /**
     * Change the server to address in serverField
     * Logs in to user in new server, if it exists, else creates a new User on that server
     * If server doesn't exist, set error label to be visible and change server back to old
     * address
     */
    public void changeServer() {
        decideServer();
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

    /**
     * Returns a String describing page-specific shortcuts
     * @return String description of page-specific shortcuts
     */
    public String additionalHelp() {
        return "Change Server specific shortcuts:\n"
                + "Enter - Submit the address\n"
                + "Escape - Close the page";
    }

    /**
     * Shows the scene as the first scene shown in the app
     */
    public void startScene() {
        backButton.setVisible(false);
        changeButton.setVisible(false);
        selectServer.setVisible(true);
    }

    /**
     * Shows the scene as a pop up to change server
     */
    public void showAsPopUp() {
        backButton.setVisible(true);
        changeButton.setVisible(true);
        selectServer.setVisible(false);
    }

    /**
     * Sets a server when the button is pressed
     */
    public void setServer() {
        decideServer();
        mainCtrl.getBoardsOverviewCtrl().initializ();
    }

    /**
     * Decides if the server can be reached, and connects to it if so
     */
    public void decideServer() {
        String newServer = serverField.getText();
        ServerUtils.setServer(newServer);
        if (newServer == null || newServer.isEmpty()) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("You need to input a server!");
            alert.showAndWait();
            return;
        }
        ServerUtils.setServer(newServer);
        server.setSession(server.getUrl());
        if (server.getSession() != null) {
            errorLabel.setVisible(false);
            mainCtrl.getBoardsOverviewCtrl().initializ();
            mainCtrl.getBoardViewCtrl().initializ();
            mainCtrl.getViewTagsCtrl().initializ();
            mainCtrl.getViewAddTagsCtrl().initializ();
            mainCtrl.getCardDetailsViewCtr().initializ();
            errorLabel.setVisible(false);
            mainCtrl.showUserView();
        } else {
            errorLabel.setVisible(true);
        }
    }
}
