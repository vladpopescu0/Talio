package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Objects;

import static client.scenes.MainCtrl.cardDataFormat;
import static client.utils.ServerUtils.packCardList;

public class CardCell extends ListCell<Card> {
    @FXML
    private Pane cardPane;

    @FXML
    private Label paneLabel;

    @FXML
    private Button editButton;

    private FXMLLoader fxmlLoader;
    private MainCtrl mainCtrl;
    private ServerUtils server;

    /**
     * useful dependencies for universal variables and server communication
     * @param server the utils where the connection to the apis is
     * @param mainCtrl the controller of the whole application
     */
    public CardCell(MainCtrl mainCtrl, ServerUtils server) {
        this.server = server;
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
                    this.editButton.setOnAction(event -> {
                        mainCtrl.setCardId(this.getItem().getId());
                        mainCtrl.showEditCard();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            paneLabel.setText(card.getName());

            setText(null);
            setGraphic(cardPane);
        }
    }

    /**
     * Handles drag-and-drop gestures for Card
     * Object equality is handled by ID equality as Serialized Objects may differ
     * and yield false for equals method
     */
    public void handleDraggable() {
        this.setOnDragDetected(event -> {
            Dragboard db = this.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.put(cardDataFormat, this.getItem());

            db.setContent(content);
            WritableImage snapshot = this.snapshot(new SnapshotParameters(), null);
            db.setDragView(snapshot);

            event.consume();
        });

        this.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.ANY);

            event.consume();
        });

        this.setOnDragDropped(event -> {
            if (event.getDragboard().hasContent(cardDataFormat)) {
                Card origin = (Card) event.getDragboard().getContent(cardDataFormat);

                if (origin.getId() != this.getItem().getId()) {
                    if (Objects.equals(origin.getParentCardList().getId(),
                            this.getItem().getParentCardList().getId())) {
                        dragCardToIdentical(origin);
                    } else {
                        dragCardToDifferent(origin);
                    }
                }
            }
            //CardList drag-and-drop is currently disabled
            /*if (event.getDragboard().hasContent(cardListDataFormat)) {
                CardList origin = (CardList) event.getDragboard().getContent(cardListDataFormat);

                if (!Objects.equals(origin.getId(), this.getItem().getParentCardList().getId())) {
                    System.out.println("Drag of CardList: " + origin.getName() + " to CardList: "
                            + this.getItem().getParentCardList().getName());
                }
            }*/

            event.setDropCompleted(true);

            event.consume();
        });
    }

    /**
     * Handles drag-and-drop gesture between Cards of the same CardList
     * @param origin the Card that the gesture origins from
     */
    public void dragCardToIdentical(Card origin) {
        Board board = mainCtrl.getBoardViewCtrl().getBoard();
        int parentIndex = board.getList().indexOf(this.getItem().getParentCardList());
        long parentId = this.getItem().getParentCardList().getId();
        origin.setParentCardList(null);
        this.getItem().setParentCardList(null);
        server.moveCard(parentId,
                List.of(origin, this.getItem()));
        board.getList().set(parentIndex, server.getCL(parentId));
        mainCtrl.getBoardViewCtrl().refresh();
    }

    /**
     * Handles drag-and-drop gesture between Cards of different CardLists
     * This method may be improved at a later point
     * @param origin the Card that the gesture origins from
     */
    public void dragCardToDifferent(Card origin) {
        Board board = mainCtrl.getBoardViewCtrl().getBoard();
        int oldParentIndex = board.getList().indexOf(server.getCL(
                origin.getParentCardList().getId()));
        int newParentIndex = board.getList().indexOf(server.getCL(
                this.getItem().getParentCardList().getId()));
        CardList oldParent = origin.getParentCardList();
        CardList newParent = this.getItem().getParentCardList();
        packCardList(oldParent);
        packCardList(newParent);
        server.updateParent(origin.getId(), List.of(oldParent, newParent));
        CardList newCardList = server.getCL(newParent.getId());
        board.getList().set(oldParentIndex, server.getCL(oldParent.getId()));
        board.getList().set(newParentIndex, newCardList);
        this.getItem().setParentCardList(newCardList);
        origin.setParentCardList(newCardList);

        dragCardToIdentical(origin);
    }
}