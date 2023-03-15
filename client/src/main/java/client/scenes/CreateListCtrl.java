package client.scenes;

import client.communication.CardListCommunication;
import commons.Board;
import commons.CardList;
import com.google.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

public class CreateListCtrl {

    private final MainCtrl mainCtrl;

    private final CardListCommunication clComm;

    private Board board;

    @FXML
    private TextField name;

    @Inject
    public CreateListCtrl(CardListCommunication clComm, MainCtrl mainCtrl, Board board) {
        this.clComm = clComm;
        this.mainCtrl = mainCtrl;
        this.board = board;
    }

    public String getName(){
        return name.getText();
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    private void clearField() {
        name.clear();
    }

    public void createList(){
        try{
            clComm.addCL(new CardList(getName(),board));
        } catch (WebApplicationException e){
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

        clearField();
        mainCtrl.showBoardView(this.board);
    }

    public void cancel(){
        clearField();
        mainCtrl.showBoardView(this.board);
    }
}