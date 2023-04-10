package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import commons.ColorScheme;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class PresetDetailsCtrl extends ListCell<ColorScheme> {

    private final CardDetailsViewCtr parent;

    @FXML
    private Pane presetsPane;

    private final MainCtrl mainCtrl;

    private final ServerUtils server;

    private FXMLLoader fxmlLoader;

    private Board board;
    private Card card;

    @FXML
    private Button set;

    @FXML
    private Circle colorBG;

    @FXML
    private Circle colorFont;

    /**
     * constructor for the custom component, no inject needed
     * @param mainCtrl of the application
     * @param server utils of the endpoint connection
     * @param parent where this scene is incorporated in
     * @param board parent
     * @param card where to apply changes to
     */
    public PresetDetailsCtrl(MainCtrl mainCtrl, ServerUtils server,
                          CardDetailsViewCtr parent, Board board, Card card) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.parent = parent;
        this.board = board;
        this.card = card;
    }

    /**
     *initialized the item, not useful implementations yet
     */
    public void initialize() {
        try{
            if (this.getItem() != null) {
                //this.board = server.getBoardByID(parent.getBoard().getId());
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    /**
     * Update override method, sets the scene as preseted
     * @param colors The new color for the cell.
     * @param empty whether or not this cell represents data from the list. If it
     *        is empty, then it does not represent any domain data, but is a cell
     *        being used to render an "empty" row.
     */
    @Override
    protected void updateItem(ColorScheme colors, boolean empty) {
        super.updateItem(colors, empty);
        if (empty || colors == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("PresetCardDetails.fxml"));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                    if(!parent.getUnlocked()){
                        set.setVisible(false);
                    }
                    this.set.setOnAction(event -> setColorScheme());
                    colorBG.setStyle("-fx-fill: "+this.getItem().getColorBGlight()+";");
                    colorFont.setStyle("-fx-fill: "+this.getItem().getColorFont()+";");
                    if(card!=null && card.getColors()!=null
                            && card.getColors().equals(this.getItem())){
                        setStyle("-fx-border-color: red;");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            setText(null);
        }
    }

    /**
     * sets a new color to that card in the database
     */
    public void setColorScheme(){
        card = server.getCardById(card.getId());
        card.setColors(this.getItem());
        server.updateCardDetails(card);
        parent.getCard().setColors(this.getItem());
        parent.refresh();
    }
}
