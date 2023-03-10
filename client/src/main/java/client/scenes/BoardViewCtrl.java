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
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.google.inject.Inject;

import client.utils.ServerUtils;
import commons.Board;
import commons.CardList;
import commons.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class BoardViewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private Board board;

    @FXML
    private TitledPane titledPane;

    @FXML
    private ListView<CardList> cardListView;

    private ObservableList<CardList> cardListObservableList;

    /**
     * Constructor of the Controller for BoardView
     * @param server Server Utility class
     * @param mainCtrl Main controller of the program
     */
    @Inject
    public BoardViewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;

        User user = new User("User"); //Create a front-end only instance of Board
        board = new Board(user, "Board 1");
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
        cardListView.setCellFactory(cl -> new CardListCell());
        titledPane.setText(board.getName());
    }

    /**
     * Adds a new CardList to the Board
     */
    public void addCardList() {
        CardList newCardList = new CardList(board.getList().size() + 1,
                "List " + (board.getList().size() + 1), new ArrayList<>());
        board.addList(newCardList);
        refresh();
    }

    /**
     * Refreshes the Board View
     */
    public void refresh() {
        cardListObservableList = FXCollections.observableList(board.getList());
        cardListView.setItems(cardListObservableList);
    }
}