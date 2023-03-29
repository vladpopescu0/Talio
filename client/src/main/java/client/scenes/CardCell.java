package client.scenes;

import client.utils.ServerUtils;
import client.utils.SocketHandler;
import commons.*;
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

public class CardCell extends ListCell<Card> {
    @FXML
    private Pane cardPane;

    @FXML
    private Label paneLabel;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;
    @FXML
    private Label statusLabel;

    @FXML
    private Label tagLabel1;
    @FXML
    private Label tagLabel2;
    @FXML
    private Label tagLabel3;
    @FXML
    private Label tagLabel4;
    @FXML
    private Label tagLabel5;

    @FXML
    private Label hasDesc;
    private FXMLLoader fxmlLoader;
    private Board board;
    private MainCtrl mainCtrl;
    private final SocketHandler socketHandler = new SocketHandler(ServerUtils.getServer());
    private ServerUtils server;

    /**
     * useful dependencies for universal variables and server communication
     * @param server the utils where the connection to the apis is
     * @param mainCtrl the controller of the whole application
     * @param cardList the cardListCell in which this card is
     * @param board the board the card belongs to
     */
    public CardCell(MainCtrl mainCtrl, ServerUtils server
            , CardListCell cardList, Board board) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.board = board;
        if(this.getItem()!=null){
            this.getItem().setParentCardList(cardList.getItem());
            //statusLabel.setText(this.getItem().tasksLabel());
            statusLabel.setText("AAAA");
        }
    }

    /**
     * Enable drag-and-drop upon initialization and test whether the
     * card has a description
     */
    public void initialize() {
        handleDraggable();
        hasDesc.setVisible(this.getItem() != null && this.getItem().hasDescription());
        statusLabel.setText(this.getItem().tasksLabel());
        cardPane.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                mainCtrl.closeSecondaryStage();
                mainCtrl.showCardDetailsView(this.getItem(), board);
            }
        });
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
                    displayTags();
                    this.editButton.setOnAction(event -> {
                        mainCtrl.setCardId(this.getItem().getId());
                        mainCtrl.showEditCard();
                    });
                    this.deleteButton.setOnAction(event ->{
                        var c = server.deleteCardfromList
                                (this.getItem().getParentCardList().getId(),this.getItem().getId());
                        if (this.getItem().getTasks() != null) {
                            for (Task t : this.getItem().getTasks()) {
                                server.deleteTaskFromCard(this.getItem().getId(), t.getId());
                                server.deleteTask(t.getId());
                            }
                        }
                        server.deleteCard(this.getItem().getId());

                        if (mainCtrl.isSecondaryFromCardCell(this.getItem())) {
                            mainCtrl.closeSecondaryStage();
                        }

                        mainCtrl.getBoardViewCtrl().refresh();
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
     * Displays Tags attached to the Card
     */
    public void displayTags() {
        List<Tag> tags = this.getItem().getTags();
        List<Label> labels = List.of(tagLabel1, tagLabel2, tagLabel3, tagLabel4, tagLabel5);

        if (tags.size() <= 5) {
            for(int x = 0; x < tags.size(); x++) {
                labels.get(tags.size() - x - 1).setText(tags.get(x).getName());
            }
            for(int x = tags.size(); x < 5; x++) {
                labels.get(x).setText(null);
            }
        } else {
            tagLabel1.setText("+" + (tags.size() - 4) + " more");
            for(int x = 0; x < 4; x++) {
                labels.get(4 - x).setText(tags.get(x).getName());
            }
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
            content.put(cardDataFormat, List.of(this.getItem().getId(),
                    this.getItem().getParentCardList().getId()));
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
                //We can opt for passing just a Card ID and retrieve
                // both the Card and CardList later
                @SuppressWarnings("unchecked")
                List<Long> ids = (List<Long>) event.getDragboard().getContent(cardDataFormat);

                if (ids.get(0) != this.getItem().getId()) {
                    if (Objects.equals(ids.get(1), this.getItem().getParentCardList().getId())) {
                        dragCardToIdentical(ids);
                    } else {
                        dragCardToDifferent(ids);
                    }
                }
            }

            event.setDropCompleted(true);

            event.consume();
        });
    }

    /**
     * Handles drag-and-drop gesture between Cards of the same CardList
     * @param ids the Card that the gesture origins from
     */
    public void dragCardToIdentical(List<Long> ids) {
        Board board = mainCtrl.getBoardViewCtrl().getBoard();
        long parentId = this.getItem().getParentCardList().getId();
        server.moveCard(parentId,
                List.of(server.getCardById(ids.get(0)), this.getItem()));
        mainCtrl.getBoardViewCtrl().refresh();
    }

    /**
     * Handles drag-and-drop gesture between Cards of different CardLists
     * This method may be improved at a later point
     * @param ids the Card that the gesture origins from
     */
    public void dragCardToDifferent(List<Long> ids) {
        Board board = mainCtrl.getBoardViewCtrl().getBoard();
        int oldParentIndex = board.getList().indexOf(server.getCL(
                ids.get(1)));
        int newParentIndex = board.getList().indexOf(server.getCL(
                this.getItem().getParentCardList().getId()));
        CardList oldParent = server.getCL(ids.get(1));
        CardList newParent = this.getItem().getParentCardList();
        server.updateParent(ids.get(0), List.of(oldParent, newParent));
        CardList newCardList = server.getCL(newParent.getId());
        board.getList().set(oldParentIndex, server.getCL(oldParent.getId()));
        board.getList().set(newParentIndex, newCardList);
        this.getItem().setParentCardList(newCardList);

        dragCardToIdentical(ids);
    }
}