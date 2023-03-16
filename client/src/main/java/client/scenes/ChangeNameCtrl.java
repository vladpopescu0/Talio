package client.scenes;

import client.communication.CardListCommunication;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.CardList;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

public class ChangeNameCtrl {

    private final MainCtrl mainCtrl;
    private ServerUtils server;

    private final CardListCommunication clComm;

    private Board board;


    private Long id;

    @FXML
    private TextField newName;

    @FXML
    private Button change;

    @Inject
    public ChangeNameCtrl(CardListCommunication clComm, MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.clComm = clComm;
    }

    public String getName(){
        return newName.getText();
    }

//    public void setBoard(Board board) {
//        this.board = board;
//    }

    private void clearField() {
        newName.clear();
    }

    public void changeName(){

        try{
            CardList original = clComm.getCL(id);
            int index = board.getList().indexOf(original);
            CardList cl = clComm.modifyNameCL(id,getName());
            cl.setName(getName());
            board.getList().set(index,cl);
        } catch (WebApplicationException e){
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

        clearField();
        mainCtrl.showBoardView(this.board);
        mainCtrl.getBoardViewCtrl().refreshRename();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void cancel(){
        clearField();
        mainCtrl.showBoardView(this.board);
    }
}