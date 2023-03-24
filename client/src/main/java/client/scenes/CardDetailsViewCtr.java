package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import commons.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import javax.inject.Inject;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CardDetailsViewCtr implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Card card;

    private  Board board;
    private ObservableList<Task> taskObservableList;

    @FXML
    private Button cancelButton;

    @FXML
    private ListView<Task> taskList;

    @FXML
    private Button confirmButton;

    @FXML
    private TextArea description;
    @FXML
    private Button editButton;

    /**
     * Constructor for the detailed card view
     * @param server the server used
     * @param mainCtrl the mainCtrl used
     * @param card the current card
     * @param board the board to which the card belongs
     */
    @Inject
    public CardDetailsViewCtr(ServerUtils server, MainCtrl mainCtrl, Card card, Board board) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.card = card;
        this.board = board;
    }

    /**
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //cancelButton.setDisable(true);
        refresh();
    }

    /**
     * Updates the tasks and description
     */
    public void refresh() {
        editButton.setVisible(true);
        cancelButton.setVisible(false);
        confirmButton.setVisible(true);
        description.setText(card.getDescription());
        description.setEditable(false);
        System.out.println(card+"cardAAAA\n");
        List<Task> tasks = (card == null || card.getTasks() == null ?
                new ArrayList<>() : card.getTasks());
        System.out.println(tasks+"TTTTTTt");
        taskObservableList = FXCollections.observableList(tasks);
        taskList.setItems(taskObservableList);
        taskList.setCellFactory(t -> new TaskCell(mainCtrl, server, card));
    }

    /**
     * Setter for the board
     * @param board the board to which the card belongs
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Setter for the card
     * @param card the current card
     */
    public void setCard(Card card) {
        this.card = card;
        refresh();
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
    }

    /**
     * Goes back to the board view
     */
    public void back() {
        mainCtrl.showBoardView(board);
    }
}
