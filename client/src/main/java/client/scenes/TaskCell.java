package client.scenes;

import client.utils.ServerUtils;
import commons.Card;
import commons.Task;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;

import java.util.List;

public class TaskCell extends ListCell<Task> {

    private ServerUtils server;
    private CardDetailsViewCtr parentController;
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
     * @param cardDetails the parent Controller in which this component is located
     */
    public TaskCell(MainCtrl mainCtrl, ServerUtils server, CardDetailsViewCtr cardDetails) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.parentController = cardDetails;
    }

    /**
     * Initialize the task cell
     */
    public void initialize() {
        System.out.println(this.getItem()+"ID found");
        try{
            if (this.getItem() != null) {
                taskTitle.setText(this.getItem().getTitle());
                taskTitle.setEditable(false);
                editButton.setVisible(true);
                removeButton.setVisible(true);
                cancelButton.setVisible(false);
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
                    this.removeButton.setOnAction(event -> {
                        server.deleteTaskFromCard(parentController.getCard().getId(),
                                this.getItem().getId());
                        server.deleteTask(this.getItem().getId());
                        mainCtrl.showCardDetailsView(server.getCardById(parentController
                                        .getCard().getId()),
                                parentController.getBoard());
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

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
     * Changes the status of the task,
     * when the checkbox is checked or unchecked
     */
    public void changeStatus() {
        this.getItem().changeStatus();
        server.updateTask(this.getItem());
    }

    /**
     * Method assigned to the arrow up button in the Task View Item in fxml
     * Takes the card from the database and moves the selected task upwards in the list
     * the order is saved properly, but the description must be saved before.
     */
    public void moveUp(){
        Task currentTask = this.getItem();
        try {
            Card updatedCard = server.getCardById(this.parentController.getCard().getId());
            List<Task> tasks = updatedCard.getTasks();
            if(!tasks.contains(currentTask)){
                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText("This task was deleted by another user!");
                alert.showAndWait();
            }else{
                int taskIndex = tasks.indexOf(currentTask);
                if(taskIndex!=0){
                    tasks.remove(currentTask);
                    tasks.add(taskIndex-1,currentTask);
                    updatedCard.setTasks(tasks);
                    server.updateCardDetails(updatedCard);
                    this.parentController.setCard(updatedCard);
                    this.parentController.refresh();
                }
            }
        }catch (WebApplicationException e){
            if(e.getResponse().getStatus()==404){
                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText("The parent card of this task was deleted!");
                alert.showAndWait();
            }
        }

    }
    /**
     * Method assigned to the arrow down button in the Task View Item in fxml
     * Takes the card from the database and moves the selected task downwards in the list
     * the order is saved properly, but the description must be saved before.
     */
    public void moveDown(){
        Task currentTask = this.getItem();
        try {
            Card updatedCard = server.getCardById(this.parentController.getCard().getId());
            List<Task> tasks = updatedCard.getTasks();
            if(!tasks.contains(currentTask)){
                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText("This task was deleted by another user!");
                alert.showAndWait();
            }else{
                int taskIndex = tasks.indexOf(currentTask);
                if(taskIndex!=tasks.size()-1){
                    tasks.remove(currentTask);
                    tasks.add(taskIndex+1,currentTask);
                    updatedCard.setTasks(tasks);
                    server.updateCardDetails(updatedCard);
                    this.parentController.setCard(updatedCard);
                    this.parentController.refresh();
                }
            }
        }catch (WebApplicationException e){
            if(e.getResponse().getStatus()==404){
                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText("The parent card of this task was deleted!");
                alert.showAndWait();
            }
        }

    }

}
