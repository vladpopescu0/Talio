package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;


public class AddCardCtrl {

    @FXML
    private TextField title;

    @FXML
    private Button cancel;

    @FXML
    private Button add;

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    /**
     * Constructor for AddCardCtrl
     * @param server
     * @param mainCtrl
     */
    @Inject
    public AddCardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    /**
     * button for cancelling the add card scene, returning to the board
     */
    public void cancel() {
        clearFields();
        mainCtrl.showBoardView(mainCtrl.board);
        System.out.println(mainCtrl.id);
    }

    /**
     * The function connected to the add card button, posts the card in the
     * database by adding it to a list with a given id
     */
    public void ok() {
        Card toBeAdded = getCard();
        try {
            if(!isNullOrEmpty(toBeAdded.getName())){
                server.addCardToList(toBeAdded,mainCtrl.id);
                clearFields();
                mainCtrl.showBoardView(mainCtrl.board);
            }
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }

    /**
     * Create a Card object with fields as the text in the add card scene
     * @return the string as a parametrized object
     */
    private Card getCard() {
        var name = title.getText();
        Card newCard = new Card(name);
        System.out.println(newCard);
        return newCard;
    }

    /**
     * Checks whether a string is "" or null
     * @param s the string to be tested
     * @return true if the string is as described, false otherwise
     */
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * clears the title text field
     */
    private void clearFields() {
        title.clear();
    }

}
