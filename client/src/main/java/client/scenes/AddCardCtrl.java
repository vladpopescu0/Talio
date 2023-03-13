package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Modality;

import java.awt.*;

public class AddCardCtrl {

    @FXML
    private TextField title;

    @FXML
    private Button cancel;

    @FXML
    private Button add;

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private long cardListId;

    @Inject
    public AddCardCtrl(ServerUtils server, MainCtrl mainCtrl,long cardListId) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.cardListId = cardListId;

    }

    public void cancel() {
        clearFields();
        mainCtrl.showBoardView();
    }

    public void ok(long id) {
        CardList cl = null;
        try {
            cl = server.getCardListById(id);
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        System.out.println(cl);
        clearFields();
        mainCtrl.showBoardView();
    }

    private Card getCard() {
        var name = title.getText();
        return new Card(name,null);
    }

    private void clearFields() {
    }

}
