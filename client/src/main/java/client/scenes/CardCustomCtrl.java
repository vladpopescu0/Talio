package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.ColorPair;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class CardCustomCtrl extends ListCell<ColorPair> {

    private final CustomizationPageCtrl parent;
    private ServerUtils server;
    private MainCtrl mainCtrl;

    private Board board;

    @FXML
    private Button delete;

    @FXML
    private Text label;

    @FXML
    private ColorPicker presBG;

    @FXML
    private ColorPicker presFont;

    @FXML
    private Pane presetsPane;

    @FXML
    private Button set;

    private FXMLLoader fxmlLoader;

    /**
     * Constructor for the Task Cell class
     * @param mainCtrl the mainCtrl used
     * @param server the serverUtils
     * @param customizationPageCtrl the parent board
     */

    public CardCustomCtrl(MainCtrl mainCtrl, ServerUtils server,
                          CustomizationPageCtrl customizationPageCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.parent = customizationPageCtrl;
//        this.board = server.getBoardByID(customizationPageCtrl.getBoard().getId());
    }

    /**
     * Initialize the task cell
     */
    public void initialize() {
        try{
            if (this.getItem() != null) {
                this.board = server.getBoardByID(parent.getBoard().getId());
//                int i = board.getColors().indexOf(this.getItem());
                presBG.setVisible(true);
                presFont.setVisible(true);
//                presBG.setValue(Color.valueOf(board.getColors().get(i).getColorBG()));
//                presFont.setValue(Color.valueOf(board.getColors().get(i).getColorFont()));
                set.setVisible(true);
                delete.setVisible(true);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(ColorPair colors, boolean empty) {
        super.updateItem(colors, empty);
//trebuie sa fac o metoda getbyid pt colorpair
        if (empty || colors == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("CardCustomView.fxml"));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
//                    int i = board.getColors().indexOf(this.getItem());
                    label.setText("Preset");
                    this.presFont.setOnAction(event -> saveFont());
                    this.presBG.setOnAction(event -> saveBG());
                    this.set.setOnAction(
                            event -> selectPreset());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            setText(null);
            setGraphic(presetsPane);
        }
    }

    /**
     * Saves the selected color in the colopicker
     */
    public void saveBG() {
        int i = board.getColors().indexOf(this.getItem());
        this.getItem().setColorBG(mainCtrl.colorToHex(presBG.getValue()));
        server.updateColorPair(this.getItem());
//        board.getColors().set(i,new ColorPair(mainCtrl.colorToHex(presBG.getValue()),
//        mainCtrl.colorToHex(presFont.getValue())));
//        server.addBoard(board);
        this.board = server.getBoardByID(board.getId());
    }
    /**
     * Saves the selected color in the colopicker
     */
    public void saveFont() {
        int i = board.getColors().indexOf(this.getItem());
        board.getColors().set(i,new ColorPair(mainCtrl.colorToHex(presBG.getValue())
                ,mainCtrl.colorToHex(presFont.getValue())));
        server.addBoard(board);
    }

    /**
     * Selects the respective preset
     */
    public void selectPreset() {
        int i = board.getColors().indexOf(this.getItem());
        board.getColors().set(i, new ColorPair(mainCtrl.colorToHex(presBG.getValue()),
                mainCtrl.colorToHex(presFont.getValue())));
        board.setCardsBGColor(mainCtrl.colorToHex(presBG.getValue()));
        board.setCardsFontColor(mainCtrl.colorToHex(presFont.getValue()));
        this.board = server.addBoard(board);
    }
}
