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

import com.google.inject.Inject;

import client.utils.ServerUtils;
import commons.Board;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;

public class BoardsOverviewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;//must change mainCtrl

    private ObservableList<Board> data;
    @FXML
    private Label serverLabel;
    @FXML
    private TableView<Board> table;
    @FXML
    private TableColumn<Board, String> colBoardName;
    @FXML
    private TableColumn<Board, String> colCreator;
    @FXML
    private Button deleteButton;

    /**
     * Constructor for the BoardsOverviewCtrl
     *
     * @param server   the server to be used
     * @param mainCtrl the mainCtrl of the application
     */
    @Inject
    public BoardsOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initializer for the BoardsOverview scene
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colBoardName.setCellValueFactory(q ->
                new SimpleStringProperty(q.getValue().getName()));
        //long polling
        server.getBoardUpdates(q -> {
            data.add(q);
            refresh();
        });
        //websockets
        server.registerForUpdates("/topic/boardsUpdate",
                Board.class, q -> Platform.runLater(() -> {
                    refresh();
                    mainCtrl.getBoardViewCtrl().refresh();
                    mainCtrl.getUserBoardsOverviewCtrl().refresh();
                    mainCtrl.getPrimaryStage()
                            .setTitle(mainCtrl.getBoardViewCtrl().getBoard().getName());
                }));
        server.registerForUpdates("/topic/boardsRenameDeleteAdd",
                Long.class, q -> Platform.runLater(() -> {
                    refresh();
                    mainCtrl.getBoardViewCtrl().refresh();
                }));
//        server.registerForUpdates("/topic/refreshUsers",
//                Long.class, q -> Platform.runLater(() -> {
//                    refresh();
//                }));
    }

    /**
     * Redirects to the createBoard scene
     */
    public void createBoard() {
        mainCtrl.createBoardView(); //to be added with addBoard Scene
    }

    /**
     * refreshes the page, looking for updates, and checks for admin privileges
     */
    public void refresh() {
        var boards = server.getBoards();
        data = FXCollections.observableList(boards);
        table.setItems(data);
        this.serverLabel.setText(ServerUtils.getServer());
        deleteButton.setVisible(mainCtrl.isAdmin());
    }

    /**
     * Joins the selected Board
     * It is redirects the user to the Board he has selected and makes him join the board.
     */
    public void joinBoard() {
        Board b = table.getSelectionModel().getSelectedItem();
        if (b == null) {
            if (table.getItems().size() != 1) {
                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText("You need to select a board!");
                alert.showAndWait();
                return;
            } else {
                b = table.getItems().get(0);
            }
        }
        b.addUser(mainCtrl.getCurrentUser());
        b = server.updateBoard(b);
        mainCtrl.getCurrentUser().setBoardList(server.
                getBoardsByUserId(mainCtrl.getCurrentUser().getId()));
        mainCtrl.showBoardView(b);
        mainCtrl.closeSecondaryStage();
    }

    /**
     * Shows the board a user has selected from the table
     */
    public void showBoard() {
        Board b = table.getSelectionModel().getSelectedItem();
        if (b == null) {
            if (table.getItems().size() != 1) {
                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText("You need to select a board!");
                alert.showAndWait();
                return;
            } else {
                b = table.getItems().get(0);
            }
        }
        mainCtrl.showBoardView(b);
        mainCtrl.closeSecondaryStage();
    }

    /**
     * Deletes the selected board, given the user has admin privileges
     */
    public void deleteBoard() {
        Board b = table.getSelectionModel().getSelectedItem();
        if (b == null) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("You need to select a board!");
            alert.showAndWait();
            return;
        }
        boolean deleted = server.adminDelete(mainCtrl.getAdminPass(), b.getId());
        refresh();
    }

    /**
     * Opens the Admin Login scene
     */
    public void checkAdmin() {
        mainCtrl.showAdminCheck();
    }

    /**
     * Redirects to change server scene
     */
    public void changeServer() {
        mainCtrl.showChangeServer();
    }

    /**
     * Redirects the user to an overview of the boards they've joined
     */
    public void userBoards() {
        mainCtrl.showUserBoardOverview();
        mainCtrl.closeSecondaryStage();
    }

    /**
     * Redirects the user to the join board by code scene
     */
    public void toJoinByLink() {
        mainCtrl.showJoinBoardByLink();
    }

    /**
     * Stops the executors
     */
    public void stop(){
        server.stop();
    }
}