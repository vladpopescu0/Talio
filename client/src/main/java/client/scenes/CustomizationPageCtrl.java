package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.ColorScheme;
import jakarta.ws.rs.WebApplicationException;
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

    private ObservableList<ColorScheme> colorPairObservableList;
    @FXML
    private ListView<ColorScheme> colorPairList;

    @FXML
    private ColorPicker boardFont;

    @FXML
    private ColorPicker listFont;

    @FXML
    private ColorPicker boardBG;

    @FXML
    private ColorPicker listBG;

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
        List<ColorScheme> colors = (board == null || board.getCardsColorSchemesList() == null ?
                new ArrayList<>() : board.getCardsColorSchemesList());
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
     * method that assign specific colors
     * to a board and the components of it
     */
    public void customizeBoard(){
        board = server.getBoardByID(board.getId());
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
    * Updates the tasks and description
     */
    public void refresh() {
        List<ColorScheme> colorsCards = (board == null || board.getCardsColorSchemesList() == null ?
                new ArrayList<>() : board.getCardsColorSchemesList());
        colorPairObservableList = FXCollections.observableList(colorsCards);
        colorPairList.setItems(colorPairObservableList);
        colorPairList.setCellFactory(t -> new CardCustomCtrl(mainCtrl, server, this));
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
     * Resets the lists' colors
     */
    public void resetLists() {
        board.getListsColorScheme()
                .setColorFont(mainCtrl.colorToHex(Color.WHITE));
        board.getListsColorScheme()
                .setColorBGdark(mainCtrl.colorToHex(Color.BLACK));
        board.getListsColorScheme()
                .setColorBGlight(mainCtrl.colorToHex(Color.BLACK));
        board.getListsColorScheme()
                .setColorLighter(mainCtrl.colorToHex(Color.GRAY));
        listFont.setValue(Color.WHITE);
        listBG.setValue(Color.BLACK);
    }

    /**
     * @return the current board state
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Adds a new preset to the board
     */
    public void addPreset(){
        try{
            board = server.getBoardByID(board.getId());
        }catch (WebApplicationException e){
            e.printStackTrace();
            return;
        }
        //!!!MIGHT NOT WORK IF THERE IS NO SUCH BOARD
        ColorScheme newDefaultColorScheme = server.addColorScheme(new ColorScheme());
        board.getCardsColorSchemesList().add(newDefaultColorScheme);
        server.updateBoard(board);
        refresh();
        this.mainCtrl.showCustomizationPage(board);
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
