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
import java.util.ArrayList;
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
    @FXML
    private Label emptyUsernameWarning;

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
        emptyUsernameWarning.setVisible(false);
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
        if (getUsername().isEmpty() || getUsername() == null) {
            emptyUsernameWarning.setVisible(true);
        } else if (server.getUserByUsername(getUsername()).isEmpty()) {
            notFoundWarning.setVisible(true);
            emptyUsernameWarning.setVisible(false);
        } else {
            mainCtrl.setCurrentUser(server.getUserByUsername(getUsername()).get(0));
            mainCtrl.getCurrentUser().setBoardList(server.
                    getBoardsByUserId(mainCtrl.getCurrentUser().getId()));
            mainCtrl.forgetPasswords();
            mainCtrl.showOverview();
        }

    }

    /**
     * Creates a new user
     */
    public void createUser() {
        if (getUsername().isEmpty() || getUsername() == null) {
            emptyUsernameWarning.setVisible(true);
        } else if (!server.getUserByUsername(getUsername()).isEmpty()) {
            alreadyUsedWarning.setVisible(true);
            emptyUsernameWarning.setVisible(false);
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
            mainCtrl.setCurrentUser(server.getUserByUsername(getUsername()).get(0));
            mainCtrl.getCurrentUser().setBoardList(new ArrayList<>());
            mainCtrl.forgetPasswords();
            mainCtrl.showOverview();
        }
    }
}
