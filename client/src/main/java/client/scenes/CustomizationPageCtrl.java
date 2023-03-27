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
    private ColorPicker BoardFont;

    @FXML
    private ColorPicker ListFont;

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

    @Inject
    public CustomizationPageCtrl(MainCtrl mainCtrl,
                                 Board board, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.board = board;
        this.server = server;
    }

    public void toBoardView(){
        mainCtrl.showBoardView(board);
    }

    public void setBoard(Board board) {
        this.board = board;
    }


    public void customizeBoard(){
        this.board.getColorScheme().setColorLighter(mainCtrl.colorToHex(boardBG.getValue().brighter()));
        this.board.getColorScheme().setColorBGlight(mainCtrl.colorToHex(boardBG.getValue()));
        this.board.getColorScheme().setColorBGdark(mainCtrl.colorToHex(boardBG.getValue().darker()));
        this.board.getColorScheme().setColorFont(mainCtrl.colorToHex(BoardFont.getValue()));

        this.board.getListsColorScheme().setColorLighter(mainCtrl.colorToHex(listBG.getValue().brighter()));
        this.board.getListsColorScheme().setColorBGlight(mainCtrl.colorToHex(listBG.getValue()));
        this.board.getListsColorScheme().setColorBGdark(mainCtrl.colorToHex(listBG.getValue().darker()));
        this.board.getListsColorScheme().setColorFont(mainCtrl.colorToHex(ListFont.getValue()));

        this.board = server.addBoard(board);
        mainCtrl.showBoardView(board);
//        mainCtrl.getBoardViewCtrl().customizeBoard(board);
    }
}
