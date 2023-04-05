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
import java.util.List;
import java.util.ResourceBundle;

import com.google.inject.Inject;

import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import commons.Tag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

public class ViewAddTagsCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private ListView<Tag> tagsView;

    @FXML
    private Button addTag;

    private Board board;
    private Card card;
    private boolean shortcut;

    private ObservableList<Tag> tagObservableList;

    /**
     * Constructor for the ViewTagsCtrl
     * @param server the server to be used
     * @param mainCtrl the mainCtrl of the application
     * @param board the Board to which the Card belongs
     * @param card the Card to which a Tag will be added
     */
    @Inject
    public ViewAddTagsCtrl(ServerUtils server, MainCtrl mainCtrl,
                           Board board, Card card) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.board = board;
        this.card = card;
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
        tagsView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tagsView.setPlaceholder(new Label("There are currently no tags available to be added"));
    }

    /**
     * Refreshes the page, looking for updates
     */
    public void refresh() {
        this.card = server.getCardById(card.getId());
        this.board = server.getBoardByID(board.getId());
        List<Tag> observableTags = board.getTags();
        for(Tag t: card.getTags()) {
            observableTags.remove(t);
        }
        tagObservableList = FXCollections.observableList(observableTags);
        tagsView.setItems(tagObservableList);
        tagsView.setCellFactory(tc ->
                new TagAddCell(mainCtrl, server, false)
        );
    }

    /**
     * Redirects the user to a page with tag creation
     */
    public void addTags() {
        if (tagsView.getSelectionModel().getSelectedItems().size() > 0) {
            server.addTagsToCard(card.getId(), tagsView.getSelectionModel().getSelectedItems());
            if (shortcut) {
                mainCtrl.getBoardViewCtrl().refresh();
            } else {
                mainCtrl.getCardDetailsViewCtr().refreshTagChange();
            }
            refresh();
        }
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
     * Setter for the Card
     * @param card the Card to which a Tag can be added
     */
    public void setCard(Card card) {
        this.card = card;
    }

    /**
     * Getter for the Card
     * @return the Card to which a Tag can be added
     */
    public Card getCard() {
        return card;
    }

    /**
     * Getter for the shortcut boolean
     * @return whether the page was opened using a keyboard shortcut
     */
    public boolean getShortcut() {
        return shortcut;
    }

    /**
     * Setter for the shortcut boolean
     * @param shortcut whether the page was opened using a keyboard shortcut
     */
    public void setShortcut(boolean shortcut) {
        this.shortcut = shortcut;
    }

    /**
     * Closes the pop up page
     */
    public void back() {
        mainCtrl.closeSecondaryStage();
    }
}