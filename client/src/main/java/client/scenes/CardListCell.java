package client.scenes;

import client.utils.ServerUtils;
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

import javax.inject.Inject;

public class CardListCell extends ListCell<CardList> {
    @FXML
    private TitledPane titledPane;

    @FXML
    private ListView<Card> cardsList;

    @FXML
    private Button addCardButton;

    private ObservableList<Card> cardObservableList;

    private FXMLLoader fxmlLoader;
    private ServerUtils serverUtils;
    private MainCtrl mainCtrl;


    @Inject
    public CardListCell(ServerUtils serverUtils, MainCtrl mainCtrl){
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
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

                    addCardButton.setOnAction(event -> {
                        mainCtrl.id=this.getItem().getId();
                        mainCtrl.showAddCard();
                        Card card = new Card("Card " + (cardsList.getItems().size() + 1));
                        cardsList.getItems().add(card);
                        System.out.println(this.getItem().getId()+"cardlist\n");
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
}