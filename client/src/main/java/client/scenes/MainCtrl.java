/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import commons.Board;
import commons.Card;
import commons.User;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.DataFormat;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {
    private Board board;
    private Stage primaryStage;
    private BoardsOverviewCtrl overviewCtrl;
    private Scene overview;
    private UserBoardsOverviewCtrl userBoardsOverviewCtrl;
    private Scene userBoardOverview;
    private BoardViewCtrl boardViewCtrl;
    private Scene boardView;
    private Scene addCard;
    private CreateListCtrl createListCtrl;
    private Scene createList;
    private CreateBoardViewCtrl createBoardViewCtrl;
    private Scene createBoard;
    private Scene user;
    private UserCtrl userCtrl;

    private long id;
    private long cardId;
    private ChangeNameCtrl changeListNameCtrl;
    private Scene changeListName;
    private AddCardCtrl addCardCtrl;
    private Scene editCard;
    private EditCardCtrl editCardCtrl;
    private Scene changeServer;
    private ChangeServerCtrl changeServerCtrl;
    private Scene editBoardName;
    private EditBoardNameViewCtrl editBoardNameViewCtrl;
    private Scene cardDetails;
    private CardDetailsViewCtr cardDetailsViewCtr;
    private CustomizationPageCtrl customizationPageCtrl;

    private Scene customizationPage;

    public static final DataFormat cardDataFormat = new DataFormat("card");
    public static final DataFormat cardListDataFormat = new DataFormat("cardList");
    private User currentUser;

    private JoinBoardByLinkCtrl joinBoardByLinkCtrl;
    private Scene joinBoardByLink;

    /**
     * Initializes the application
     * @param primaryStage the primary stage used
     * @param overview the boardOverview scene
     * @param boardView the boardView scene
     * @param createList the createList scene
     * @param createBoard the createBoard scene
     * @param addCard the addCard scene
     * @param userPage the user log in page
     * @param editCard the editCard scene
     * @param changeListName the changeListName scene
     * @param changeServer the changeServer scene
     * @param userBoardsOverview the userBoardsOverview scene
     * @param editBoardName the editBoardName scene
     * @param joinBoardByLink the JoinBoardByLink scene
<<<<<<< HEAD
     * @param details the cardDetails scene
=======
     * @param customizationPage the CustomizationPage scene
>>>>>>> e3bd475188823fc0794923e8e027f00c3d84dc1d
     */
    public void initialize(Stage primaryStage, Pair<BoardsOverviewCtrl, Parent> overview,
                           Pair<BoardViewCtrl, Parent> boardView,
                           Pair<CreateListCtrl, Parent> createList,
                           Pair<CreateBoardViewCtrl, Parent> createBoard,
                           Pair<AddCardCtrl,Parent> addCard, Pair<UserCtrl, Parent> userPage,
                           Pair<EditCardCtrl, Parent> editCard,
                           Pair<ChangeNameCtrl, Parent> changeListName,
                           Pair<ChangeServerCtrl, Parent> changeServer,
                           Pair<UserBoardsOverviewCtrl, Parent> userBoardsOverview,
                           Pair<EditBoardNameViewCtrl, Parent> editBoardName,
                           Pair<JoinBoardByLinkCtrl, Parent> joinBoardByLink,
                           Pair<CardDetailsViewCtr, Parent> details,Pair<CustomizationPageCtrl, Parent> customizationPage) {
        this.primaryStage = primaryStage;

        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.boardViewCtrl = boardView.getKey();
        this.boardView = new Scene(boardView.getValue());


        this.createListCtrl = createList.getKey();
        this.createList = new Scene(createList.getValue());

        this.createBoardViewCtrl = createBoard.getKey();
        this.createBoard = new Scene(createBoard.getValue());

        this.changeListNameCtrl = changeListName.getKey();
        this.changeListName = new Scene(changeListName.getValue());

        this.addCardCtrl = addCard.getKey();
        this.addCard = new Scene(addCard.getValue());

        this.editCard = new Scene(editCard.getValue());
        this.editCardCtrl = editCard.getKey();

        this.userCtrl = userPage.getKey();
        this.user = new Scene(userPage.getValue());

        this.changeServerCtrl = changeServer.getKey();
        this.changeServer = new Scene(changeServer.getValue());

        this.userBoardsOverviewCtrl = userBoardsOverview.getKey();
        this.userBoardOverview = new Scene(userBoardsOverview.getValue());

        this.editBoardNameViewCtrl = editBoardName.getKey();
        this.editBoardName = new Scene(editBoardName.getValue());

        this.customizationPageCtrl = customizationPage.getKey();
        this.customizationPage = new Scene(customizationPage.getValue());

        this.joinBoardByLinkCtrl = joinBoardByLink.getKey();
        this.joinBoardByLink = new Scene(joinBoardByLink.getValue());

        this.cardDetailsViewCtr = details.getKey();
        this.cardDetails = new Scene(details.getValue());

        showUserView();
        primaryStage.show();
    }

    /**
     * Setter for the current user
     * @param user the user to be introduced as current user
     */
    public void setCurrentUser (User user) {
        this.currentUser = user;
    }

    /**
     * Getter for the current user
     * @return the current user
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Shows an overview of all boards
     */
    public void showOverview() {
        primaryStage.setTitle("Main Page");
        primaryStage.setScene(overview);
        this.overviewCtrl.refresh();
    }

    /**
     * Redirects to the Board View page
     *
     * @param board the board to be shown
     */
    public void showBoardView(Board board) {
        primaryStage.setTitle(board.getName());
        primaryStage.setScene(boardView);

        this.boardViewCtrl.setBoard(board);
        this.boardViewCtrl.refresh();
        this.boardViewCtrl.checkUser();

    }

    /**
     * Shows the detailed view of cards
     * @param card the card whose details are to be shown
     * @param board the board to which the card belongs
     */
    public void showCardDetailsView(Card card, Board board) {
        primaryStage.setTitle(card.getName());
        primaryStage.setScene(cardDetails);

        this.cardDetailsViewCtr.setCard(card);
        this.cardDetailsViewCtr.setBoard(board);
    }

    /**
     * Redirects to the edit Board name page
     * @param board the board whose name is to be changed
     */
    public void showEditBoardNameView(Board board) {
        primaryStage.setTitle("Edit board name: " + board.getName());
        primaryStage.setScene(editBoardName);

        this.editBoardNameViewCtrl.setBoard(board);
    }

    /**
     * Shows the add card page
     */
    public void showAddCard() {
        primaryStage.setTitle("Add Card");
        primaryStage.setScene(addCard);
    }

    /**
     * Shows the edit card page
     */
    public void showEditCard() {
        primaryStage.setTitle("Edit Card");
        primaryStage.setScene(editCard);
        editCardCtrl.updateFields(getCardId());
        //must change later for safety measures

    }


    /**
     * Shows the createList scene
     *
     * @param board the board to which the list is to be added
     */
    public void showCreateList(Board board) {
        primaryStage.setTitle("Main Page");
        primaryStage.setScene(createList);
        this.createListCtrl.setBoard(board);
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(Main.class.getResource("ClientView.fxml"));
//        mainLayout = loader.load();
//
//        ClientViewController cvc = loader.getController();
//        cvc.setClient(client); // Passing the client-object to the ClientViewController
//        this.createListCtrl.setBoard(board);
//
//        Scene scene = new Scene(mainLayout, 900, 600);
//        primaryStage.setScene(scene);
//        primaryStage.setResizable(true);
//        primaryStage.show();
    }

    /**
     * Getter for boardViewCtrl
     *
     * @return the boardViewCtrl
     */
    @SuppressWarnings("unused")
    public BoardViewCtrl getBoardViewCtrl() {
        return boardViewCtrl;
    }
    /**
     * Getter for boardViewCtrl
     *
     * @return the boardViewCtrl
     */
    @SuppressWarnings("unused")
    public BoardsOverviewCtrl getOverviewCtrl() {
        return overviewCtrl;
    }

    /**
     * Getter for userBoardOverviewCtrl
     *
     * @return the userBoardOverviewCtrl
     */
    public UserBoardsOverviewCtrl getUserBoardsOverviewCtrl() {
        return userBoardsOverviewCtrl;
    }

    /**
     * Shows the createBoard scene
     */
    public void createBoardView() {
        primaryStage.setTitle("New Board");
        primaryStage.setScene(createBoard);
    }

    /**
     * Shows the sign-in page
     */
    public void showUserView() {
        primaryStage.setTitle("Sign in");
        primaryStage.setScene(user);
    }

    /**
     * Shows the customization page
     * @param board the board to be customized
     */
    public void showCustomizationPage(Board board) {
        primaryStage.setTitle("Customize Your Board");
        primaryStage.setScene(customizationPage);
        this.customizationPageCtrl.setBoard(board);
    }

    /** Shows the ChangeListName scene
     * @param id id of the current cardList
     */
    public void showChangeListName(Long id) {
        Board board = getBoardViewCtrl().getBoard();
        primaryStage.setScene(changeListName);
        this.changeListNameCtrl.setId(id);
        this.changeListNameCtrl.setBoard(board);
    }

    /**
     * Shows the Change Server scene
     */
    public void showChangeServer() {
        primaryStage.setScene(changeServer);
    }

    /**
     * Shows an overview of all boards for a logged-in user
     */
    public void showUserBoardOverview() {
        primaryStage.setTitle("Your boards");
        primaryStage.setScene(userBoardOverview);
        this.userBoardsOverviewCtrl.refresh();
    }

    /**
     * Sets the current screen to the "JoinBoardByLink scene from resources"
     */
    public void showJoinBoardByLink(){
        primaryStage.setTitle("Join A Board By Code");
        primaryStage.setScene(joinBoardByLink);
    }

    /**
     * @return the current cardlist id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id sets the id of the current cardlist
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the current cardList id
     */
    public long getCardId() {
        return cardId;
    }

    /**
     * @param cardId sets the id of the current cardlist
     */
    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    /**
     * @param color the color the needs to be transformed to hex format
     * @return a hex format of the color
     */
    public String colorToHex(Color color){
        return String.format( "#%02X%02X%02X",
                (int)( color.getRed() * 255 ),
                (int)( color.getGreen() * 255 ),
                (int)( color.getBlue() * 255 ) );
    }

    /**
     * @return the controller of the customization page
     */
    public CustomizationPageCtrl getCustomizationPageCtrl() {
        return customizationPageCtrl;
    }

    /** Sets the style for a button
     * @param button the button for which the style is set
     * @param bgColor the bg color of the button
     * @param fontColor the cont color of the button
     */
    public void setButtonStyle(Button button, String bgColor, String fontColor) {
        String style = "-fx-background-color: " + bgColor + "; "
                + "-fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0;"
                + "-fx-background-radius: 5px;" +
                "-fx-text-fill:" + fontColor + ";"+
                "-fx-border-color: " + fontColor+";"+
                "-fx-border-radius: 5%;";
        button.setStyle(style);
    }
}