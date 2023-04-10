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
        String password;
        if(passField!=null){
            password = passField.getText();
        }else{
            password="password";
        }
        if(messageLabel!=null){
            messageLabel.setVisible(true);
        }
        if (server.isAdmin(password)) {
            if(messageLabel!=null){
                messageLabel.setTextFill(Color.DARKGREEN);
                messageLabel.setText("Now logged in as admin");
            }
            mainCtrl.setAdmin(true);
            mainCtrl.setAdminPass(password);
            mainCtrl.closeSecondaryStage();
        } else {
            if(messageLabel!=null){
                messageLabel.setTextFill(Color.RED);
                messageLabel.setText("Incorrect Password");
            }
        }
        mainCtrl.showOverview();
    }

    /**
     * Goes back to the board overview page
     */
    public void cancel(){
        passField.clear();
        messageLabel.setVisible(false);
        mainCtrl.closeSecondaryStage();
    }

    /**
     * Returns a String describing page-specific shortcuts
     * @return String description of page-specific shortcuts
     */
    public String additionalHelp() {
        return "Admin Login specific shortcuts:\n"
                + "Enter - Submit the password\n"
                + "Escape - Close the page";
    }
}
