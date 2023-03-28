package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.ColorPair;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CustomizationPageCtrl implements Initializable {

    private final MainCtrl mainCtrl;

    private final ServerUtils server;

    private Board board;

    @FXML
    private Button addPreset;

    private ObservableList<ColorPair> colorPairObservableList;
    @FXML
    private ListView<ColorPair> colorPairList;

    @FXML
    private ColorPicker boardFont;

    @FXML
    private ColorPicker listFont;

    @FXML
    private ColorPicker boardBG;

    @FXML
    private ColorPicker listBG;

    @FXML
    private ColorPicker pres1BG;

    @FXML
    private ColorPicker pres1Font;

    @FXML
    private ColorPicker pres2BG;

    @FXML
    private ColorPicker pres2Font;

    @FXML
    private ColorPicker pres3BG;

    @FXML
    private ColorPicker pres3Font;

    /**
     * Constructor for the CustomizationPageCtrl class
     * @param server the server to be used
     * @param mainCtrl the mainCtrl of the application
     * @param board the board that is customized
     */
    @Inject
    public CustomizationPageCtrl(MainCtrl mainCtrl,
                                 Board board, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.board = board;
        this.server = server;
    }

    /**
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<ColorPair> colors = (board == null || board.getColors() == null ?
                new ArrayList<>() : board.getColors());
        colorPairObservableList = FXCollections.observableList(colors);
        colorPairList.setItems(colorPairObservableList);
        colorPairList.setCellFactory(t -> new CardCustomCtrl(mainCtrl, server, this));
    }

    /** Sets the board to be customized
     * @param board the board that is going to be customized
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Updates the tasks and description
     */
    public void refresh() {
        List<ColorPair> colors = (board == null || board.getColors() == null ?
                new ArrayList<>() : board.getColors());
        colorPairObservableList = FXCollections.observableList(colors);
        colorPairList.setItems(colorPairObservableList);
        colorPairList.setCellFactory(t -> new CardCustomCtrl(mainCtrl, server, this));
    }

    /**
     * Customizes the board, list and cards
     */
    public void customizeBoard() {
        this.board.setColorLighter(mainCtrl.colorToHex(boardBG.getValue().brighter()));
        this.board.setColorBGlight(mainCtrl.colorToHex(boardBG.getValue()));
        this.board.setColorBGdark(mainCtrl.colorToHex(boardBG.getValue().darker()));

        this.board.setCardsBGColor(board.getCardsBGColor());
        this.board.setCardsFontColor(board.getCardsFontColor());
        boardBG.setValue(boardBG.getValue());
        this.board.setColorFont(mainCtrl.colorToHex(boardFont.getValue()));
        boardFont.setValue(boardFont.getValue());
        this.board = server.addBoard(board);
        mainCtrl.showBoardView(board);
    }

    /**
     * @return the colopicker for the board font
     */
    public ColorPicker getBoardFont() {
        return boardFont;
    }

    /**
     * @return the colopicker for the board background
     */
    public ColorPicker getBoardBG() {
        return boardBG;
    }

    /**
     * Resets the board's colors
     */
    public void reset() {
        board.setColorFont(mainCtrl.colorToHex(Color.BLACK));
        board.setColorBGdark(mainCtrl.colorToHex(Color.GRAY));
        board.setColorBGlight(mainCtrl.colorToHex(Color.LIGHTGRAY));
        board.setColorLighter(mainCtrl.colorToHex(Color.LIGHTGRAY.brighter()));
        boardFont.setValue(Color.BLACK);
        boardBG.setValue(Color.LIGHTGRAY);
    }
    /**
     * Selects the first preset and makes button disappear
     */
    public void selectPreset1() {
        board.setCardsBGColor(mainCtrl.colorToHex(pres1BG.getValue()));
        board.setCardsFontColor(mainCtrl.colorToHex(pres1Font.getValue()));
        this.board = server.addBoard(board);
    }
    /**
     * Selects the second preset and makes button disappear
     */
    public void selectPreset2() {
        board.setCardsBGColor(mainCtrl.colorToHex(pres2BG.getValue()));
        board.setCardsFontColor(mainCtrl.colorToHex(pres2Font.getValue()));
        this.board = server.addBoard(board);
    }
    /**
     * Selects the third preset and makes button disappear
     */
    public void selectPreset3() {
        board.setCardsBGColor(mainCtrl.colorToHex(pres3BG.getValue()));
        board.setCardsFontColor(mainCtrl.colorToHex(pres3Font.getValue()));
        this.board = server.addBoard(board);
    }
    /**
     * Saves the selected color in the colopicker
     */
    public void saveBG1() {
        board.getPresetsBGColor().set(0, mainCtrl.colorToHex(pres1BG.getValue()));
        this.board = server.addBoard(board);
    }
    /**
     * Saves the selected color in the colopicker
     */
    public void saveFont1() {
        board.getPresetsFontColor().set(0, mainCtrl.colorToHex(pres1Font.getValue()));
        this.board = server.addBoard(board);
    }
    /**
     * Saves the selected color in the colopicker
     */
    public void saveBG2() {
        board.getPresetsBGColor().set(1, mainCtrl.colorToHex(pres2BG.getValue()));
        this.board = server.addBoard(board);
    }
    /**
     * Saves the selected color in the colopicker
     */
    public void saveFont2() {
        board.getPresetsFontColor().set(1, mainCtrl.colorToHex(pres2Font.getValue()));
        this.board = server.addBoard(board);
    }
    /**
     * Saves the selected color in the colopicker
     */
    public void saveBG3() {
        board.getPresetsBGColor().set(2, mainCtrl.colorToHex(pres3BG.getValue()));
        this.board = server.addBoard(board);
    }
    /**
     * Saves the selected color in the colopicker
     */
    public void saveFont3() {
        board.getPresetsFontColor().set(2, mainCtrl.colorToHex(pres3Font.getValue()));
        this.board = server.addBoard(board);
    }

//    /**
//     * @return the first preset for the card's background
//     */
//    public ColorPicker getPres1BG() {
//        return this.pres1BG;
//    }
//    /**
//     * @return the first preset for the card's font
//     */
//    public ColorPicker getPres1Font() {
//        return this.pres1Font;
//    }
//    /**
//     * @return the second preset for the card's background
//     */
//    public ColorPicker getPres2BG() {
//        return pres2BG;
//    }
//    /**
//     * @return the second preset for the card's font
//     */
//    public ColorPicker getPres2Font() {
//        return pres2Font;
//    }
//    /**
//     * @return the third preset for the card's background
//     */
//    public ColorPicker getPres3BG() {
//        return pres3BG;
//    }
//    /**
//     * @return the third preset for the card's font
//     */
//    public ColorPicker getPres3Font() {
//        return pres3Font;
//    }

    /**
     * @return the current board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Adds a new preset to the board
     */
    public void addPreset(){
        board.getColors().add(new ColorPair("#ffffff","#808080"));

        server.addBoard(board);

        refresh();
        this.mainCtrl.showCustomizationPage(board);
    }
}
