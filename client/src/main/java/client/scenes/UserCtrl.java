package client.scenes;

import client.utils.ServerUtils;
import commons.User;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class UserCtrl implements Initializable {
    @SuppressWarnings("unused")
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField username;
    @FXML
    private Label notFoundWarning;
    @FXML
    private Label alreadyUsedWarning;

    /**
     * Constructor for the UserCtrl class
     * @param server the serverUtils used
     * @param mainCtrl the mainCtrl of the application
     */
    @Inject
    public UserCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initializer for the User scene
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        username.setText("");
        notFoundWarning.setVisible(false);
        alreadyUsedWarning.setVisible(false);
    }

    /**
     * Getter for the username
     * @return the username entered by the user.
     */
    public String getUsername() {
        return username.getText();
    }

    /**
     * Signs in the user
     */
    public void logUserIn() {
        //if (!isThereAnotherUser()) {
        //    notFoundWarning.setVisible(true);
        //}
        mainCtrl.showOverview();
    }

    /**
     * Creates a new user
     */
    public void createUser() {
        if (server.getUser(getUsername()) == null) {
            alreadyUsedWarning.setVisible(true);
        } else {
            User newUser = new User(getUsername());
            try {
                server.addUser(newUser);
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
}
