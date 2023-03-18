package client.scenes;

import client.utils.ServerUtils;
import commons.Card;
import commons.CardList;
import jakarta.ws.rs.BadRequestException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CardListCell extends ListCell<CardList> {
    @FXML
    private TitledPane titledPane;

    @FXML
    private ListView<Card> cardsList;

    @FXML
    private Button addCardButton;

    private ObservableList<Card> cardObservableList;

    private FXMLLoader fxmlLoader;
    private ServerUtils server;
    private MainCtrl mainCtrl;

    /**
     *useful dependencies for universal variables and server communication
     * @param serverUtils the utils where the connection to the apis is
     * @param mainCtrl the controller of the whole application
     */
    @Inject
    public CardListCell(ServerUtils serverUtils, MainCtrl mainCtrl){
        this.server = serverUtils;
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
                    System.out.println(this.getItem().getId());
                    addCardButton.setOnAction(event -> {
                        mainCtrl.id=this.getItem().getId();
                        mainCtrl.showAddCard();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            titledPane.setText(cardList.getName());
            System.out.println(this.getItem().getId());
            long id = this.getItem().getId();
            CardList cl=null;
            try{
                cl = server.getCardListById(id);
            }catch(BadRequestException br){
                System.out.println("tzac");
                //br.printStackTrace();
            }
            List<Card> cards = (cl==null ? new ArrayList<>() : cl.getCards());
            cardObservableList = FXCollections.observableList(cards);
            cardsList.setItems(cardObservableList);
            System.out.println(this.getItem()+"MMMMMMMMMM");
            cardsList.setCellFactory(c -> new CardCell(mainCtrl,server,this.getItem()));

            setText(null);
            setGraphic(titledPane);
        }
    }
}