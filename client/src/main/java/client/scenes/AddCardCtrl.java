package client.scenes;

import client.communication.CardListCommunication;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
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

    private final CardListCommunication cardListCommunication;

    /**
     * Constructor for the AddCardCtrl class
     * @param server the server to be used
     * @param mainCtrl the mainCtrl of the application
     * @param cardListCommunication the utilities for card list communication
     */
    @Inject
    public AddCardCtrl(ServerUtils server, MainCtrl mainCtrl,
                       CardListCommunication cardListCommunication) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.cardListCommunication = cardListCommunication;

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
        Card toBeAdded = getCard();
        try {
            if(!isNullOrEmpty(toBeAdded.getName())){
                CardList before = cardListCommunication.getCL(mainCtrl.getId());
                server.addCardToList(toBeAdded,mainCtrl.getId());
                CardList after = cardListCommunication.getCL(mainCtrl.getId());
                Board board = mainCtrl.getBoardViewCtrl().getBoard();
                int index = board.getList().indexOf(before);
                board.getList().set(index,after);
                clearFields();
                mainCtrl.showBoardView(mainCtrl.getBoardViewCtrl().getBoard());
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
        Card newCard = new Card(name);
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
     * clears the title text field
     */
    private void clearFields() {
        title.clear();
    }
}