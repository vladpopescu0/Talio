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

import com.google.inject.Inject;
import client.utils.ServerUtils;
import commons.Board;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;

import java.util.List;

/*Not imlementing initializable is required because otherwise, the ctrl tries to look for
the current server when the app starts, which is impossible, since it has not been
selected yet by the user*/
public class BoardsOverviewCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private ObservableList<Board> data;
    @FXML
    private Label serverLabel;
    @FXML
    private TableView<Board> table;
    @FXML
    private TableColumn<Board, String> colBoardName;
    @FXML
    private Button deleteButton;
    @FXML
    private Label userLabel;
    @FXML
    private Button myBoardsButton;

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
     */
    public void init() {
        server.setSession(ServerUtils.getUrl());
        if(mainCtrl.isAdmin()){
            if (colBoardName != null) {
                colBoardName.setCellValueFactory(q ->
                        new SimpleStringProperty(q.getValue().getName()));
            }
        }
//        //long polling
        server.getBoardUpdates(q -> {
            data.add(q);
            refresh();
        });
        //websockets
        server.registerForUpdates("/topic/boardsUpdate",
                Long.class, q -> Platform.runLater(() -> {
                    refresh();
                    mainCtrl.getBoardViewCtrl().refresh();
                }));
        server.registerForUpdates("/topic/boardsRenameDeleteAdd",
                Long.class, q -> Platform.runLater(() -> {
                    refresh();
                    mainCtrl.getBoardViewCtrl().refresh();
                }));
        server.registerForUpdates("/topic/deleteBoard",
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
     * refreshes the page, looking for updates, and checks for admin privileges
     */
    public void refresh() {
        List<Board> boards;
        if(mainCtrl.isAdmin()){
            boards = server.getBoards();
        } else {
            boards = server.getBoardsByUserId(mainCtrl.getCurrentUser().getId());
        }

        data = FXCollections.observableList(boards);
        table.setItems(data);
        colBoardName.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().getName()));
        this.serverLabel.setText(ServerUtils.getServer());
        this.userLabel.setText(mainCtrl.getCurrentUser().getUsername());
        deleteButton.setVisible(mainCtrl.isAdmin());
        myBoardsButton.setVisible(mainCtrl.isAdmin());
    }

    /**
     * Joins the selected Board
     * It is redirects the user to the Board he has selected and makes him join the board.
     */
    public void joinBoard() {
        Board test = new Board(null,"test");
        test.setHasPassword(true);
        test.setPassword("pass");
        test.setId(0L);
        Board b = (table==null ? test
                : table.getSelectionModel().getSelectedItem());


        if (b == null) {
            if (table!=null && table.getItems().size() != 1) {
                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText("You need to select a board!");
                alert.showAndWait();
                return;
            } else {
                //this avoids cyclomatic complexity
                b = boardUpdate(b);
            }
        }
        if (b.isHasPassword() && !mainCtrl.isAdmin()) {
            if (mainCtrl.getSavedPasswords().containsKey(b.getId())){
                if (server.checkBoardPassword(
                        mainCtrl.getSavedPasswords().get(b.getId()), b.getId()
                )) {
//                    b.addUser(mainCtrl.getCurrentUser());
//                    b = server.updateBoard(b);
//                    mainCtrl.getCurrentUser().setBoardList(server.
//                            getBoardsByUserId(mainCtrl.getCurrentUser().getId()));
                    mainCtrl.showBoardView(b);
                    mainCtrl.closeSecondaryStage();
                    return;
                }
            }
            mainCtrl.showBoardView(b);

        } else {
//            b.addUser(mainCtrl.getCurrentUser());
//            b = server.updateBoard(b);
//            mainCtrl.getCurrentUser().setBoardList(server.
//                    getBoardsByUserId(mainCtrl.getCurrentUser().getId()));
            mainCtrl.showBoardView(b);
            mainCtrl.closeSecondaryStage();
        }
    }



    private Board boardUpdate(Board b) {
        if(table!=null){
            b = table.getItems().get(0);
        }
        return b;
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
     * Changes the board table to only show the user's boards (for admin use)
     */
    public void userBoards() {
        List<Board> boards = server.getBoardsByUserId(mainCtrl.getCurrentUser().getId());
        data = FXCollections.observableList(boards);
        table.setItems(data);
        myBoardsButton.setText("All Boards");
        myBoardsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                allBoards();
            }
        });
    }


    /**
     * Changes the board table to only show all boards (for admin use)
     */
    public void allBoards() {
        List<Board> boards = server.getBoards();
        data = FXCollections.observableList(boards);
        table.setItems(data);
        myBoardsButton.setText("My Boards");
        myBoardsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                userBoards();
            }
        });
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

    /**
     * Redirects to the log in page
     */
    public void changeUser() {
        mainCtrl.showUserView();
    }
}