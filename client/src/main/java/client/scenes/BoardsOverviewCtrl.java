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
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;

/**
 * not finished yet
 */
public class BoardsOverviewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;//must change mainCtrl

    private ObservableList<Board> data;

    @FXML
    private TableView<Board> table;
    @FXML
    private TableColumn<Board, String> colBoardName;
    @FXML
    private TableColumn<Board, String> colCreator;

    @Inject
    public BoardsOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colBoardName.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().getName()));
        colCreator.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().getUsers().get(0).getUsername()));
    }

    public void createBoard() {
        mainCtrl.createBoardView(); //to be added with addBoard Scene
    }

    public void refresh() {
        var boards = server.getBoards();
        data = FXCollections.observableList(boards);
        table.setItems(data);
    }

    /**
     * Joins the selected Board
     * It is currently redirecting to the only available Board
     */
    public void joinBoard() {
        Board b = table.getSelectionModel().getSelectedItem();
        if(b == null){
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("You need to select a board!");
            alert.showAndWait();
            return;
        }
            mainCtrl.showBoardView(b);
    }
}