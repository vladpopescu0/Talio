package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
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
     * Adds support for keyboard shortcuts
     */
    @FXML
    private void handleShortcuts(KeyEvent event) {
        switch(event.getCode()) {
            case ENTER: ok();
                break;
            case ESCAPE: cancel();
                break;
        }
    }

    /**
     * button for cancelling the add card scene, returning to the board
     * Cancel button that returns back to the boardView
     */
    public void cancel() {
        clearFields();
        mainCtrl.closeSecondaryStage();
    }

    /**
     * The function connected to the add card button, posts the card in the
     * database by adding it to a list with a given id
     * The method called when pressing the button creating a card
     */
    public void ok() {
        Card toBeAdded;
        if(getTitle()==null){
            toBeAdded = getCard("Card");
        }else{
            toBeAdded = getCard(getTitle().getText());
        }
        try {
            if(!isNullOrEmpty(toBeAdded.getName())){
                server.addCardToList(toBeAdded, mainCtrl.getId());
                clearFields();

                mainCtrl.closeSecondaryStage();

                BoardViewCtrl ctrl = mainCtrl.getBoardViewCtrl();
                ctrl.setBoard(mainCtrl.getBoardViewCtrl().getBoard());
                ctrl.refresh();
                ctrl.checkUser();
            }
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }

    /**
     * @return textField title, easier to test now
     */
    private TextField getTitle(){
        return title;
    }

    /**
     * Gets a card with the fields filled by the user
     * @return the card from the fields
     */
    private Card getCard(String name) {
        return new Card(name);
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
        if(title!=null){
            title.clear();
        }
    }

    /**
     * Returns a String describing page-specific shortcuts
     * @return String description of page-specific shortcuts
     */
    public String additionalHelp() {
        return "Add Card specific shortcuts:\n"
                + "Enter - Create a card\n"
                + "Escape - Close the page";
    }
}
