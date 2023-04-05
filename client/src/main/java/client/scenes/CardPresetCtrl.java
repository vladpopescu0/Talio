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

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.ColorScheme;
import commons.Tag;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;

public class CardPresetCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private ListView<ColorScheme> colorSchemeList;

    private Board board;
    private Card card;

    private ObservableList<ColorScheme> colorSchemeObservableList;

    /**
     * Constructor for the CardPresetCtrl
     * @param server the server to be used
     * @param mainCtrl the mainCtrl of the application
     * @param board the Board of which presets are displayed
     * @param card the Card for which preset can be selected
     */
    @Inject
    public CardPresetCtrl(ServerUtils server, MainCtrl mainCtrl, Card card, Board board) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.board = board;
        this.card = card;
    }

    /**
     * Adds support for keyboard shortcuts
     */
    @FXML
    private void handleShortcuts(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            back();
        }
    }

    /**
     * Initializer for the CardPreset scene
     */
    public void initializ() {
        List<ColorScheme> colors = (board == null || board.getCardsColorSchemesList() == null ?
                new ArrayList<>() : board.getCardsColorSchemesList());
        CardDetailsViewCtr ctrl = mainCtrl.getCardDetailsViewCtr();
        colorSchemeObservableList = FXCollections.observableList(colors);
        colorSchemeList.setItems(colorSchemeObservableList);
        ctrl.setCard(card);
        ctrl.setBoard(board);
        colorSchemeList.setCellFactory(p ->
                new PresetDetailsCtrl(mainCtrl, server,
                        ctrl, board, card));
    }

    /**
     * Refreshes the page, looking for updates
     */
    public void refresh() {
        List<ColorScheme> colors = (board == null || board.getCardsColorSchemesList() == null ?
                new ArrayList<>() : board.getCardsColorSchemesList());
        CardDetailsViewCtr ctrl = mainCtrl.getCardDetailsViewCtr();
        colorSchemeObservableList = FXCollections.observableList(colors);
        colorSchemeList.setItems(colorSchemeObservableList);
        ctrl.setCard(card);
        ctrl.setBoard(board);
        colorSchemeList.setCellFactory(p ->
                new PresetDetailsCtrl(mainCtrl, server,
                        ctrl, board, card));
    }

    /**
     * Setter for the board
     * @param board the Board of which presets are displayed
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * @return the Board of which presets are displayed
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Setter for the Card
     * @param card the Card for which preset can be selected
     */
    public void setCard(Card card) {
        this.card = card;
    }

    /**
     * Getter for the Card
     * @return the Card for which preset can be selected
     */
    public Card getCard() {
        return card;
    }

    /**
     * Returns back to the Board view
     */
    public void back() {
        mainCtrl.closeSecondaryStage();
        mainCtrl.showBoardView(server.getBoardByID(board.getId()));
    }
}