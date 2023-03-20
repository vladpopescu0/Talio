/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import java.net.URL;
import java.util.ResourceBundle;

import client.communication.CardListCommunication;
import com.google.inject.Inject;

import client.utils.ServerUtils;
import commons.Board;
import commons.CardList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class BoardViewCtrl implements Initializable {

    @SuppressWarnings("unused")
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private final CardListCommunication cardListCommunication;

    private Board board;

    @FXML
    private TitledPane titledPane;

    @FXML
    private ListView<CardList> cardListView;

    @FXML
    private Button removeButton;

    private ObservableList<CardList> cardListObservableList;


    /**
     * Constructor of the Controller for BoardView
     * @param server Server Utility class
     * @param mainCtrl Main controller of the program
     * @param board the board to be displayed
     * @param cardListCommunication the cardlist utility class
     */
    @Inject
    public BoardViewCtrl(ServerUtils server, MainCtrl mainCtrl,
                         Board board, CardListCommunication cardListCommunication) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.cardListCommunication = cardListCommunication;
        this.board = board;
    }

    /**
     * Runs upon initialization of the controller
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cardListObservableList = FXCollections.observableList(board.getList());
        cardListView.setItems(cardListObservableList);
        cardListView.setCellFactory(cl -> new CardListCell(mainCtrl,cardListCommunication,server));
        titledPane.setText(board.getName());
        if (!board.getUsers().contains(mainCtrl.getCurrentUser())) {
            removeButton.setDisable(false);
        } else {
            removeButton.setDisable(true);
        }
    }

    /**
     * Setter for the board
     * @param board the new board to be assigned to the scene
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * @return the current board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Adds a new CardList to the Board
     */
    public void addCardList() {//Not that suggestive I would say
        mainCtrl.showCreateList(board);
        refresh();
    }

    /**
     * refreshes the boardView page
     */
    public void refresh() {
        this.board=server.getBoardByID(board.getId());
        cardListObservableList = FXCollections.observableList(board.getList());
        cardListView.setItems(cardListObservableList);
        cardListView.setCellFactory(cl ->
            new CardListCell(mainCtrl,cardListCommunication,server)
        );
        System.out.println("updated");
    }

    /**
     * Refreshes the Board View and deletes a card
     * @param cardList the cardlist to be deleted
     */
    public void refreshDelete(CardList cardList) {
        cardListObservableList.remove(cardList);
        refresh();
    }

    /**
     * Goes back to the overview page
     */
    public void cancel(){
        mainCtrl.showOverview();
    }

    /**
     * refreshes page when an object is renamed
     */
    public void refreshRename() {
        cardListView.setItems(FXCollections.observableList(board.getList()));
        refresh();
    }
    /**
     * Redirects the user back to the overview page
     */
    public void toOverview() {
        mainCtrl.showOverview();
    }

    /**
     * Removes the current user from the board, in case the user has joined the board
     */
    public void removeUser() {
        board.removeUser(mainCtrl.getCurrentUser());
        server.updateBoard(board);
        mainCtrl.getCurrentUser().setBoardList(server.
                getBoardsByUserId(mainCtrl.getCurrentUser().getId()));
        mainCtrl.showOverview();
    }

}