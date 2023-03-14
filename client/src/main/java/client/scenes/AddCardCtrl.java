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

    @Inject
    public AddCardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    public void cancel() {
        clearFields();
        mainCtrl.showBoardView();
        System.out.println(mainCtrl.id);
    }

    public void ok() {
        Card toBeAdded = getCard();
        try {
            if(!isNullOrEmpty(toBeAdded.getName())){
                System.out.println(toBeAdded);
                server.addCard(getCard());
                clearFields();
                mainCtrl.showBoardView();
            }
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

    }

    private Card getCard() {
        var name = title.getText();
        CardList cardList = server.getCardListById(mainCtrl.id);
        Card newCard = new Card(name,cardList);
        System.out.println(newCard);
        return newCard;
    }
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    private void clearFields() {
    }

}
