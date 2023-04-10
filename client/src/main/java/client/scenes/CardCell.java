package client.scenes;

import client.utils.ServerUtils;
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
import javafx.util.Duration;

import java.util.List;

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
    private ServerUtils server;

    private FadeTransition fadeTransition;

    private boolean unlocked;
    private int location=1;

    /**
     * useful dependencies for universal variables and server communication
     * @param server the utils where the connection to the apis is
     * @param mainCtrl the controller of the whole application
     * @param cardList the cardListCell in which this card is
     * @param board the board the card belongs to
     * @param colorScheme the colorscheme of this card
     * @param unlocked whether it is unlocked
     */
    public CardCell(MainCtrl mainCtrl, ServerUtils server, CardListCell cardList,
                    Board board, ColorScheme colorScheme, boolean unlocked) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.board = board;
        this.parent = cardList;
        if(this.getItem()!=null){
            this.getItem().setParentCardList(cardList.getItem());
//            //statusLabel.setText(this.getItem().tasksLabel());
//            statusLabel.setText("AAAA");
        }
        this.colorSchemeCustom = colorScheme;
        this.unlocked = unlocked;
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
        if (unlocked) {
            switch(event.getCode()) {
                case C:
                    mainCtrl.showCardPresetShortcut(this.getItem());
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
            setStyle("-fx-background-color:" + "transparent" + ";");
        } else {
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("CardView.fxml"));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                    displayTags();
                    this.setOnKeyPressed(this::handleShortcuts);
                    if(!hasDefault()){
                        this.colorSchemeCustom = this.getItem().getColors();
                    }
                    paneLabel.setText(this.getItem().getName());
                    hasDesc.setStyle("-fx-text-fill:" + colorSchemeCustom.getColorFont() + ";");
                    statusLabel.setStyle("-fx-text-fill:" + colorSchemeCustom.getColorFont() + ";");

                    this.editButton.setOnAction(event -> {
                        mainCtrl.setCardId(this.getItem().getId());
                        mainCtrl.showEditCard();
                    });

                    this.deleteButton.setOnAction(event ->{
                        deleteCard();
//                        mainCtrl.getCardDetailsViewCtr().setCard(null);
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            setAnim();
            focusChange(card);
            paneLabel.setText(card.getName());

            setStyle("-fx-background-color:" + "transparent"
                    + "; -fx-effect: dropshadow(gaussian, #333334, 12, 0.05, 0.0, 2);");
            setText(null);
            setGraphic(cardPane);
            cardPane.setStyle("-fx-background-color:"
                    +colorSchemeCustom.getColorBGlight()+";");
            paneLabel.setStyle("-fx-text-fill:" + colorSchemeCustom.getColorFont() + ";");

            if (!unlocked) {
                editButton.setVisible(false);
                deleteButton.setVisible(false);
            }
        }
    }

    private void focusChange(Card card) {
        if (mainCtrl.getFocusedNode() instanceof CardCell) {
            CardCell cardCell = (CardCell) mainCtrl.getFocusedNode();
            if (cardCell!=null && this.getItem()!=null && cardCell.getItem() != null &&
                    this.getItem().getId() == cardCell.getItem().getId()) {
                if(location==1){
                    fadeTransition.setFromValue(1);
                    fadeTransition.setToValue(0.65);
                    fadeTransition.play();
                    fadeTransition.setOnFinished(event -> {
                        location--;
                        //Recursively calling the method to check whether the
                        // focus changed between animation changes
                        //this way any animation can be applied smoothly
                        focusChange(card);
                    });
                }
                this.requestFocus();
            } else {
                if(location==0){
                    fadeTransition.setFromValue(0.65);
                    fadeTransition.setToValue(1);
                    fadeTransition.play();
                    fadeTransition.setOnFinished(event -> {
                        location++;
                        focusChange(card);
                    });
                }
            }
        } else {
            if(location==0){
                fadeTransition.setFromValue(0.65);
                fadeTransition.setToValue(1);
                fadeTransition.play();
                fadeTransition.setOnFinished(event -> {
                    location++;
                    focusChange(card);
                });
            }
        }
    }

    /**
     * Opens the edit Card panel
     */
    public void editCard() {
        mainCtrl.setCardId(this.getItem().getId());
        mainCtrl.showEditCard();
        mainCtrl.getBoardViewCtrl().refocusFromBackup();
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
        mainCtrl.getBoardViewCtrl().refocusFromBackup();
    }

    /**
     * Shows the Card details panel
     */
    public void showDetails() {
        mainCtrl.closeSecondaryStage();
        //otherwise the board will have empty lists
        mainCtrl.showCardDetailsView(this.getItem(), board, unlocked);
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
                        .setStyle("-fx-text-fill: "+tags.get(x).getColor()+";");
            }
            for(int x = tags.size(); x < 5; x++) {
                labels.get(x).setText(null);
            }
        } else {
            tagLabel1.setText("+" + (tags.size() - 4) + " more");
            tagLabel1.setStyle("-fx-text-fill: "+"white"+";");
            for(int x = 0; x < 4; x++) {
                labels.get(4 - x).setText(tags.get(x).getName());
                labels.get(4 - x).setStyle("-fx-text-fill: "+tags.get(x).getColor()+";");
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
            if (unlocked) {
                Dragboard db = this.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.put(cardDataFormat, this.getItem().getId());
                db.setContent(content);
                WritableImage snapshot = this.snapshot(new SnapshotParameters(), null);
                db.setDragView(snapshot);
            }

            event.consume();
        });

        this.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.ANY);

            event.consume();
        });

        this.setOnDragDropped(event -> {
            if (event.getDragboard().hasContent(cardDataFormat)) {
                long id = (long) event.getDragboard().getContent(cardDataFormat);
                if (id != this.getItem().getId()) {
                    dragCard(id);
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
            if (mainCtrl.isPrimaryStageFocused() && unlocked) {
                Node oldFocus = mainCtrl.getFocusedNode();
                this.requestFocus();
                mainCtrl.getBoardViewCtrl().setFocusedNodeBackup(this);
                updateItem(this.getItem(), false);
                if (oldFocus instanceof CardCell) {
                    CardCell oldFocusCell = (CardCell) oldFocus;
                    oldFocusCell.updateItem(oldFocusCell.getItem(), false);
                }
            }
        });

        this.setOnMouseClicked(event -> {
            if (unlocked) {
                Node oldFocus = mainCtrl.getFocusedNode();
                this.requestFocus();
                mainCtrl.getBoardViewCtrl().setFocusedNodeBackup(this);
                updateItem(this.getItem(), false);
                if (oldFocus instanceof CardCell) {
                    CardCell oldFocusCell = (CardCell) oldFocus;
                    oldFocusCell.updateItem(oldFocusCell.getItem(), false);
                }
            }
        });
    }

    /**
     * Handles drag-and-drop gesture between two Cards
     * @param id the ID of the Card that the gesture origins from
     */
    private void dragCard(long id) {
        server.moveCard(List.of(id, this.getItem().getId()));
        mainCtrl.getBoardViewCtrl().refresh();
    }

    /**
     * Method that checks whether the card has a default color scheme
     * @return true if the card has a default color scheme, false otherwise
     */
    private boolean hasDefault(){
        ColorScheme c = this.getItem().getColors();
        return c==null || !board.getCardsColorSchemesList().contains(c);
    }

    /**
     * sets a fade animation if the variable is null
     */
    private void setAnim(){
        if(fadeTransition==null){
            fadeTransition = new FadeTransition(Duration.millis(1000));
            fadeTransition.setNode(this);
            fadeTransition.setFromValue(1);
            fadeTransition.setToValue(0.65);
            fadeTransition.setCycleCount(1);
            fadeTransition.setAutoReverse(true);
        }
    }
}