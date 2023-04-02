package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import javax.inject.Inject;

public class AdminCheckCtrl {

    private final MainCtrl mainCtrl;
    private ServerUtils server;

    @FXML
    private TextField passField;

    @FXML
    private Label messageLabel;


    /**
     * Injects mainCtrl and ServerUtils to controller
     * @param mainCtrl Injected main controller
     * @param server Injected server utils
     */
    @Inject
    public AdminCheckCtrl(MainCtrl mainCtrl, ServerUtils server){
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Adds support for keyboard shortcuts
     */
    @FXML
    private void handleShortcuts(KeyEvent event) {
        switch(event.getCode()) {
            case ENTER: checkAdmin();
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
        messageLabel.setVisible(false);
    }

    /**
     * Checks if password is correct to qualify current user as an admin
     */
    public void checkAdmin(){
        String password = passField.getText();
        messageLabel.setVisible(true);
        if (server.isAdmin(password)) {
            messageLabel.setTextFill(Color.DARKGREEN);
            messageLabel.setText("Now logged in as admin");
            mainCtrl.setAdmin(true);
            mainCtrl.setAdminPass(password);
        } else {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Incorrect Password");
        }
    }

    /**
     * Goes back to the board overview page
     */
    public void cancel(){
        passField.clear();
        messageLabel.setVisible(false);
        mainCtrl.showOverview();
    }
}