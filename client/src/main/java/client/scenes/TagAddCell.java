package client.scenes;

import com.google.inject.Inject;
import client.utils.ServerUtils;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class TagAddCell extends ListCell<Tag> {

    private final MainCtrl mainCtrl;

    @FXML
    private Pane tagPane;

    @FXML
    private Label paneLabel;

    @FXML
    private Button removeButton;

    @FXML
    private Circle colorCircle;

    private FXMLLoader fxmlLoader;
    private ServerUtils server;

    private boolean visibleRemove;

    /**
     * useful dependencies for universal variables and server communication
     *
     * @param serverUtils           the utils where the connection to the apis is
     * @param mainCtrl              the controller of the whole application
     * @param visibleRemove whether the remove button is visible or not
     */
    @Inject
    public TagAddCell(MainCtrl mainCtrl, ServerUtils serverUtils, boolean visibleRemove) {
        this.server = serverUtils;
        this.mainCtrl = mainCtrl;
        this.visibleRemove = visibleRemove;
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
                fxmlLoader = new FXMLLoader(getClass().getResource("TagAddView.fxml"));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                    colorCircle.setStyle("-fx-fill: "+this.getItem().getColor()+";");
                    this.removeButton.setVisible(visibleRemove);
                    this.removeButton.setOnAction(event ->{
                        server.removeTagFromCard(mainCtrl.getCardDetailsViewCtr().getCard().getId(),
                                this.getItem());
                        mainCtrl.getCardDetailsViewCtr().refreshTagChange();
                        mainCtrl.getViewAddTagsCtrl().refresh();
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