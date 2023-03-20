package client.scenes;

import client.communication.CardListCommunication;
import client.utils.ServerUtils;
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
    private ServerUtils server;

    private final CardListCommunication clComm;


    private Board board;

    @FXML
    private TextField name;

    /**
     * Constructor for the CreateListCtrl class
     * @param clComm the communication used
     * @param mainCtrl the mainCtrl of the application
     * @param board the board to which the cardList is supposed to be added
     * @param server the server utilities
     */
    @Inject
    public CreateListCtrl(CardListCommunication clComm, MainCtrl mainCtrl,
                          Board board, ServerUtils server) {
        this.clComm = clComm;
        this.mainCtrl = mainCtrl;
        this.board = board;
        this.server = server;
    }

    /**
     * Getter for the name of the list
     * @return the name of the list, as entered by the user
     */
    public String getName(){
        return name.getText();
    }

    /**
     * Setter for the board
     * @param board the board to which the list is added
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Clears the name field
     */
    private void clearField() {
        name.clear();
    }

    /**
     * Creates the list, when the corresponding button is pressed
     */
    public void createList(){
        try{
            CardList list = new CardList(getName());
            board = server.getBoardByID(board.getId());
            board.addList(list);
            server.addBoard(board);
            list.setId(board.getList().get(board.getList().size() - 1).getId());
        } catch (WebApplicationException e){
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        clearField();
        mainCtrl.getBoardViewCtrl().refreshRename();
        mainCtrl.showBoardView(this.board);
    }

    /**
     * Redirects to the board Page
     */
    public void cancel(){
        clearField();
        mainCtrl.showBoardView(this.board);
    }
}