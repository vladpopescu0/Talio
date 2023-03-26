package client.scenes;

import com.google.inject.Inject;
import client.utils.ServerUtils;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

public class TagCell extends ListCell<Tag> {

    private final MainCtrl mainCtrl;

    @FXML
    private Pane tagPane;

    @FXML
    private Label paneLabel;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    private FXMLLoader fxmlLoader;
    private ServerUtils server;

    /**
     * useful dependencies for universal variables and server communication
     *
     * @param serverUtils           the utils where the connection to the apis is
     * @param mainCtrl              the controller of the whole application
     */
    @Inject
    public TagCell(MainCtrl mainCtrl, ServerUtils serverUtils) {
        this.server = serverUtils;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Update method for a custom ListCell
     *
     * @param tag The new item for the cell.
     * @param empty    whether or not this cell represents data from the list. If it
     *                 is empty, then it does not represent any domain data, but is a cell
     *                 being used to render an "empty" row.
     */
    @Override
    protected void updateItem(Tag tag, boolean empty) {
        super.updateItem(tag, empty);

        if (empty || tag == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("TagView.fxml"));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                    this.editButton.setOnAction(event -> {
                        mainCtrl.showEditTag(tag);
                    });
                    this.deleteButton.setOnAction(event ->{
                        server.removeTag(tag.getId());
                        mainCtrl.getViewTagsCtrl().refreshEdit();
                        //When the Tags are added to Cards,
                        //their removal must be propagated to the Cards as well
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            paneLabel.setText(tag.getName());

            setText(null);
            setGraphic(tagPane);
        }
    }
}