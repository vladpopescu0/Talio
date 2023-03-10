package client.scenes;

import commons.Card;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;

public class CardCell extends ListCell<Card> {
    @FXML
    private Pane cardPane;

    @FXML
    private Label paneLabel;

    private FXMLLoader fxmlLoader;

    /**
     * Update method for a custom ListCell
     * @param card The new item for the cell.
     * @param empty whether or not this cell represents data from the list. If it
     *        is empty, then it does not represent any domain data, but is a cell
     *        being used to render an "empty" row.
     */
    @Override
    protected void updateItem(Card card, boolean empty) {
        super.updateItem(card, empty);

        if (empty || card == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("CardView.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            paneLabel.setText(card.getName());

            setText(null);
            setGraphic(cardPane);
        }
    }
}