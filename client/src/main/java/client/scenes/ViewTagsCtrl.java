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
import commons.Tag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class ViewTagsCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private ListView<Tag> tagsView;

    @FXML
    private Button createTag;

    private Board board;

    private ObservableList<Tag> tagObservableList;

    /**
     * Constructor for the ViewTagsCtrl
     * @param server the server to be used
     * @param mainCtrl the mainCtrl of the application
     * @param board the Board of which Tags are displayed
     */
    @Inject
    public ViewTagsCtrl(ServerUtils server, MainCtrl mainCtrl,
                        Board board) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.board = board;
    }

    /**
     * Initializer for the ViewTags scene
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
        tagsView.setPlaceholder(new Label("There are currently no tags available"));
        tagObservableList = FXCollections.observableList(board.getTags());
        tagsView.setItems(tagObservableList);
        tagsView.setCellFactory(tc ->
                new TagCell(mainCtrl, server,this)
        );
    }

    /**
     * Refreshes the page, looking for updates
     */
    public void refresh() {
        this.board = server.getBoardByID(board.getId());
        tagObservableList = FXCollections.observableList(board.getTags());
        tagsView.setItems(tagObservableList);
        tagsView.setCellFactory(tc ->
                new TagCell(mainCtrl, server,this)
        );
    }

    /**
     * Redirects the user to a page with tag creation
     */
    public void createTag() {
        mainCtrl.showAddTag(board);
        refresh();
    }

    /**
     * Refreshes page when a Tag is edited
     */
    public void refreshEdit() {
        tagsView.setItems(FXCollections.observableList(board.getTags()));
        refresh();
    }

    /**
     * Setter for the board
     * @param board the Board of which Tags are displayed
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * @return the Board of which Tags are displayed
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns back to the Board view
     */
    public void back() {
        mainCtrl.closeSecondaryStage();
        mainCtrl.showBoardView(mainCtrl.getBoardViewCtrl().getBoard());
    }
}