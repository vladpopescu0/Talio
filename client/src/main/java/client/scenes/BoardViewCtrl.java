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

import client.utils.SocketHandler;
import com.google.inject.Inject;

import client.utils.ServerUtils;
import commons.Board;
import commons.CardList;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class BoardViewCtrl implements Initializable {

    @SuppressWarnings("unused")
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private final SocketHandler socketHandler = new SocketHandler(ServerUtils.getServer());

    private Board board;

    private Region content;
    @FXML
    private TitledPane titledPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private AnchorPane border;

    @FXML
    private ListView<CardList> cardListView;

    @FXML
    private Button removeButton;

    private ObservableList<CardList> cardListObservableList;

    @FXML
    private Button editTitle;

    @FXML
    private Button addList;

    @FXML
    private Button customizeButton;

    @FXML
    private Button myBoardsButton;
    @FXML
    private Button allBoardsButton;


    /**
     * Constructor of the Controller for BoardView
     *
     * @param server   Server Utility class
     * @param mainCtrl Main controller of the program
     * @param board    the board to be displayed
     */
    @Inject
    public BoardViewCtrl(ServerUtils server, MainCtrl mainCtrl,
                         Board board) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.board = board;
    }

    /**
     * Runs upon initialization of the controller
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cardListObservableList = FXCollections.observableList(board.getList());
        cardListView.setItems(cardListObservableList);
        cardListView.setCellFactory(cl -> new CardListCell(mainCtrl, server));
        titledPane.setText(board.getName());
    }

    /**
     * Checks if the user has joined this board and if so, allows him to
     * edit it
     */
    public void checkUser() {
        if (!board.getUsers().contains(mainCtrl.getCurrentUser())) {
            removeButton.setDisable(true);
            editTitle.setDisable(true);
            addList.setDisable(true);
            cardListView.setDisable(true);
        } else {
            removeButton.setDisable(false);
            editTitle.setDisable(false);
            addList.setDisable(false);
            cardListView.setDisable(false);
        }
        socketHandler.registerForUpdates("/topic/lists",
                CardList.class, q -> Platform.runLater(() -> {
                    cardListObservableList.add(q);
                    refresh();
                    mainCtrl.getOverviewCtrl().refresh();
                }));
        socketHandler.registerForUpdates("/topic/updateParent",
                Long.class, q -> Platform.runLater(() -> {
                    refresh();
                    mainCtrl.getOverviewCtrl().refresh();
                }));
    }

    /**
     * Setter for the board
     *
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
    public void addCardList() {
        mainCtrl.showCreateList(board);
        refresh();
    }

    /**
     * refreshes the boardView page
     */
    public void refresh() {
        this.board = server.getBoardByID(board.getId());
        cardListObservableList = FXCollections.observableList(board.getList());
        cardListView.setItems(cardListObservableList);
        customizeBoard(board);
    }

    /**
     * Goes back to the overview page
     */
    public void cancel() {
        mainCtrl.showOverview();
    }

    /**
     * Redirects the user back to the overview page
     */
    public void toCustomizationPage() {
        mainCtrl.showCustomizationPage(this.board);
    }

    /**
     * Redirects the user back to the overview page
     */
    public void toUserOverview() {
        mainCtrl.showUserBoardOverview();
    }

    /**
     * Removes the current user from the board, in case the user has joined the board
     */
    public void removeUser() {
        board.removeUser(mainCtrl.getCurrentUser());
        server.updateBoard(board);
        mainCtrl.getCurrentUser().setBoardList(server.
                getBoardsByUserId(mainCtrl.getCurrentUser().getId()));
        mainCtrl.showUserBoardOverview();
    }

    /**
     * Redirects to edit Board name scene, where the user can change the name
     * of the current user
     */
    public void editTitle() {
        mainCtrl.showEditBoardNameView(board);
    }
    public void customizeBoard(Board board) {
        this.content = (Region) titledPane.lookup(".title");
        String style = "-fx-background-color: " + board.getColorBGlight() + ";" +
                "\n-fx-border-color: " + board.getColorBGlight() + ";";
        String darkerStyle = "-fx-background-color: " + board.getColorBGdark() + ";" +
                "\n-fx-border-color: " + board.getColorBGdark()+ ";";

        setButtonStyle(editTitle, board.getColorLighter(),board.getColorFont());
        setButtonStyle(removeButton, board.getColorLighter(),board.getColorFont());
        setButtonStyle(addList, board.getColorLighter(),board.getColorFont());
        setButtonStyle(allBoardsButton, board.getColorLighter(),board.getColorFont());
        setButtonStyle(myBoardsButton, board.getColorLighter(),board.getColorFont());
        setButtonStyle(customizeButton, board.getColorLighter(),board.getColorFont());


        content.setStyle(darkerStyle);
        border.setStyle(darkerStyle);
        cardListView.setStyle(style);
        scrollPane.setStyle(style);
        cardListView.setCellFactory(cl -> {
            CardListCell c = new CardListCell(mainCtrl, server);
            c.setColor(board.getColorBGlight());
            c.setStyle(style);
            return c;
        });
    }

    public void setButtonStyle(Button button,String bgColor, String fontColor){
        String style =  "-fx-background-color: " + bgColor + "; " + "-fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0;" + "-fx-background-radius: 5px;" +
                "-fx-text-fill:" + fontColor + ";";
        button.setStyle(style);
    }
}