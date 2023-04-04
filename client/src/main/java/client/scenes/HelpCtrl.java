package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class HelpCtrl {
    @FXML
    private Label pageInfo;

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    /**
     * Constructor for HelpCtrl
     * @param server the server to be used
     * @param mainCtrl the mainCtrl of the application
     */
    @Inject
    public HelpCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }
}