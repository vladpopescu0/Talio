package client.scenes;

import client.utils.ServerUtils;
import commons.Card;
import commons.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class TaskCell extends ListCell<Task> {

    private ServerUtils server;
    private Card card;
    private MainCtrl mainCtrl;

    @FXML
    private Pane taskPane;
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

    private FXMLLoader fxmlLoader;

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
        System.out.println(this.getItem()+"ID found");
        try{
            if (this.getItem() != null) {
                taskTitle.setText(this.getItem().getTitle());
                taskTitle.setEditable(true);
                editButton.setVisible(true);
                removeButton.setVisible(true);
                cancelButton.setVisible(true);
                confirmButton.setVisible(false);
                statusBox.setSelected(this.getItem().getStatus());
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }

    }

    /**
     * FXML renderer for the custom Task View component
     * @param task The new item for the cell.
     * @param empty whether or not this cell represents data from the list. If it
     *        is empty, then it does not represent any domain data, but is a cell
     *        being used to render an "empty" row.
     */
    @Override
    protected void updateItem(Task task, boolean empty) {
        super.updateItem(task, empty);

        if (empty || task == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("TaskView.fxml"));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println(this.getItem());

            setText(null);
            setGraphic(taskPane);
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
