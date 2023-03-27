package client.scenes;

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
import javafx.scene.input.TransferMode;

import java.util.*;

import static client.scenes.MainCtrl.cardDataFormat;

public class CardListCell extends ListCell<CardList> {

    private final MainCtrl mainCtrl;
    @FXML
    private Button editListButton;

    @FXML
    private Button deleteList;
    @FXML
    private TitledPane titledPane;

    @FXML
    private ListView<Card> cardsList;

    @FXML
    private Button addCardButton;

    private ObservableList<Card> cardObservableList;

    private String color;

    private String colorCard;
    private String colorFontCard;

    private FXMLLoader fxmlLoader;
    private ServerUtils server;

    private Board board;

    /**
     * useful dependencies for universal variables and server communication
     * @param serverUtils           the utils where the connection to the apis is
     * @param mainCtrl              the controller of the whole application
     * @param board the board to which the cardList belongs
     */
    @Inject
    public CardListCell(MainCtrl mainCtrl, ServerUtils serverUtils, Board board) {
        this.server = serverUtils;
        this.mainCtrl = mainCtrl;
        this.board = board;
    }

    /**
     * Enable drag-and-drop upon initialization
     */
    public void initialize() {
        handleDraggable();
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
                setStyle("-fx-background-color:" + color + ";");
                try {
                    fxmlLoader.load();
                    editListButton.setOnAction(event -> {
                        rename(cardList.getId());
                    });
                    deleteList.setOnAction(event -> {
                        delete(cardList.getId());
                    });

                    addCardButton.setOnAction(event -> {
                        mainCtrl.setId(this.getItem().getId());
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
                cl = server.getCL(id);
            } catch (BadRequestException br) {
                br.printStackTrace();
            }

            refresh();
            setText(null);
            setGraphic(titledPane);
        }
    }

    /**
     * refresh method for an individual list of cards
     * on the client
     */
    public void refresh(){
        List<Card> cards = (this.getItem() == null ? new ArrayList<>() : this.getItem().getCards());
        cardObservableList = FXCollections.observableList(cards);
        cardsList.setItems(cardObservableList);
        cardsList.setCellFactory(c -> {
            CardCell card = new CardCell(mainCtrl, server,this,board);
            card.setColor(colorCard);
            card.setColorFont(colorFontCard);
            return card;
        });
    }

    /** Helper method for renaming a cardlist
     * @param id the id of the cardList whose name will be modified
     */
    public void rename(Long id) {
        mainCtrl.showChangeListName(id);
    }

    /** Helper method for renaming a cardlist
     * @param id the id of the cardList which will be deleted
     */
    public void delete(Long id) {
        Board b = mainCtrl.getBoardViewCtrl().getBoard();
        server.removeCL(id);
        mainCtrl.showBoardView(b);
    }

    /**
     * Handles drag-and-drop gestures for CardList
     * Object equality is handled by ID equality as Serialized Objects may differ
     * and yield false for equals method
     */
    public void handleDraggable() {
        this.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.ANY);

            event.consume();
        });

        this.setOnDragDropped(event -> {
            if (event.getDragboard().hasContent(cardDataFormat)) {
                //We can opt for passing just a Card ID and retrieve
                // both the Card and CardList later
                @SuppressWarnings("unchecked")
                List<Long> ids = (List<Long>) event.getDragboard().getContent(cardDataFormat);

                if (!Objects.equals(ids.get(1), this.getItem().getId())) {
                    dragCardToCardList(ids);
                }
            }

            event.setDropCompleted(true);

            event.consume();
        });
    }

    /**
     * Handles drag-and-drop gesture from Card to CardList
     * @param ids the Card that the gesture origins from
     */
    public void dragCardToCardList(List<Long> ids) {
        CardList oldParent = server.getCL(ids.get(1));
        server.updateParent(ids.get(0), List.of(oldParent, this.getItem()));
        mainCtrl.getBoardViewCtrl().refresh();
    }
    /** Sets the bg color of the list
     * @param color color to be set
     */
    public void setColor(String color) {
        this.color = color;
    }
    /** Sets the bg color of the card
     * @param colorCard color to be set for the card
     */
    public void setColorCard(String colorCard) {
        this.colorCard = colorCard;
    }
    /** Sets the font color of the card
     * @param colorFontCard color to be set for the card
     */
    public void setColorFontCard(String colorFontCard) {
        this.colorFontCard = colorFontCard;
    }
}