package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;


public class EditCardCtrl {
    @FXML
    private TextField title;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    /**
     * Constructor for EditCardCtrl
     * @param server the server to be used
     * @param mainCtrl the mainCtrl of the application
     */
    @Inject
    public EditCardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * button for cancelling the add card scene, returning to the board
     */
    public void cancel() {
        clearFields();
        mainCtrl.closeSecondaryStage();
    }

    /**
     * The function connected to the add card button, posts the card in the
     * database by adding it to a list with a given id
     */
    public void ok() {
        Card toBeAdded = getCard();
        try {
            if(!isNullOrEmpty(toBeAdded.getName())){
                server.updateCard(toBeAdded.getName(), mainCtrl.getCardId());
                clearFields();
                mainCtrl.closeSecondaryStage();

                BoardViewCtrl ctrl = mainCtrl.getBoardViewCtrl();
                ctrl.setBoard(mainCtrl.getBoardViewCtrl().getBoard());
                ctrl.refresh();
                ctrl.checkUser();
            }
        } catch (WebApplicationException e) {
            e.printStackTrace();
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }

    /**
     * Updates all fields for the card
     * @param id the id of the card
     */
    public void updateFields(Long id){
        this.title.setText(server.getCardById(id).getName());
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
