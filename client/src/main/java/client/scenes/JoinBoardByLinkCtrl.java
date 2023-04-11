package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

import java.util.List;
import java.util.stream.Collectors;


public class JoinBoardByLinkCtrl {

    @FXML
    private TextField code;

    @FXML
    private Button cancel;

    @FXML
    private Button join;

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    /**
     * Constructor for the AddCardCtrl class
     * @param server the server to be used
     * @param mainCtrl the mainCtrl of the application
     */
    @Inject
    public JoinBoardByLinkCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    /**
     * Adds support for keyboard shortcuts
     */
    @FXML
    private void handleShortcuts(KeyEvent event) {
        switch(event.getCode()) {
            case ENTER: ok();
                break;
            case ESCAPE: cancel();
                break;
        }
    }

    /**
     * button for cancelling the add card scene, returning to the board
     * Cancel button that returns back to the boardView
     */
    public void cancel() {
        clearFields();
        mainCtrl.closeSecondaryStage();
    }

    /**
     * The function connected to the add card button, posts the card in the
     * database by adding it to a list with a given id
     * The method called when pressing the button creating a card
     */
    public void ok() {
        if(isNullOrEmpty(code.getText()) || code.getText().length()<4){
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("Enter a numeric code with at least 4 digits!");
            alert.showAndWait();
            return;
        }
        try{
            long boardId = Long.parseLong(code.getText());
            List<Board> boards = server.getBoards().stream()
                    .filter(board -> board.getId()==boardId)
                    .collect(Collectors.toList());
            if(boards.size()==1){
                Board foundBoard = boards.get(0);
                if(mainCtrl.getCurrentUser().getBoardList().contains(foundBoard)){
                    mainCtrl.closeSecondaryStage();
                    mainCtrl.showBoardView(foundBoard);
                    return;
                }
                foundBoard.addUser(mainCtrl.getCurrentUser());
                foundBoard = server.updateBoard(foundBoard);
                mainCtrl.getCurrentUser().setBoardList(server.
                        getBoardsByUserId(mainCtrl.getCurrentUser().getId()));
                mainCtrl.closeSecondaryStage();
                mainCtrl.showBoardView(foundBoard);
            }else{
                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText("Board Not Found! Enter a different code.");
                alert.showAndWait();
            }
        }catch (NumberFormatException nfe){
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("Enter a numeric code with at least 4 digits!");
            alert.showAndWait();
        }
    }

    /**
     * Checks if a string is null or empty
     * @param s the string to be checked
     * @return true is s == null or s = ''
     */
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * clears the title text field
     */
    private void clearFields() {
        if (code != null) {
            code.clear();
        }
    }

    /**
     * Returns a String describing page-specific shortcuts
     * @return String description of page-specific shortcuts
     */
    public String additionalHelp() {
        return "Join by Code specific shortcuts:\n"
                + "Enter - Submit the code\n"
                + "Escape - Close the page";
    }
}
