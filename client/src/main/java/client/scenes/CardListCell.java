package client.scenes;

import client.communication.CardListCommunication;
import com.google.inject.Inject;
import commons.Board;
import client.utils.ServerUtils;
import commons.Card;
import commons.CardList;
import jakarta.ws.rs.BadRequestException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;

import java.util.ArrayList;
import java.util.List;

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
    private ServerUtils server;

    /**
     * useful dependencies for universal variables and server communication
     *
     * @param serverUtils the utils where the connection to the apis is
     * @param mainCtrl    the controller of the whole application
     * @param cardListCommunication the utils for CardList class
     */
    @Inject
    public CardListCell(MainCtrl mainCtrl, CardListCommunication cardListCommunication,ServerUtils serverUtils) {
        this.server = serverUtils;
        this.cardListCommunication = cardListCommunication;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Update method for a custom ListCell
     *
     * @param cardList The new item for the cell.
     * @param empty    whether or not this cell represents data from the list. If it
     *                 is empty, then it does not represent any domain data, but is a cell
     *                 being used to render an "empty" row.
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
                    EditListButton.setOnAction(event -> rename(cardList.getId()));
                    deleteList.setOnAction(event -> {
                                delete(cardList.getId());
                                mainCtrl.getBoardViewCtrl().refreshDelete(cardList);

                    });
                            addCardButton.setOnAction(event -> {
                                mainCtrl.id = this.getItem().getId();
                                mainCtrl.showAddCard();
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            titledPane.setText(cardList.getName());
            long id = this.getItem().getId();

            CardList cl = null;
            try {
                cl = cardListCommunication.getCL(id);
            } catch (BadRequestException br) {
                br.printStackTrace();
            }

            List<Card> cards = (cl == null ? new ArrayList<>() : cl.getCards());
            cardObservableList = FXCollections.observableList(cards);
            cardsList.setItems(cardObservableList);
            cardsList.setCellFactory(c -> new CardCell(mainCtrl, server));

            setText(null);
            setGraphic(titledPane);
        }
    }

    public void rename(Long id) {
        mainCtrl.showChangeListName(id);
    }

    public void delete(Long id) {
        Board b = mainCtrl.getBoardViewCtrl().getBoard();
        cardListCommunication.removeCL(id);
        mainCtrl.showBoardView(b);
    }


}