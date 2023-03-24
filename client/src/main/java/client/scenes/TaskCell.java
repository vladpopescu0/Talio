package client.scenes;

import client.utils.ServerUtils;
import commons.Card;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;

public class TaskCell extends ListCell<Task> {

    private ServerUtils server;
    private Card card;
    private MainCtrl mainCtrl;
    @FXML
    private CheckBox statusBox;
    @FXML
    private TextField taskTitle;
    @FXML
    private Button editButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button confirmButton;

    /**
     * Constructor for the Task Cell class
     * @param mainCtrl the mainCtrl used
     * @param server the serverUtils
     * @param card the card to which the task belongs
     */
    public TaskCell(MainCtrl mainCtrl, ServerUtils server, Card card) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.card = card;
    }

    /**
     * Initialize the task cell
     */
    public void initialize() {
        if (this.getItem() != null) {
            taskTitle.setText(this.getItem().getTitle());
            taskTitle.setEditable(false);
            editButton.setVisible(true);
            removeButton.setVisible(true);
            cancelButton.setVisible(false);
            confirmButton.setVisible(false);
            statusBox.setSelected(this.getItem().getStatus());
        }
    }

    /**
     * Allows the user to edit the name of the card
     */
    public void edit() {
        editButton.setVisible(false);
        removeButton.setVisible(false);
        cancelButton.setVisible(true);
        confirmButton.setVisible(true);
        taskTitle.setEditable(true);
    }

    /**
     * Cancels the changes to the task
     */
    public void cancel() {
        editButton.setVisible(true);
        removeButton.setVisible(true);
        cancelButton.setVisible(false);
        confirmButton.setVisible(false);
        taskTitle.setEditable(false);
        taskTitle.setText(this.getItem().getTitle());
    }

    /**
     * Confirms the changes to the task
     */
    public void confirm() {
        editButton.setVisible(true);
        removeButton.setVisible(true);
        cancelButton.setVisible(false);
        confirmButton.setVisible(false);
        taskTitle.setEditable(false);
        this.getItem().setTitle(taskTitle.getText());
        server.updateTask(this.getItem());
    }

    /**
     * Removes the current task
     */
    public void remove() {
    }



}
