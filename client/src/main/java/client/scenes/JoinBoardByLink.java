package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


public class JoinBoardByLink {

    @FXML
    private TextField code;

    @FXML
    private Button cancel;

    @FXML
    private Button join;

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    /**
     * Constructor for the AddCardCtrl class
     * @param server the server to be used
     * @param mainCtrl the mainCtrl of the application
     */
    @Inject
    public JoinBoardByLink(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    /**
     * button for cancelling the add card scene, returning to the board
     * Cancel button that returns back to the boardView
     */
    public void cancel() {
        clearFields();
        mainCtrl.showBoardView(mainCtrl.getBoardViewCtrl().getBoard());
    }

    /**
     * The function connected to the add card button, posts the card in the
     * database by adding it to a list with a given id
     * The method called when pressing the button creating a card
     */
    public void ok() {
        System.out.println(Long.getLong(code.getText()));
    }

    /**
     * Checks if a string is null or empty
     * @param s the string to be checked
     * @return true is s == null or s = ''
     */
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * clears the title text field
     */
    private void clearFields() {
        code.clear();
    }
}
