package client.scenes;

import client.utils.ServerUtils;
import commons.*;
import commons.Board;
import commons.Card;
import commons.Tag;
import commons.Task;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CardDetailsViewCtr {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Card card;

    private Board board;
    private ObservableList<Task> taskObservableList;
    private ObservableList<Tag> tagObservableList;

    private ObservableList<ColorScheme> colorSchemeObservableList;

    @FXML
    private Button cancelButton;

    @FXML
    private ListView<Task> taskList;

    @FXML
    private ListView<Tag> tagList;

    @FXML
    private ListView<ColorScheme> colorSchemeList;

    @FXML
    private Button confirmButton;

    @FXML
    private TextArea description;
    @FXML
    private Button editButton;
    @FXML
    private Button addTagButton;

    @FXML
    private Button addTaskButton;

    private boolean unlocked = true;

    /**
     * Constructor for the detailed card view
     *
     * @param server   the server used
     * @param mainCtrl the mainCtrl used
     * @param card     the current card
     * @param board    the board to which the card belongs
     */
    @Inject
    public CardDetailsViewCtr(ServerUtils server, MainCtrl mainCtrl, Card card, Board board) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.card = card;
        this.board = board;
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
     * Initialized the card Details view
     */
    public void init() {
        //cancelButton.setDisable(true);
        server.setSession(ServerUtils.getUrl());
        editButton.setVisible(true);
        cancelButton.setVisible(false);
        confirmButton.setVisible(false);
        description.setText(card.getDescription());
        description.setEditable(false);
        List<Task> tasks = (card == null || card.getTasks() == null ?
                new ArrayList<>() : card.getTasks());

        taskObservableList = FXCollections.observableList(tasks);
        taskList.setItems(taskObservableList);
        taskList.setCellFactory(t -> new TaskCell(mainCtrl, server, this, unlocked));
        tagObservableList = FXCollections.observableList(card == null || card.getTags() == null ?
                new ArrayList<>() : card.getTags());
        tagList.setItems(tagObservableList);
        tagList.setCellFactory(t -> new TagAddCell(mainCtrl, server, true, unlocked));
        List<ColorScheme> colors = (board == null || board.getCardsColorSchemesList() == null ?
                new ArrayList<>() : board.getCardsColorSchemesList());
        colorSchemeObservableList = FXCollections.observableList(colors);
        colorSchemeList.setItems(colorSchemeObservableList);
        colorSchemeList.setCellFactory(p ->
                new PresetDetailsCtrl(mainCtrl, server, this,board,card));
        description.setText(card.getDescription());
        server.registerForUpdates("/topic/tasks",
                Long.class, q -> Platform.runLater(() -> {
                    refresh();
                    description.setText(card.getDescription());
                    mainCtrl.getBoardViewCtrl().refresh();
                    mainCtrl.getOverviewCtrl().refresh();
                }));
        server.registerForUpdates("/topic/deleteCard",
                Card.class, q -> Platform.runLater(() -> {
                    if (card.equals(q)) {
                        mainCtrl.showBoardView(server.getBoardByID(board.getId()));
                        mainCtrl.getBoardViewCtrl().refresh();
                        mainCtrl.getOverviewCtrl().refresh();
                        mainCtrl.getCardDetailsViewCtr().setCard(null);
                        mainCtrl.getViewAddTagsCtrl().setCard(null);
                    }
                }));

    }

    /**
     * Updates the tasks, description and tags
     */
    public void refresh() {
        if (server.getCardById(card.getId()) != null) {
            this.card = server.getCardById(card.getId());
            List<Task> tasks = (card == null || card.getTasks() == null ?
                    new ArrayList<>() : card.getTasks());
            taskObservableList = FXCollections.observableList(tasks);
            taskList.setItems(taskObservableList);
            taskList.setCellFactory(t -> new TaskCell(mainCtrl, server, this, unlocked));
            tagObservableList =
                    FXCollections.observableList(card == null || card.getTags() == null ?
                            new ArrayList<>() : card.getTags());
            tagList.setItems(tagObservableList);
            tagList.setCellFactory(t -> new TagAddCell(mainCtrl, server, true, unlocked));
            List<ColorScheme> colors = (board == null || board.getCardsColorSchemesList() == null ?
                    new ArrayList<>() : board.getCardsColorSchemesList());
            colorSchemeObservableList = FXCollections.observableList(colors);
            colorSchemeList.setItems(colorSchemeObservableList);
            colorSchemeList.setCellFactory(p ->
                    new PresetDetailsCtrl(mainCtrl, server, this,board,card));
            description.setText(card.getDescription());

            if (!unlocked) {
                editButton.setVisible(false);
                addTaskButton.setVisible(false);
                addTagButton.setVisible(false);
            }
        }
    }

    /**
     * Updates the Tags when changes were made to them
     */
    public void refreshTagChange() {
        refresh();
    }

    /**
     * Setter for the board
     *
     * @param board the board to which the card belongs
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Setter for the card
     *
     * @param card the current card
     */
    public void setCard(Card card) {
        this.card = card;
        refresh();
    }

    /**
     * Getter for the card field, used when editing task order,
     * so that the task cell is dependent on this class
     *
     * @return the card from this controller
     */
    public Card getCard() {
        return card;
    }

    /**
     * Getter for the board to which the card belongs
     *
     * @return the board to which this card belongs
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Allows to edit the description
     */
    public void edit() {
        cancelButton.setVisible(true);
        confirmButton.setVisible(true);
        description.setEditable(true);
        editButton.setVisible(false);
    }

    /**
     * Restores the description
     */
    public void cancel() {
        editButton.setVisible(true);
        cancelButton.setVisible(false);
        confirmButton.setVisible(false);
        description.setText(card.getDescription());
        description.setEditable(false);
    }

    /**
     * Confirm the changes to the description
     */
    public void confirm() {
        editButton.setVisible(true);
        cancelButton.setVisible(false);
        confirmButton.setVisible(false);
        card.setDescription(description.getText());
        server.updateCardDetails(card);
        description.setText(card.getDescription());
    }

    /**
     * Adds a new task to the card(currently not working)
     */
    public void addTask() {
        Task added = new Task("Task name");
        server.addTaskToCard(added, card.getId());
        refresh();
        mainCtrl.showCardDetailsView(server.getCardById(card.getId()), board, unlocked);
    }

    /**
     * Pops up a secondary page on which tag to be added can be selected
     */
    public void addTag() {
        mainCtrl.showViewAddTag(board, card, false);
    }

    /**
     * Pops up a secondary page on which tag to be added can be selected
     * and informs the page that it's popped from a shortcut
     *
     * @param board Board to which the Card belongs
     * @param card  Card to which Tags may be added
     */
    public void addTagsShortcut(Board board, Card card) {
        mainCtrl.showViewAddTag(board, card, true);
    }

    /**
     * Goes back to the board view
     */
    public void back() {
        mainCtrl.showBoardView(board);
        mainCtrl.closeSecondaryStage();
    }

    /**
     * Set unlocked
     * @param unlocked whether it's unlocked
     */
    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }
}
