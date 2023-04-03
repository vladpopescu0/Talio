package client.scenes;

import client.utils.ServerUtils;
import client.utils.SocketHandler;
import commons.*;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

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

    private ColorScheme colorSchemeCustom;
    private FXMLLoader fxmlLoader;
    private Board board;
    private CardListCell parent;
    private MainCtrl mainCtrl;
    private final SocketHandler socketHandler = new SocketHandler(ServerUtils.getServer());
    private ServerUtils server;

    private FadeTransition fadeTransition;

    /**
     * useful dependencies for universal variables and server communication
     * @param server the utils where the connection to the apis is
     * @param mainCtrl the controller of the whole application
     * @param cardList the cardListCell in which this card is
     * @param board the board the card belongs to
     * @param colorScheme the colorscheme of this card
     * @param parent the parent CardListCell of this CardCell
     */
    public CardCell(MainCtrl mainCtrl, ServerUtils server, CardListCell cardList,
                    Board board, ColorScheme colorScheme, CardListCell parent) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.board = board;
        this.parent = parent;
        if(this.getItem()!=null){
            this.getItem().setParentCardList(cardList.getItem());
            //statusLabel.setText(this.getItem().tasksLabel());
            statusLabel.setText("AAAA");
        }
        this.colorSchemeCustom = colorScheme;
    }

    /**
     * Enable drag-and-drop upon initialization and test whether the
     * card has a description
     */
    public void initialize() {
        handleDraggable();
        handleHover();
        hasDesc.setVisible(this.getItem() != null && this.getItem().hasDescription());
        statusLabel.setText(this.getItem().tasksLabel());
        cardPane.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                showDetails();
            }
        });
    }

    /**
     * Adds support for keyboard shortcuts
     */
    private void handleShortcuts(KeyEvent event) {
        switch(event.getCode()) {
            case A:
                System.out.println(mainCtrl.getFocusedNode());
                break;
            case C:
                mainCtrl.getBoardViewCtrl().toCustomizationPage();
                break;
            case T:
                mainCtrl.getCardDetailsViewCtr().addTagsShortcut(board, this.getItem());
                break;
            case E:
                editCard();
                break;
            case ENTER:
                showDetails();
                break;
            case DELETE:
            case BACK_SPACE:
                deleteCard();
                break;
            case UP:
                if (event.isShiftDown()) {
                    parent.swapCards(this.getItem(), true);
                } else {
                    parent.changeCardFocus(this.getItem(), true);
                }
                break;
            case DOWN:
                if (event.isShiftDown()) {
                    parent.swapCards(this.getItem(), false);
                } else {
                    parent.changeCardFocus(this.getItem(), false);
                }
                break;
        }
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
                    this.editButton.setOnAction(event -> editCard());
                    this.deleteButton.setOnAction(event -> deleteCard());
                    this.setOnKeyPressed(this::handleShortcuts);
                    if(!hasDefault()){
                        this.colorSchemeCustom=this.getItem().getColors();
                    }
                    hasDesc.setStyle("-fx-text-fill:" + colorSchemeCustom.getColorFont() + ";");
                    statusLabel.setStyle("-fx-text-fill:" + colorSchemeCustom.getColorFont() + ";");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            paneLabel.setText(mainCtrl.getFocusedNode().equals(this)?
                    card.getName() + " (S)" : card.getName());
            setAnim();
            if(mainCtrl.getFocusedNode().equals(this)){
                fadeTransition.play();
            }else{
                fadeTransition.jumpTo(Duration.ZERO);
                fadeTransition.stop();
            }
            setStyle("-fx-background-color:" + colorSchemeCustom.getColorBGdark() + ";");
            setText(null);
            setGraphic(cardPane);
            cardPane.setStyle("-fx-background-color:" + colorSchemeCustom.getColorBGdark() + ";");
            paneLabel.setStyle("-fx-text-fill:" + colorSchemeCustom.getColorFont() + ";");
            String lighter = mainCtrl.colorToHex(Color
                    .valueOf(colorSchemeCustom.getColorBGdark()).brighter());
            mainCtrl.setButtonStyle(deleteButton,lighter,colorSchemeCustom.getColorFont());
            mainCtrl.setButtonStyle(editButton,lighter,colorSchemeCustom.getColorFont());
        }
    }

    /**
     * Opens the edit Card panel
     */
    public void editCard() {
        mainCtrl.setCardId(this.getItem().getId());
        mainCtrl.showEditCard();
    }

    /**
     * Deletes the Card
     */
    public void deleteCard() {
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
    }

    /**
     * Shows the Card details panel
     */
    public void showDetails() {
        mainCtrl.closeSecondaryStage();
        //otherwise the board will have empty lists
        mainCtrl.showCardDetailsView(this.getItem(), board);
    }

    /**
     * Displays Tags attached to the Card
     */
    private void displayTags() {
        List<Tag> tags = this.getItem().getTags();
        List<Label> labels = List.of(tagLabel1, tagLabel2, tagLabel3, tagLabel4, tagLabel5);

        if (tags.size() <= 5) {
            for(int x = 0; x < tags.size(); x++) {
                labels.get(tags.size() - x - 1).setText(tags.get(x).getName());
                labels.get(tags.size() - x - 1)
                        .setStyle("-fx-text-fill: "+tags.get(x).getColor()+";"
                        +"-fx-border-color: "+tags.get(x).getColor()+";");
            }
            for(int x = tags.size(); x < 5; x++) {
                labels.get(x).setText(null);
            }
        } else {
            tagLabel1.setText("+" + (tags.size() - 4) + " more");
            tagLabel1.setStyle("-fx-text-fill: "+"white"+"; "
                    +"-fx-border-color: "+"white"+";");
            for(int x = 0; x < 4; x++) {
                labels.get(4 - x).setText(tags.get(x).getName());
                labels.get(4 - x).setStyle("-fx-text-fill: "+tags.get(x).getColor()+"; "
                        +"-fx-border-color: "+tags.get(x).getColor()+";");
            }
        }
    }

    /**
     * Handles drag-and-drop gestures for Card
     * Object equality is handled by ID equality as Serialized Objects may differ
     * and yield false for equals method
     */
    private void handleDraggable() {
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
     * Handles hovering gesture for mouse
     */
    private void handleHover() {
        this.setOnMouseEntered(event -> {
            Node oldFocus = mainCtrl.getFocusedNode();
            this.requestFocus();
            updateItem(this.getItem(), false);
            if (oldFocus instanceof CardCell) {
                CardCell oldFocusCell = (CardCell) oldFocus;
                oldFocusCell.updateItem(oldFocusCell.getItem(), false);
            }
        });

        this.setOnMouseClicked(event -> {
            Node oldFocus = mainCtrl.getFocusedNode();
            this.requestFocus();
            updateItem(this.getItem(), false);
            if (oldFocus instanceof CardCell) {
                CardCell oldFocusCell = (CardCell) oldFocus;
                oldFocusCell.updateItem(oldFocusCell.getItem(), false);
            }
        });
    }

    /**
     * Handles drag-and-drop gesture between Cards of the same CardList
     * @param ids the Card that the gesture origins from
     */
    private void dragCardToIdentical(List<Long> ids) {
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
    private void dragCardToDifferent(List<Long> ids) {
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

    /**
     * Method that checks whether the card has a default color scheme
     * @return true if the card has a default color scheme, false otherwise
     */
    private boolean hasDefault(){
        ColorScheme c = this.getItem().getColors();
        if(c==null){
            return true;
        }
        return Objects.equals(c.getColorFont(), "black")
                && Objects.equals(c.getColorLighter(), "black");
    }

    /**
     * sets a fade animation if the variable is null
     */
    private void setAnim(){
        if(fadeTransition==null){
            fadeTransition = new FadeTransition(Duration.millis(1000));
            fadeTransition.setNode(this);
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0.3);
            fadeTransition.setCycleCount(200);
            fadeTransition.setAutoReverse(true);
        }
    }
}