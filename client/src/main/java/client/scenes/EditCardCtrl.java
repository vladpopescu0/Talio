package client.scenes;

import client.communication.CardListCommunication;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;


public class EditCardCtrl {

    private final CardListCommunication cardListCommunication;
    @FXML
    private TextField title;

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    /**
     * Constructor for EditCardCtrl
     * @param server the server to be used
     * @param mainCtrl the mainCtrl of the application
     * @param cardListCommunication the utilities for card list communication
     */
    @Inject
    public EditCardCtrl(ServerUtils server, MainCtrl mainCtrl,
                        CardListCommunication cardListCommunication) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.cardListCommunication = cardListCommunication;
    }

    /**
     * button for cancelling the add card scene, returning to the board
     */
    public void cancel() {
        clearFields();
        mainCtrl.showBoardView(mainCtrl.getBoardViewCtrl().getBoard());
    }

    /**
     * The function connected to the add card button, posts the card in the
     * database by adding it to a list with a given id
     */
    public void ok() {
        Card toBeAdded = getCard();
        try {
            if(!isNullOrEmpty(toBeAdded.getName())){
                //CardList before = cardListCommunication.getCL(mainCtrl.getId());
                server.updateCard(toBeAdded.getName(), mainCtrl.getCardId());
                //CardList after = cardListCommunication.getCL(mainCtrl.getId());
                //Board board = mainCtrl.getBoardViewCtrl().getBoard();
                //int index = board.getList().indexOf(before);
                //board.getList().set(index,after);
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
     * Updates all fields for the card
     */
    public void updateFields(){
        this.title.setText(server.getCardById(mainCtrl.getId()).getName());
        //must change later for safety measures
    }
    /**
     * Create a Card object with fields as the text in the add card scene
     * @return the string as a parametrized object
     */
    private Card getCard() {
        var name = title.getText();
        return new Card(name);
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
