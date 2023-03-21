package client.scenes;

import com.google.inject.Inject;
import commons.Board;
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
import javafx.scene.input.TransferMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static client.scenes.MainCtrl.cardDataFormat;
import static client.utils.ServerUtils.packCardList;

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

    private FXMLLoader fxmlLoader;
    private ServerUtils server;

    /**
     * useful dependencies for universal variables and server communication
     *
     * @param serverUtils           the utils where the connection to the apis is
     * @param mainCtrl              the controller of the whole application
     */
    @Inject
    public CardListCell(MainCtrl mainCtrl, ServerUtils serverUtils) {
        this.server = serverUtils;
        this.mainCtrl = mainCtrl;
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

                try {
                    fxmlLoader.load();
                    editListButton.setOnAction(event -> {
                        rename(cardList.getId());
                        mainCtrl.getBoardViewCtrl().refreshRename();
                    });
                    deleteList.setOnAction(event -> {
                        delete(cardList.getId());
                        mainCtrl.getBoardViewCtrl().refreshDelete(cardList);

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
        cardsList.setCellFactory(c -> new CardCell(mainCtrl, server,this));
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
        //CardList drag-and-drop is currently disabled
        /*this.setOnDragDetected(event -> {
            Dragboard db = this.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.put(cardListDataFormat, this.getItem());

            db.setContent(content);
            WritableImage snapshot = this.snapshot(new SnapshotParameters(), null);
            db.setDragView(snapshot);

            event.consume();
        });*/

        this.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.ANY);

            event.consume();
        });

        this.setOnDragDropped(event -> {
            if (event.getDragboard().hasContent(cardDataFormat)) {
                Card origin = (Card) event.getDragboard().getContent(cardDataFormat);

                if (!Objects.equals(origin.getParentCardList().getId(), this.getItem().getId())) {
                    dragCardToCardList(origin);
                }/* else {
                    System.out.println("Drag of Card: " + origin.getName()
                            + " into the same CardList: " + this.getItem().getName());
                }*/
            }
            //CardList drag-and-drop is currently disabled
            /*if (event.getDragboard().hasContent(cardListDataFormat)) {
                CardList origin = (CardList) event.getDragboard().getContent(cardListDataFormat);

                if (!Objects.equals(origin.getId(), this.getItem().getId())) {
                    System.out.println("Drag of CardList: " + origin.getName()
                            + " to CardList: " + this.getItem().getName());
                }
            }*/

            event.setDropCompleted(true);

            event.consume();
        });
    }

    /**
     * Handles drag-and-drop gesture from Card to CardList
     * @param origin the Card that the gesture origins from
     */
    public void dragCardToCardList(Card origin) {
        Board board = mainCtrl.getBoardViewCtrl().getBoard();
        int oldParentIndex = board.getList().indexOf(server.getCL(
                origin.getParentCardList().getId()));
        int newParentIndex = board.getList().indexOf(server.getCL(
                this.getItem().getId()));
        CardList oldParent = origin.getParentCardList();
        packCardList(oldParent);
        packCardList(this.getItem());
        server.updateParent(origin.getId(), List.of(oldParent, this.getItem()));
        board.getList().set(oldParentIndex, server.getCL(oldParent.getId()));
        board.getList().set(newParentIndex, server.getCL(this.getItem().getId()));
        mainCtrl.getBoardViewCtrl().refresh();
    }
}