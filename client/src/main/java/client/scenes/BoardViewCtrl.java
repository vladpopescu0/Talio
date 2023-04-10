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
import commons.CardList;
import commons.Tag;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.skin.VirtualFlow;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import java.util.List;
import javafx.util.Duration;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class BoardViewCtrl {

    @SuppressWarnings("unused")
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private Board board;
    private boolean isAnimationPlayed = false;
    private Node focusedNodeBackup;

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
    private Button leaveButton;
    @FXML
    private Button deleteButton;
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
    @FXML
    private Button copyInviteButton;
    @FXML
    private Button viewTags;

    @FXML
    private Button boardPass;
    @FXML
    private Label copyLabel;
    @FXML
    private Label boardTitle;

    @FXML
    private ImageView lockImage;

    private boolean unlocked = true;

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
     * Handles unwanted unfocusing from mouse clicks
     */
    @FXML
    private void handleClicks() {
        refocusFromBackup();
    }

    /**
     * Runs upon initialization of the controller
     */
    public void init() {
        server.setSession(server.getUrl());
        cardListObservableList = FXCollections.observableList(board.getList());
        cardListView.setItems(cardListObservableList);
        cardListView.setCellFactory(cl -> new CardListCell(mainCtrl, server, board, unlocked));
        titledPane.setText(board.getName());
        titledPane.setOnMouseClicked(event -> refocusFromBackup());
        cardListView.setFocusTraversable(false);
        cardListView.setSelectionModel(dsm);
        server.registerForUpdates("/topic/updateList",
                CardList.class, q -> Platform.runLater(() -> {
                    cardListObservableList.add(q);
                    refresh();
                    mainCtrl.getOverviewCtrl().refresh();
                }));
        server.registerForUpdates("/topic/updateParent",
                Long.class, q -> Platform.runLater(() -> {
                    refresh();
                    mainCtrl.getOverviewCtrl().refresh();
                }));
    }

    /**
     * Checks if the user has joined this board and if so, allows him to
     * edit it
     */
    public void checkUser() {
        if (!board.getUsers().contains(mainCtrl.getCurrentUser()) ||
                (board.isHasPassword() && (!mainCtrl.getSavedPasswords().containsKey(board.getId())
                || !server.checkBoardPassword(mainCtrl.getSavedPasswords().get(
                        board.getId()), board.getId())))) {
            leaveButton.setDisable(true);
            deleteButton.setDisable(true);
            editTitle.setDisable(true);
            addList.setDisable(true);
            //cardListView.setDisable(true);
            viewTags.setDisable(true);
            customizeButton.setDisable(true);
            copyInviteButton.setDisable(true);
            boardPass.setDisable(true);
            unlocked = false;
            lockImage.setVisible(true);
            lockImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    unlock();

                }
            });
        } else {
            leaveButton.setDisable(false);
            deleteButton.setDisable(false);
            editTitle.setDisable(false);
            addList.setDisable(false);
            cardListView.setDisable(false);
            viewTags.setDisable(false);
            customizeButton.setDisable(false);
            copyInviteButton.setDisable(false);
            boardPass.setDisable(false);
            unlocked = true;
            lockImage.setOnMouseClicked(null);
            lockImage.setVisible(true);
        }
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
        refocusFromBackup();
    }

    /**
     * refreshes the boardView page
     */
    public void refresh() {
        Node focusedNode = mainCtrl.getFocusedNode();
        long focusedId = -1;
        if (focusedNode instanceof CardCell) {
            focusedId = ((CardCell) focusedNode).getItem().getId();
        }
        if (board != null && board.getId() != null) {
            this.board = server.getBoardByID(board.getId());
            cardListObservableList = FXCollections.observableList(board.getList());
            cardListView.setItems(cardListObservableList);
            cardListView.setCellFactory(cl ->
                    new CardListCell(mainCtrl, server, board, unlocked)
            );
            lockImage.setVisible(false);
            customizeBoard(board);
            boardTitle.setText(board.getName());
            focusChange(focusedId);
        }
    }

    /**
     * Helper method for refresh
     * @param focusedId ID of the focused Node
     */
    private void focusChange(long focusedId) {
        if (focusedId >= 0) {
            for (int x = 0; x < cardListObservableList.size(); x++) {
                List<Card> cardList = cardListObservableList.get(x).getCards();
                for (int y = 0; y < cardList.size(); y++) {
                    if (cardList.get(y).getId() == focusedId) {
                        VirtualFlow virtualFlowCL = (VirtualFlow) cardListView
                                .lookup(".virtual-flow");
                        VirtualFlow virtualFlowC = (VirtualFlow) virtualFlowCL.getCell(x)
                                .lookup(".virtual-flow");
                        Node newFocus = virtualFlowC.getCell(y);
                        if (newFocus instanceof CardCell) {
                            newFocus.requestFocus();
                            CardCell cc = (CardCell) newFocus;
                            if (cc.getItem() != null) {
                                cc.updateItem(cc.getItem(), false);
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    /**
     * Goes back to the overview page
     */
    public void cancel() {
        mainCtrl.closeSecondaryStage();
        mainCtrl.showOverview();
    }

    /**
     * Redirects the user back to the overview page
     */
    public void toCustomizationPage() {
        if (board.getColorScheme().getColorLighter() == null) {
            mainCtrl.getCustomizationPageCtrl().getBoardBG().setValue(Color.BLACK);
        } else {
            mainCtrl.getCustomizationPageCtrl().getBoardBG()
                    .setValue(Color.valueOf(board.getColorScheme().getColorBGlight()));
        }
        if (board.getColorScheme().getColorFont() == null) {
            mainCtrl.getCustomizationPageCtrl().getBoardBG().setValue(Color.WHITE);
        } else {
            mainCtrl.getCustomizationPageCtrl().getBoardFont()
                    .setValue(Color.valueOf(board.getColorScheme().getColorFont()));
        }
        if (board.getListsColorScheme().getColorBGlight() == null) {
            mainCtrl.getCustomizationPageCtrl().getBoardBG().setValue(Color.BLACK);
        } else {
            mainCtrl.getCustomizationPageCtrl().getListBG()
                    .setValue(Color.valueOf(board.getListsColorScheme().getColorBGlight()));
        }
        if (board.getListsColorScheme().getColorFont() == null) {
            mainCtrl.getCustomizationPageCtrl().getListFont().setValue(Color.WHITE);
        } else {
            mainCtrl.getCustomizationPageCtrl().getListFont()
                    .setValue(Color.valueOf(board.getListsColorScheme().getColorFont()));
        }
        mainCtrl.showCustomizationPage(this.board);
        refocusFromBackup();
    }

    /**
     * Redirects the user back to the overview page
     */
    public void toUserOverview() {
        mainCtrl.closeSecondaryStage();
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
        mainCtrl.closeSecondaryStage();
        mainCtrl.showUserBoardOverview();
    }

    /**
     * Redirects to edit Board name scene, where the user can change the name
     * of the current user
     */
    public void editTitle() {
        mainCtrl.showEditBoardNameView(board);
        refocusFromBackup();
    }

    /**
     * Redirects to edit Password scene, where the user can change the password of the board
     */
    public void editPassword() {
        mainCtrl.showChangeBoardPasswordView(board);
        refocusFromBackup();
    }

    /**
     * Customizes the board, list and cards
     *
     * @param board the board to be customized
     */
    public void customizeBoard(Board board) {
        if (board.getColorScheme().getColorBGlight() == null) {
            board.getColorScheme().setColorBGlight(mainCtrl.colorToHex(Color.WHITE));
        }
        if (board.getColorScheme().getColorBGdark() == null) {
            board.getColorScheme().setColorBGdark(mainCtrl.colorToHex(Color.BLACK));
        }
        if (board.getColorScheme().getColorFont() == null) {
            board.getColorScheme().setColorFont(mainCtrl.colorToHex(Color.BLACK));
        }
        if (board.getColorScheme().getColorLighter() == null) {
            board.getColorScheme().setColorLighter(mainCtrl.colorToHex(Color.GRAY));
        }

        this.content = (Region) titledPane.lookup(".title");
//        this.scrollbar = (Region) cardListView.lookup(".virtual-flow > .corner");

        String style = "-fx-background-color: " + board.getColorScheme().getColorBGlight() + ";" +
                "\n-fx-border-color: " + board.getColorScheme().getColorBGlight() + ";";
        String darkerStyle = "-fx-background-color: "
                + board.getColorScheme().getColorBGdark() + ";" +
                "\n-fx-border-color: " + board.getColorScheme().getColorBGdark() + ";";

        boardTitle.setStyle("-fx-text-fill: "+board.getColorScheme().getColorFont()+";");

        content.setOpacity(0);
        titledPane.setStyle(darkerStyle);
        border.setStyle(darkerStyle);
        cardListView.setStyle(style);
        scrollPane.setStyle(style);
        cardListView.setCellFactory(cc -> {
            CardListCell c = new CardListCell(mainCtrl, server, board, unlocked);
            c.setStyle("-fx-background-color: "
                    + board.getListsColorScheme().getColorBGlight() + ";"
                    + "\n-fx-border-color: "
                    + board.getListsColorScheme().getColorBGlight() + ";");
            return c;
        });
    }

//    public void setScrollBarStyle(Region scrollbar, String bgColor) {
//        String style = "-fx-background-color: " + bgColor + ";";
//        scrollbar.setStyle(style);
//    }
    /**
     * Redirects the user to the overview of tags for the current Board
     */
    public void viewTags() {
        mainCtrl.closeSecondaryStage();
        mainCtrl.showViewTags(board);
    }

    /**
     * Copies an invitation code of at least 4 digits
     * to the clipboard and uses a fade animation to
     * display a confirmation pop-up.
     * The user can type this code to the join board
     * scene in the Main Page.
     */
    public void copyLink() {
        long boardId = this.board.getId();
        String inviteCode = String.valueOf(boardId);
        switch (inviteCode.length()) {
            case 1:
                inviteCode = "000" + inviteCode;
                break;
            case 2:
                inviteCode = "00" + inviteCode;
                break;
            case 3:
                inviteCode = "0" + inviteCode;
                break;
        }
        if (!isAnimationPlayed) {
            FadeTransition fade = new FadeTransition();
            fade.setDuration(Duration.millis(4000));
            fade.setFromValue(30);
            fade.setToValue(0);
            fade.setNode(copyLabel);
            fade.setOnFinished(e -> {
                copyLabel.setVisible(false);
                isAnimationPlayed = false;
            });
            copyLabel.setVisible(true);
            copyLabel.setText("Board Code Copied!\nThe Code is: " + inviteCode);
            fade.play();
            isAnimationPlayed = true;
        }
        StringSelection stringSelection = new StringSelection(inviteCode);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        refocusFromBackup();
    }

    /**
     * Focuses the first CardCell on the Board
     */
    private void focusFirstCardCell() {
        for (int x = 0; x < cardListObservableList.size(); x++) {
            List<Card> cardList = cardListObservableList.get(x).getCards();
            for (Card c: cardList) {
                VirtualFlow virtualFlowCL = (VirtualFlow) cardListView
                        .lookup(".virtual-flow");
                VirtualFlow virtualFlowC = (VirtualFlow) virtualFlowCL.getCell(x)
                        .lookup(".virtual-flow");
                Node newFocus = virtualFlowC.getCell(0);
                if (newFocus instanceof CardCell) {
                    newFocus.requestFocus();
                    CardCell cc = (CardCell) newFocus;
                    if (cc.getItem() != null) {
                        focusedNodeBackup = cc;
                        cc.updateItem(cc.getItem(), false);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Setter for the backup of a focused node
     * @param node new focused node backup
     */
    public void setFocusedNodeBackup(Node node) {
        focusedNodeBackup = node;
    }

    /**
     * Refocuses the backup node
     */
    public void refocusFromBackup() {
        long focusedId = -1;
        if (focusedNodeBackup instanceof CardCell
                && ((CardCell) focusedNodeBackup).getItem() != null) {
            focusedId = ((CardCell) focusedNodeBackup).getItem().getId();
        }

        if (focusedId >= 0) {
            for (int x = 0; x < cardListObservableList.size(); x++) {
                List<Card> cardList = cardListObservableList.get(x).getCards();
                for (int y = 0; y < cardList.size(); y++) {
                    if (cardList.get(y).getId() == focusedId) {
                        VirtualFlow virtualFlowCL = (VirtualFlow) cardListView
                                .lookup(".virtual-flow");
                        VirtualFlow virtualFlowC = (VirtualFlow) virtualFlowCL.getCell(x)
                                .lookup(".virtual-flow");
                        Node newFocus = virtualFlowC.getCell(y);
                        if (newFocus instanceof CardCell
                                && ((CardCell) newFocus).getItem() != null) {
                            newFocus.requestFocus();
                            CardCell cc = (CardCell) newFocus;
                            focusedNodeBackup = cc;
                            cc.updateItem(cc.getItem(), false);
                        }
                        return;
                    }
                }
            }
        }
    }

    private final MultipleSelectionModel<CardList> dsm = new MultipleSelectionModel<>() {
        @Override
        public ObservableList<Integer> getSelectedIndices() {
            return FXCollections.emptyObservableList();
        }

        @Override
        public ObservableList<CardList> getSelectedItems() {
            return FXCollections.emptyObservableList();
        }

        @Override
        public void selectIndices(int index, int... indices) {}

        @Override
        public void selectAll() {}

        @Override
        public void selectFirst() {}

        @Override
        public void selectLast() {}

        @Override
        public void clearAndSelect(int index) {}

        @Override
        public void select(int index) {}

        @Override
        public void select(CardList obj) {}

        @Override
        public void clearSelection(int index) {}

        @Override
        public void clearSelection() {}

        @Override
        public boolean isSelected(int index) {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public void selectPrevious() {}

        @Override
        public void selectNext() {}
    };

    /**
     * Deletes a board and all lists of it from the database
     */
    public void deleteBoard() {
        board.removeUser(mainCtrl.getCurrentUser());
        server.updateBoard(board);
        mainCtrl.getCurrentUser().setBoardList(server.
                getBoardsByUserId(mainCtrl.getCurrentUser().getId()));
        if (board.getList() != null) {
            for (CardList cl : board.getList()) {
                server.removeCL(cl.getId());
            }
        }
        if (board.getTags() != null) {
            for (Tag t : board.getTags()) {
                server.removeTag(t.getId());
            }
        }
        //if (board.getCardsColorSchemesList() != null) {
        //    for (ColorScheme c : board.getCardsColorSchemesList()) {
        //        server.deleteColorSchemeById(c.getId());
        //    }
        //}
        server.deleteBoard(board.getId());
        mainCtrl.showUserBoardOverview();
    }

    /**
     * Unlock method
     */
    public void unlock(){
        mainCtrl.showCheckBoardPasswordView(board);
    }
}