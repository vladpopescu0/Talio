package client.scenes;

import client.communication.CardListCommunication;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.CardList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;

public class CardListCell extends ListCell<CardList> {

    private final MainCtrl mainCtrl;

    private final CardListCommunication cardListCommunication;

    @FXML
    private Button EditListButton;

    @FXML
    private Button deleteList;
    @FXML
    private TitledPane titledPane;

    @FXML
    private ListView<Card> cardsList;

    @FXML
    private Button addCardButton;

    private ObservableList<Card> cardObservableList;

    private FXMLLoader fxmlLoader;

    @Inject
    public CardListCell(MainCtrl mainCtrl,CardListCommunication cardListCommunication){
        this.mainCtrl = mainCtrl;
        this.cardListCommunication = cardListCommunication;

    }


    /**
     * Update method for a custom ListCell
     * @param cardList The new item for the cell.
     * @param empty whether or not this cell represents data from the list. If it
     *        is empty, then it does not represent any domain data, but is a cell
     *        being used to render an "empty" row.
     */
    @Override
    protected void updateItem(CardList cardList, boolean empty) {
        super.updateItem(cardList, empty);

        if (empty || cardList == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("CardListView.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                    EditListButton.setOnAction(event -> {
                        rename(cardList.getId());
                    });
                    deleteList.setOnAction(event -> {
                        delete(cardList.getId());
                        mainCtrl.getBoardViewCtrl().refreshDelete(cardList);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            titledPane.setText(cardList.getName());

            cardObservableList = FXCollections.observableList(cardList.getCards());
            cardsList.setItems(cardObservableList);
            cardsList.setCellFactory(c -> new CardCell());

            setText(null);
            setGraphic(titledPane);
        }
    }

    public void rename(Long id){
        mainCtrl.showChangeListName(id);

    }

    public void delete(Long id){
        Board b = mainCtrl.getBoardViewCtrl().getBoard();
        cardListCommunication.removeCL(id);
        mainCtrl.showBoardView(b);
    }


}