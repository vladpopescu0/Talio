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
        this.colorScheme= board.getListsColorScheme();
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
                fxmlLoader = new FXMLLoader(getClass()
                        .getResource("CardListView.fxml"));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                    Platform.runLater(() -> {
                        Pane title = (Pane) titledPane.lookup(".title");
                        if (title != null) {
                            title.setStyle("-fx-background-color: "
                                    +colorScheme.getColorBGdark()+";");
                            System.out.println(title.getStyle());
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
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                + colorScheme.getColorFont() + ";");
        anchorPane.setStyle("-fx-background-color: "
                +colorScheme.getColorBGdark()+";"
                + "-fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0;"
                + "-fx-background-radius: 5px;" +
                "-fx-text-fill:" + colorScheme.getColorFont() + ";");
        cardsList.setStyle("-fx-background-color: "
                +board.getCardsColorScheme().getColorBGlight()+";");
        mainCtrl.setButtonStyle(editListButton
                ,colorScheme.getColorBGlight(),colorScheme.getColorFont());
        mainCtrl.setButtonStyle(addCardButton
                ,colorScheme.getColorBGlight(),colorScheme.getColorFont());
        mainCtrl.setButtonStyle(deleteList
                ,colorScheme.getColorBGlight(),colorScheme.getColorFont());
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
            CardCell card = new CardCell(mainCtrl, server,this,board,board.getCardsColorScheme(),this);
            card.setStyle("-fx-background-color: " +
                    board.getCardsColorScheme().getColorBGlight() + ";" +
                    "\n-fx-border-color: " +
                    board.getCardsColorScheme().getColorBGlight() + ";");
            card.setOnMouseClicked(event -> Platform.runLater(() ->{
                card.setStyle("-fx-background-color:"
                        +board.getCardsColorScheme().getColorBGdark()+";" +
                        "-fx-border-color:"
                        +board.getCardsColorScheme().getColorBGdark()+";");
            }));
            card.hoverProperty().addListener(
                    (observable, oldValue, newValue) -> {
                        if (newValue) {
                            card.setStyle("-fx-background-color:"
                                    +board.getCardsColorScheme().getColorBGdark()+";" +
                                    "\n-fx-border-color:"
                                    +board.getCardsColorScheme().getColorBGdark()+";");
                        } else {
                            card.setStyle("-fx-background-color:"
                                    +board.getCardsColorScheme().getColorBGlight()+";" +
                                    "\n-fx-border-color:"
                                    +board.getCardsColorScheme().getColorBGlight()+";");
                        }
                    });

            return card ;
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
    }

    /**
     * Changes the focus to an adjacent Card above or below
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
     * @param card currently selected Card
     * @param up whether the swap should be made upwards
     */
    public void swapCards(Card card, boolean up) {
        int index = cardsList.getItems().indexOf(card);
        VirtualFlow virtualFlow = (VirtualFlow) cardsList.lookup(".virtual-flow");
        if (up && index > 0) {
            server.moveCard(this.getItem().getId(),
                    List.of(card, cardsList.getItems().get(index - 1)));

            mainCtrl.getBoardViewCtrl().refresh();
            virtualFlow.getCell(index - 1).requestFocus();
        } else if (!up && index + 1 < cardsList.getItems().size()) {
            server.moveCard(this.getItem().getId(),
                    List.of(card, cardsList.getItems().get(index + 1)));

            mainCtrl.getBoardViewCtrl().refresh();
            virtualFlow.getCell(index + 1).requestFocus();
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
}