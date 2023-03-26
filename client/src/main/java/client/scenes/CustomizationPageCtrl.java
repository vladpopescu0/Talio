package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    private Button boardReset;

    @FXML
    private ColorPicker listBG;

    @FXML
    private Button listReset;

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

    @FXML
    private Button set1;

    @FXML
    private Button set2;

    @FXML
    private Button set3;

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
     * Customizes the board, list and cards
     */
    public void customizeBoard() {
        if (!set1.isVisible()) {
            board.setCardsBGColor(mainCtrl.colorToHex(pres1BG.getValue()));
            board.setCardsFontColor(mainCtrl.colorToHex(pres1Font.getValue()));
        } else if (!set2.isVisible()) {
            board.setCardsBGColor(mainCtrl.colorToHex(pres2BG.getValue()));
            board.setCardsFontColor(mainCtrl.colorToHex(pres2Font.getValue()));
        } else if (!set3.isVisible()) {
            board.setCardsBGColor(mainCtrl.colorToHex(pres3BG.getValue()));
            board.setCardsFontColor(mainCtrl.colorToHex(pres3Font.getValue()));
        }
        this.board.setColorLighter(mainCtrl.colorToHex(boardBG.getValue().brighter()));
        this.board.setColorBGlight(mainCtrl.colorToHex(boardBG.getValue()));
        this.board.setColorBGdark(mainCtrl.colorToHex(boardBG.getValue().darker()));
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
        set2.setVisible(true);
        board.getButtons().set(1, true);
        set3.setVisible(true);
        board.getButtons().set(2, true);
        set1.setVisible(false);
        board.getButtons().set(0, false);
        this.board = server.addBoard(board);
    }
    /**
     * Selects the second preset and makes button disappear
     */
    public void selectPreset2() {
        set1.setVisible(true);
        board.getButtons().set(0, true);
        set3.setVisible(true);
        board.getButtons().set(2, true);
        set2.setVisible(false);
        board.getButtons().set(1, false);
        this.board = server.addBoard(board);
    }
    /**
     * Selects the third preset and makes button disappear
     */
    public void selectPreset3() {
        set2.setVisible(true);
        board.getButtons().set(1,true);
        set1.setVisible(true);
        board.getButtons().set(0,true);
        set3.setVisible(false);
        board.getButtons().set(2,false);
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
     * @return the button for setting the first preset
     */
    public Button getSet1() {
        return set1;
    }

    /**
     * @return the button for setting the second preset
     */
    public Button getSet2() {
        return set2;
    }

    /**
     * @return the button for setting the third preset
     */
    public Button getSet3() {
        return set3;
    }
}
