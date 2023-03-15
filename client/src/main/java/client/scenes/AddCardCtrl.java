package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;
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
     * Constructor for the AddCardCtrl class
     * @param server the server to be used
     * @param mainCtrl the mainCtrl of the application
     */
    @Inject
    public AddCardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    /**
     * Cancel button that returns back to the boardView
     */
    public void cancel() {
        clearFields();
        mainCtrl.showBoardView(mainCtrl.board);
        System.out.println(mainCtrl.id);
    }

    /**
     * The method called when pressing the button creating a card
     */
    public void ok() {
        Card toBeAdded = getCard();
        try {
            if(!isNullOrEmpty(toBeAdded.getName())){
                System.out.println(toBeAdded+"\nfrontend\n");
                server.addCard(getCard());
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
     * Gets a card with the fields filled by the user
     * @return
     */
    private Card getCard() {
        var name = title.getText();
        CardList cardList = server.getCardListById(mainCtrl.id);
        Card newCard = new Card(name,cardList);
        System.out.println(newCard);
        return newCard;
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
     * Clears all fields
     */
    private void clearFields() {
        title.setText("");
    }

}
