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
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;

import static client.utils.ServerUtils.packBoard;
import static client.utils.ServerUtils.unpackBoard;

/**
 * not finished yet
 */
public class BoardsOverviewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;//must change mainCtrl
    private final SocketHandler socketHandler = new SocketHandler(ServerUtils.getServer());

    private ObservableList<Board> data;
    @FXML
    private Label serverLabel;
    @FXML
    private TableView<Board> table;
    @FXML
    private TableColumn<Board, String> colBoardName;
    @FXML
    private TableColumn<Board, String> colCreator;

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
        colBoardName.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().getName()));
        colCreator.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().listUsernames()));
//                .getUsers().get(0).getUsername()));
        socketHandler.registerForUpdates("/topic/boards", Board.class, q -> data.add(q));
        socketHandler.registerForUpdates("/topic/boardsUpdate",
                Board.class, q -> Platform.runLater(() -> {
                    refresh();
                    mainCtrl.getUserBoardsOverviewCtrl().refresh();
                }));
        socketHandler.registerForUpdates("/topic/boardsRenameDeleteAdd",
                Long.class, q -> Platform.runLater(() -> {
                    refresh();
                    mainCtrl.getBoardViewCtrl().refresh();
                }));

    }

    /**
     * Redirects to the createBoard scene
     */
    public void createBoard() {
        mainCtrl.createBoardView(); //to be added with addBoard Scene
    }

    /**
     * refreshes the page, looking for updates
     */
    public void refresh() {
        var boards = server.getBoards();
        System.out.println(boards);
        data = FXCollections.observableList(boards);
        table.setItems(data);
        this.serverLabel.setText(ServerUtils.getServer());
    }

    /**
     * Joins the selected Board
     * It is redirects the user to the Board he has selected and makes him join the board.
     */
    public void joinBoard() {
        Board b = table.getSelectionModel().getSelectedItem();
        if (b == null) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("You need to select a board!");
            alert.showAndWait();
            return;
        }
        b.addUser(mainCtrl.getCurrentUser());
        packBoard(b);
        int index = data.indexOf(b);
        Board bo = server.updateBoard(b);
        data.set(index,bo);
        unpackBoard(b);
        mainCtrl.getCurrentUser().setBoardList(server.
                getBoardsByUserId(mainCtrl.getCurrentUser().getId()));
        mainCtrl.showBoardView(b);
    }

    /**
     * Shows the board a user has selected from the table
     */
    public void showBoard() {
        Board b = table.getSelectionModel().getSelectedItem();
        if (b == null) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("You need to select a board!");
            alert.showAndWait();
            return;
        }
        mainCtrl.showBoardView(b);
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
    }
}