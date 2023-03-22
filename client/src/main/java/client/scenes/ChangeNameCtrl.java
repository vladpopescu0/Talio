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

    /** Constructor foor ChangeNameCtrl
     * @param clComm utility service for cardlist communication
     * @param mainCtrl main controller of the program
     * @param server utility service for server communication
     */
    @Inject
    public ChangeNameCtrl(CardListCommunication clComm, MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.clComm = clComm;
    }

    /**
     * @return the name in the textbox
     */
    public String getName(){
        return newName.getText();
    }

//    public void setBoard(Board board) {
//        this.board = board;
//    }

    /**
     * Clears all fields so they are empty when the page is entered again
     */
    private void clearField() {
        newName.clear();
    }

    /**
     * Method for changing the name of a cardlist in the database
     */
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
        mainCtrl.getBoardViewCtrl().refresh();
    }

    /**
     * Sets the id of the current cardlist
     * @param id id that needs to be set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Sets the board of the current cardlist
     * @param board board that needs to be set
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Goes back to the corresponding board overview page
     */
    public void cancel(){
        clearField();
        mainCtrl.showBoardView(this.board);
    }
}