package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

public class CustomizationPageCtrl {

    private final MainCtrl mainCtrl;

    private final ServerUtils server;

    private Board board;

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

    /** Sets the board to be customized
     * @param board the board that is going to be customized
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * method that assign specific colors
     * to a board and the components of it
     */
    public void customizeBoard(){
        this.board.getColorScheme()
                .setColorLighter(mainCtrl.colorToHex(boardBG.getValue().brighter()));
        this.board.getColorScheme()
                .setColorBGlight(mainCtrl.colorToHex(boardBG.getValue()));
        this.board.getColorScheme()
                .setColorBGdark(mainCtrl.colorToHex(boardBG.getValue().darker()));
        this.board.getColorScheme()
                .setColorFont(mainCtrl.colorToHex(boardFont.getValue()));

        this.board.getListsColorScheme()
                .setColorLighter(mainCtrl.colorToHex(listBG.getValue().brighter()));
        this.board.getListsColorScheme()
                .setColorBGlight(mainCtrl.colorToHex(listBG.getValue()));
        this.board.getListsColorScheme()
                .setColorBGdark(mainCtrl.colorToHex(listBG.getValue().darker()));
        this.board.getListsColorScheme()
                .setColorFont(mainCtrl.colorToHex(listFont.getValue()));
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
        board.getColorScheme()
                .setColorFont(mainCtrl.colorToHex(Color.WHITE));
        board.getColorScheme()
                .setColorBGdark(mainCtrl.colorToHex(Color.BLACK));
        board.getColorScheme()
                .setColorBGlight(mainCtrl.colorToHex(Color.BLACK));
        board.getColorScheme()
                .setColorLighter(mainCtrl.colorToHex(Color.GRAY));
        boardFont.setValue(Color.WHITE);
        boardBG.setValue(Color.BLACK);
    }
    /**
     * Selects the first preset and makes button disappear
     */
    public void selectPreset1() {
        board.getCardsColorScheme()
                .setColorBGdark(mainCtrl.colorToHex(pres1BG.getValue()));
        board.getCardsColorScheme()
                .setColorFont(mainCtrl.colorToHex(pres1Font.getValue()));
        this.board = server.addBoard(board);
    }
    /**
     * Selects the second preset and makes button disappear
     */
    public void selectPreset2() {
        board.getCardsColorScheme()
                .setColorBGdark(mainCtrl.colorToHex(pres2BG.getValue()));
        board.getCardsColorScheme()
                .setColorFont(mainCtrl.colorToHex(pres2Font.getValue()));
        this.board = server.addBoard(board);
    }
    /**
     * Selects the third preset and makes button disappear
     */
    public void selectPreset3() {
        board.getCardsColorScheme()
                .setColorBGdark(mainCtrl.colorToHex(pres3BG.getValue()));
        board.getCardsColorScheme()
                .setColorFont(mainCtrl.colorToHex(pres3Font.getValue()));
        this.board = server.addBoard(board);
    }
    /**
     * Saves the selected color in the colopicker
     */
    public void saveBG1() {
        board.getPresetsBGColor()
                .set(0, mainCtrl.colorToHex(pres1BG.getValue()));
        this.board = server.addBoard(board);
    }
    /**
     * Saves the selected color in the colopicker
     */
    public void saveFont1() {
        board.getPresetsFontColor()
                .set(0, mainCtrl.colorToHex(pres1Font.getValue()));
        this.board = server.addBoard(board);
    }
    /**
     * Saves the selected color in the colopicker
     */
    public void saveBG2() {
        board.getPresetsBGColor()
                .set(1, mainCtrl.colorToHex(pres2BG.getValue()));
        this.board = server.addBoard(board);
    }
    /**
     * Saves the selected color in the colopicker
     */
    public void saveFont2() {
        board.getPresetsFontColor()
                .set(1, mainCtrl.colorToHex(pres2Font.getValue()));
        this.board = server.addBoard(board);
    }
    /**
     * Saves the selected color in the colopicker
     */
    public void saveBG3() {
        board.getPresetsBGColor()
                .set(2, mainCtrl.colorToHex(pres3BG.getValue()));
        this.board = server.addBoard(board);
    }
    /**
     * Saves the selected color in the colopicker
     */
    public void saveFont3() {
        board.getPresetsFontColor()
                .set(2, mainCtrl.colorToHex(pres3Font.getValue()));
        this.board = server.addBoard(board);
    }

    /**
     * @return the first preset for the card's background
     */
    public ColorPicker getPres1BG() {
        return this.pres1BG;
    }
    /**
     * @return the first preset for the card's font
     */
    public ColorPicker getPres1Font() {
        return this.pres1Font;
    }
    /**
     * @return the second preset for the card's background
     */
    public ColorPicker getPres2BG() {
        return pres2BG;
    }
    /**
     * @return the second preset for the card's font
     */
    public ColorPicker getPres2Font() {
        return pres2Font;
    }
    /**
     * @return the third preset for the card's background
     */
    public ColorPicker getPres3BG() {
        return pres3BG;
    }
    /**
     * @return the third preset for the card's font
     */
    public ColorPicker getPres3Font() {
        return pres3Font;
    }

    /**
     * getter for the colorpicker of a list's font
     * @return the listfont
     */
    public ColorPicker getListFont() {
        return listFont;
    }

    /**
     * getter for a list background colorpicker
     * @return the colorpicker object
     */
    public ColorPicker getListBG() {
        return listBG;
    }
}
