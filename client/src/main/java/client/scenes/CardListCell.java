package client.scenes;

import com.google.inject.Inject;
import commons.Board;
import client.utils.ServerUtils;
import commons.Card;
import commons.CardList;
import commons.ColorScheme;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.skin.VirtualFlow;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.*;

import static client.scenes.MainCtrl.cardDataFormat;

public class CardListCell extends ListCell<CardList>{

    private final MainCtrl mainCtrl;
    @FXML
    private Button editListButton;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button deleteList;

    @FXML
    private TitledPane titledPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ListView<Card> cardsList;

    @FXML
    private Button addCardButton;

    private ObservableList<Card> cardObservableList;

    private ColorScheme colorScheme;
    private FXMLLoader fxmlLoader;
    private ServerUtils server;

    private Board board;

    private boolean unlocked;

    /**
     * useful dependencies for universal variables and server communication
     * @param serverUtils           the utils where the connection to the apis is
     * @param mainCtrl              the controller of the whole application
     * @param board the board to which the cardList belongs
     * @param unlocked whether it is unlocked
     **/
    @Inject
    public CardListCell(MainCtrl mainCtrl, ServerUtils serverUtils, Board board, boolean unlocked) {
        this.server = serverUtils;
        this.mainCtrl = mainCtrl;
        this.board = board;
        this.colorScheme= board.getListsColorScheme();
        this.unlocked = unlocked;
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
            setStyle("-fx-background-color: "+board.getColorScheme().getColorBGlight()+";");
        } else {
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass()
                        .getResource("CardListView.fxml"));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                    titledPane.setOnMouseClicked(event ->
                            mainCtrl.getBoardViewCtrl().refocusFromBackup());
                    cardsList.setFocusTraversable(false);
                    Platform.runLater(() -> {
                        Pane title = (Pane) titledPane.lookup(".title");
                        if (title != null) {
                            title.setStyle("-fx-background-color: "
                                    +colorScheme.getColorBGlight()+";");
                        }
                    });
                    editListButton.setOnAction(event -> {
                        rename(cardList.getId());
                    });
                    deleteList.setOnAction(event -> {
                        delete(cardList.getId());
                    });
                    addCardButton.setOnAction(event -> {
                        mainCtrl.setId(this.getItem().getId());
                        mainCtrl.showAddCard();
                        mainCtrl.getBoardViewCtrl().refocusFromBackup();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (!unlocked) {
                editListButton.setVisible(false);
                deleteList.setVisible(false);
                addCardButton.setVisible(false);
            }
            titledPane.setText(cardList.getName());
            refresh();
            setGraphic(titledPane);
            setStyles();
        }
    }
    /**
     * sets the colors for the given list
     */
    public void setStyles(){
        titledPane.setStyle(" -fx-text-fill: "
                + colorScheme.getColorFont() + ";"
                +"-fx-border-width: 0;");
        anchorPane.setStyle("-fx-background-color: "
                +colorScheme.getColorBGdark()+";"
                + "-fx-text-fill:" + colorScheme.getColorFont() + ";"
                +"-fx-border-width: 0;");
        cardsList.setStyle("-fx-background-color: "
                +colorScheme.getColorBGlight()+";"
                +"-fx-border-width: 0;");
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
            CardCell card = new CardCell(mainCtrl, server,
                    this,board,board.getCardsColorScheme(), unlocked);
            return card ;
        });
    }

    /** Helper method for renaming a cardlist
     * @param id the id of the cardList whose name will be modified
     */
    public void rename(Long id) {
        mainCtrl.showChangeListName(id);
        mainCtrl.getBoardViewCtrl().refocusFromBackup();
    }

    /** Helper method for renaming a cardlist
     * @param id the id of the cardList which will be deleted
     */
    public void delete(Long id) {
        Board b = mainCtrl.getBoardViewCtrl().getBoard();
        server.removeCL(id);

        if (mainCtrl.isSecondaryFromCardListCell(this.getItem())) {
            mainCtrl.closeSecondaryStage();
        } else {
            if (this.getItem().getCards().stream()
                    .anyMatch(c -> c.getId() == mainCtrl.getCardId())) {
                if (mainCtrl.isSecondaryFromCardCell(this.getItem().getCards().stream()
                        .filter(c -> c.getId() == mainCtrl.getCardId()).findFirst().get())) {
                    mainCtrl.closeSecondaryStage();
                }
            }
        }

        mainCtrl.showBoardView(b);
        mainCtrl.getBoardViewCtrl().refocusFromBackup();
    }

    /**
     * Changes the focus to an adjacent Card above or below
     * This method represents navigation between the Cards using the keyboard
     * @param card currently selected Card
     * @param up whether the change should be made upwards
     */
    public void changeCardFocus(Card card, boolean up) {
        int index = cardsList.getItems().indexOf(card);
        Node oldFocus = mainCtrl.getFocusedNode();
        VirtualFlow virtualFlow = (VirtualFlow) cardsList.lookup(".virtual-flow");
        if (up && index > 0) {
            virtualFlow.getCell(index - 1).requestFocus();

            if (mainCtrl.getFocusedNode() instanceof CardCell) {
                CardCell newFocusCell = (CardCell) mainCtrl.getFocusedNode();
                mainCtrl.getBoardViewCtrl().setFocusedNodeBackup(newFocusCell);
                newFocusCell.updateItem(newFocusCell.getItem(), false);
            }
            if (oldFocus instanceof CardCell) {
                CardCell oldFocusCell = (CardCell) oldFocus;
                oldFocusCell.updateItem(oldFocusCell.getItem(), false);
            }
        } else if (!up && index + 1 < cardsList.getItems().size()) {
            virtualFlow.getCell(index + 1).requestFocus();

            if (mainCtrl.getFocusedNode() instanceof CardCell) {
                CardCell newFocusCell = (CardCell) mainCtrl.getFocusedNode();
                mainCtrl.getBoardViewCtrl().setFocusedNodeBackup(newFocusCell);
                newFocusCell.updateItem(newFocusCell.getItem(), false);
            }
            if (oldFocus instanceof CardCell) {
                CardCell oldFocusCell = (CardCell) oldFocus;
                oldFocusCell.updateItem(oldFocusCell.getItem(), false);
            }
        }
    }

    /**
     * Swaps the currently selected Card with an adjacent Card above or below
     * This method represents swapping Cards using the keyboard
     * @param card currently selected Card
     * @param up whether the swap should be made upwards
     */
    public void swapCards(Card card, boolean up) {
        int index = cardsList.getItems().indexOf(card);
        if (up && index > 0) {
            server.moveCard(List.of(card.getId(), cardsList.getItems().get(index - 1).getId()));
            mainCtrl.getBoardViewCtrl().refresh();
        } else if (!up && index + 1 < cardsList.getItems().size()) {
            server.moveCard(List.of(card.getId(), cardsList.getItems().get(index + 1).getId()));
            mainCtrl.getBoardViewCtrl().refresh();
        }
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
                long id = (long) event.getDragboard().getContent(cardDataFormat);
                dragCardToCardList(id);
            }

            event.setDropCompleted(true);

            event.consume();
        });
    }

    /**
     * Handles drag-and-drop gesture from Card to CardList
     * @param id the ID of the Card that the gesture origins from
     */
    public void dragCardToCardList(long id) {
        server.moveCardToCardList(this.getItem().getId(), id);
        mainCtrl.getBoardViewCtrl().refresh();
    }
}