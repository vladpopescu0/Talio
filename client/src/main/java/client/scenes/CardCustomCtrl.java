package client.scenes;

import client.utils.ServerUtils;
import client.utils.SocketHandler;
import commons.Board;
import commons.ColorScheme;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class CardCustomCtrl extends ListCell<ColorScheme> {

    private final CustomizationPageCtrl parent;
    private ServerUtils server;
    private MainCtrl mainCtrl;
    private final SocketHandler socketHandler = new SocketHandler(ServerUtils.getServer());

    private Board board;

    @FXML
    private Button removeButton;

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
                if(board.getCardsColorScheme().equals(this.getItem())){
                    setStyle("-fx-border-color: red;");
                }
                presBG.setVisible(true);
                presFont.setVisible(true);
                set.setVisible(true);
                removeButton.setVisible(true);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(ColorScheme colors, boolean empty) {
        super.updateItem(colors, empty);
        if (empty || colors == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("CardCustomView.fxml"));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                    presBG.setValue(Color.valueOf(this.getItem().getColorBGlight()));
                    presFont.setValue(Color.valueOf(this.getItem().getColorFont()));
                    if(board.getCardsColorScheme().equals(this.getItem())){
                        setStyle("-fx-border-color: red;");
                    }
                    socketHandler.registerForUpdates("/topic/colorsUpdate",
                            ColorScheme.class, q -> Platform.runLater(() -> {
                                mainCtrl.getCustomizationPageCtrl().refresh();
                            }));
                    socketHandler.registerForUpdates("/topic/boardsUpdate",
                            Board.class, q -> Platform.runLater(() -> {
                                mainCtrl.getCustomizationPageCtrl().refresh();
                            }));
//                    int i = board.getColors().indexOf(this.getItem());
                    label.setText("Preset");
                    this.presFont.setOnAction(event -> saveFont());
                    this.presBG.setOnAction(event -> saveBG());
                    this.set.setOnAction(
                            event -> selectPreset());
                    this.removeButton.setOnAction(event -> deletePreset());
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
        this.getItem().setColorBGlight(mainCtrl.colorToHex(presBG.getValue()));
        this.getItem().setColorBGdark(mainCtrl.colorToHex(presBG.getValue().darker()));
        this.getItem().setColorLighter(mainCtrl.colorToHex(presBG.getValue().brighter()));
        server.updateColorScheme(this.getItem());
        board = server.getBoardByID(board.getId());
        mainCtrl.showCustomizationPage(board);
    }
    /**
     * Saves the selected color in the colopicker
     * refreshes the board to the current state in the database
     */
    public void saveFont() {
        this.getItem().setColorFont(mainCtrl.colorToHex(presFont.getValue()));
        server.updateColorScheme(this.getItem());
        board = server.getBoardByID(board.getId());
        mainCtrl.showCustomizationPage(board);
    }

    /**
     * Selects the respective preset
     */
    public void selectPreset() {
        board = server.getBoardByID(board.getId());
        board.setCardsColorScheme(this.getItem());
        server.updateBoard(board);
        mainCtrl.showCustomizationPage(board);
    }

    /**
     * Action for the delete button,
     * refreshes the view and deletes this colorScheme
     */
    public void deletePreset(){
        board = server.getBoardByID(board.getId());
        if(board.getCardsColorScheme().equals(this.getItem())){
            ColorScheme newScheme = server.addColorScheme(new ColorScheme());
            board.setCardsColorScheme(newScheme);
        }
        board.getCardsColorSchemesList().remove(this.getItem());
        board.setCardsColorScheme(new ColorScheme());
        server.updateBoard(board);
        mainCtrl.showCustomizationPage(board);
    }
}
