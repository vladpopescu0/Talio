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

import commons.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MainCtrl {

    private Stage primaryStage;
    private Stage secondaryStage;
    private Stage helpStage;
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
    private List<Long> draggableData;
    private ChangeNameCtrl changeListNameCtrl;
    private Scene changeListName;
    private AddCardCtrl addCardCtrl;
    private Scene editCard;
    private EditCardCtrl editCardCtrl;
    private Scene changeServer;
    private ChangeServerCtrl changeServerCtrl;
    private Scene adminCheck;
    private AdminCheckCtrl adminCheckCtrl;
    private Scene editBoardName;
    private EditBoardNameViewCtrl editBoardNameViewCtrl;
    private Scene cardDetails;
    private CardDetailsViewCtr cardDetailsViewCtr;
    private CustomizationPageCtrl customizationPageCtrl;

    private Scene changeBoardPass;

    private EditBoardPasswordViewCtrl changeBoardPassCtrl;

    private Scene checkBoardPass;

    private CheckBoardPasswordViewCtrl checkBoardPassCtrl;

    private Scene customizationPage;
    private Scene viewTags;
    private ViewTagsCtrl viewTagsCtrl;
    private Scene createTag;
    private CreateTagCtrl createTagCtrl;
    private Scene editTag;
    private EditTagCtrl editTagCtrl;
    private Scene viewAddTag;
    private ViewAddTagsCtrl viewAddTagsCtrl;
    private Scene helpPage;
    private HelpCtrl helpPageCtrl;
    public static final DataFormat cardDataFormat = new DataFormat("card");
    private User currentUser;
    private JoinBoardByLinkCtrl joinBoardByLinkCtrl;
    private Scene joinBoardByLink;
    private boolean isAdmin = false;

    private String adminPass = "";

    private HashMap<Long, String> savedPasswords = new HashMap<>();

    /**
     * Initializes the application
     *
     * @param primaryStage       the primary stage used
     * @param secondaryStage     the secondary stage used
     * @param helpStage          the stage for help information used
     * @param overview           the boardOverview scene
     * @param boardView          the boardView scene
     * @param createList         the createList scene
     * @param createBoard        the createBoard scene
     * @param addCard            the addCard scene
     * @param userPage           the user log in page
     * @param editCard           the editCard scene
     * @param changeListName     the changeListName scene
     * @param changeServer       the changeServer scene
     * @param userBoardsOverview the userBoardsOverview scene
     * @param editBoardName the editBoardName scene
     * @param createTag the createTag scene
     * @param viewTags the viewTags scene
     * @param editTag the editTag scene
     * @param joinBoardByLink the JoinBoardByLink scene
     * @param viewAddTag the viewAddTag scene
     * @param details the cardDetails scene
     * @param customizationPage the CustomizationPage scene
     * @param adminCheck the adminCheck scene
     * @param editBoardPass the editBoardPassword scene
     * @param helpPage the HelpPage scene
     * @param checkBoardPass the checkBoardPassword scene
     */
    public void initialize(Stage primaryStage, Stage secondaryStage, Stage helpStage,
                           Pair<BoardsOverviewCtrl, Parent> overview,
                           Pair<BoardViewCtrl, Parent> boardView,
                           Pair<CreateListCtrl, Parent> createList,
                           Pair<CreateBoardViewCtrl, Parent> createBoard,
                           Pair<AddCardCtrl, Parent> addCard,
                           Pair<UserCtrl, Parent> userPage,
                           Pair<EditCardCtrl, Parent> editCard,
                           Pair<ChangeNameCtrl, Parent> changeListName,
                           Pair<ChangeServerCtrl, Parent> changeServer,
                           Pair<UserBoardsOverviewCtrl, Parent> userBoardsOverview,
                           Pair<EditBoardNameViewCtrl, Parent> editBoardName,
                           Pair<JoinBoardByLinkCtrl, Parent> joinBoardByLink,
                           Pair<CardDetailsViewCtr, Parent> details,
                           Pair<CustomizationPageCtrl, Parent> customizationPage,
                           Pair<AdminCheckCtrl, Parent> adminCheck,
                           Pair<ViewTagsCtrl, Parent> viewTags,
                           Pair<CreateTagCtrl, Parent> createTag,
                           Pair<EditTagCtrl, Parent> editTag,
                           Pair<ViewAddTagsCtrl, Parent> viewAddTag,
                            Pair<EditBoardPasswordViewCtrl, Parent> editBoardPass,
                           Pair<CheckBoardPasswordViewCtrl, Parent> checkBoardPass,
                           Pair<HelpCtrl, Parent> helpPage) {

        this.primaryStage = primaryStage;

        this.secondaryStage = secondaryStage;
        this.helpStage = helpStage;

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

        this.adminCheckCtrl = adminCheck.getKey();
        this.adminCheck = new Scene(adminCheck.getValue());

        this.userBoardsOverviewCtrl = userBoardsOverview.getKey();
        this.userBoardOverview = new Scene(userBoardsOverview.getValue());

        this.editBoardNameViewCtrl = editBoardName.getKey();
        this.editBoardName = new Scene(editBoardName.getValue());

        this.customizationPageCtrl = customizationPage.getKey();
        this.customizationPage = new Scene(customizationPage.getValue());

        this.viewTagsCtrl = viewTags.getKey();
        this.viewTags = new Scene(viewTags.getValue());

        this.createTagCtrl = createTag.getKey();
        this.createTag = new Scene(createTag.getValue());

        this.editTagCtrl = editTag.getKey();
        this.editTag = new Scene(editTag.getValue());

        this.joinBoardByLinkCtrl = joinBoardByLink.getKey();
        this.joinBoardByLink = new Scene(joinBoardByLink.getValue());

        this.cardDetailsViewCtr = details.getKey();
        this.cardDetails = new Scene(details.getValue());

        this.viewAddTagsCtrl = viewAddTag.getKey();
        this.viewAddTag = new Scene(viewAddTag.getValue());

        this.helpPageCtrl = helpPage.getKey();
        this.helpPage = new Scene(helpPage.getValue());

        this.changeBoardPassCtrl = editBoardPass.getKey();
        this.changeBoardPass = new Scene(editBoardPass.getValue());

        this.checkBoardPassCtrl = checkBoardPass.getKey();
        this.checkBoardPass = new Scene(checkBoardPass.getValue());
        primaryStage.show();

        helpStage.setScene(this.helpPage);

        showSelectServer();

        primaryStage.setOnCloseRequest(event -> {
            closeSecondaryStage();
            closeHelpStage();
        });

        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            keyEventListener(event, true);
        });
        secondaryStage.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            keyEventListener(event, false);
        });
    }

    /**
     * Setter for the current user
     *
     * @param user the user to be introduced as current user
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /**
     * Getter for the current user
     *
     * @return the current user
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Shows an overview of all boards
     */
    public void showOverview() {
        changePrimaryStage(overview, "Main Page");
        this.overviewCtrl.refresh();
    }

    /**
     * Redirects to the Board View page
     *
     * @param board the board to be shown
     */
    public void showBoardView(Board board) {
        changePrimaryStage(boardView, board.getName());

        this.boardViewCtrl.setBoard(board);
        this.boardViewCtrl.refresh();
        this.boardViewCtrl.checkUser();

    }

    /**
     * Shows the detailed view of cards
     *
     * @param card  the card whose details are to be shown
     * @param board the board to which the card belongs
     */
    public void showCardDetailsView(Card card, Board board) {
        changePrimaryStage(cardDetails, card.getName());

        this.cardDetailsViewCtr.setCard(card);
        this.cardDetailsViewCtr.setBoard(board);
        this.cardDetailsViewCtr.refresh();
    }

    /**
     * Redirects to the edit Board name page
     *
     * @param board the board whose name is to be changed
     */
    public void showEditBoardNameView(Board board) {
        showSecondaryStage(editBoardName, "Edit board name: " + board.getName());

        this.editBoardNameViewCtrl.setBoard(board);
    }

    /**
     * Shows the change Password scene for the given board
     * @param board the board to change
     */
    public void showChangeBoardPasswordView(Board board) {
        showSecondaryStage(changeBoardPass, "Edit board password");

        this.changeBoardPassCtrl.setBoard(board);
    }

    /**
     * Shows the Check password scene for the given board
     * @param board The board to check
     */
    public void showCheckBoardPasswordView(Board board) {
        showSecondaryStage(checkBoardPass, "Check board password");

        this.checkBoardPassCtrl.setBoard(board);
    }

    /**
     * Shows the add card page
     */
    public void showAddCard() {
        showSecondaryStage(addCard, "Add Card");
    }

    /**
     * Shows the edit card page
     */
    public void showEditCard() {
        showSecondaryStage(editCard, "Edit Card");

        editCardCtrl.updateFields(getCardId());
        //must change later for safety measures

    }


    /**
     * Shows the createList scene
     *
     * @param board the board to which the list is to be added
     */
    public void showCreateList(Board board) {
        showSecondaryStage(createList, "Create List");

        this.createListCtrl.setBoard(board);
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
     * Getter for the CardDetailsViewCtr
     *
     * @return the CardDetailsViewCtr
     */
    public CardDetailsViewCtr getCardDetailsViewCtr() {
        return cardDetailsViewCtr;
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
     * Getter for viewTagsCtrl
     *
     * @return the viewTagsCtrl
     */
    @SuppressWarnings("unused")
    public ViewTagsCtrl getViewTagsCtrl() {
        return viewTagsCtrl;
    }

    /**
     * Getter for viewAddTagsCtrl
     *
     * @return the viewAddTagsCtrl
     */
    public ViewAddTagsCtrl getViewAddTagsCtrl() {
        return viewAddTagsCtrl;
    }

    /**
     * Shows the createBoard scene
     */
    public void createBoardView() {
        showSecondaryStage(createBoard, "New Board");
    }

    /**
     * Shows the sign-in page
     */
    public void showUserView() {
        changePrimaryStage(user, "Sign in");
    }

    /**
     * Shows the customization page
     *
     * @param board the board to be customized
     */
    public void showCustomizationPage(Board board) {
        //primaryStage.setTitle("Customize Your Board");
        showSecondaryStage(customizationPage,"Customize Your Board");
        this.customizationPageCtrl.setBoard(board);
        this.customizationPageCtrl.refresh();
        //primaryStage.setScene(customizationPage);
    }

    /**
     * Shows the admin login page
     */
    public void showAdminCheck() {
        primaryStage.setTitle("Admin Password");
        primaryStage.setScene(adminCheck);
    }

    /**
     * Shows the ChangeListName scene
     *
     * @param id id of the current cardList
     */
    public void showChangeListName(Long id) {
        Board board = getBoardViewCtrl().getBoard();
        showSecondaryStage(changeListName, primaryStage.getTitle());

        this.changeListNameCtrl.setId(id);
        this.changeListNameCtrl.setBoard(board);
    }

    /**
     * Shows the Change Server scene
     */
    public void showChangeServer() {
        this.changeServerCtrl.initialize();
        showSecondaryStage(changeServer, "Change Server");
        this.changeServerCtrl.showAsPopUp();
    }

    /**
     * Shows the selectServer scene
     */
    public void showSelectServer() {
        this.changeServerCtrl.initialize();
        primaryStage.setTitle("Select a server");
        this.primaryStage.setScene(changeServer);
        this.changeServerCtrl.startScene();
    }

    /**
     * Shows an overview of all boards for a logged-in user
     */
    public void showUserBoardOverview() {
        changePrimaryStage(userBoardOverview, "Your boards");

        this.userBoardsOverviewCtrl.refresh();
    }

    /**
     * Opens a new window with an overview of all tags for the current board
     *
     * @param board the Board of which Tag overview is to be shown
     */
    public void showViewTags(Board board) {
        changePrimaryStage(viewTags, "Tags Overview");

        viewTagsCtrl.setBoard(board);
        viewTagsCtrl.refresh();
    }

    /**
     * Shows the add Tag page
     *
     * @param board the Board to add a Tag to
     */
    public void showAddTag(Board board) {
        showSecondaryStage(createTag, "Add Tag");
        createTagCtrl.setBoard(board);
    }

    /**
     * Shows the edit Tag page
     *
     * @param tag Tag to be edited
     */
    public void showEditTag(Tag tag) {
        showSecondaryStage(editTag, "Edit Tag");
        editTagCtrl.setTag(tag);
        editTagCtrl.updateFields();
    }

    /**
     * Shows the Add Tag to Card page
     *
     * @param board    the board to which the Card belongs
     * @param card     the Card to which a Tag might be added
     * @param shortcut whether the page was opened using a keyboard shortcut
     */
    public void showViewAddTag(Board board, Card card, boolean shortcut) {
        showSecondaryStage(viewAddTag, "Add Tag to " + card.getName());
        viewAddTagsCtrl.setBoard(board);
        viewAddTagsCtrl.setCard(card);
        viewAddTagsCtrl.setShortcut(shortcut);
        viewAddTagsCtrl.refresh();
    }

    /**
     * Sets the current screen to the "JoinBoardByLink scene from resources"
     */
    public void showJoinBoardByLink() {
        showSecondaryStage(joinBoardByLink, "Join A Board By Code");
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
    public String colorToHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    /**
     * @return the controller of the customization page
     */
    public CustomizationPageCtrl getCustomizationPageCtrl() {
        return customizationPageCtrl;
    }

    /**
     * Sets the style for a button
     *
     * @param button    the button for which the style is set
     * @param bgColor   the bg color of the button
     * @param fontColor the cont color of the button
     */
    public void setButtonStyle(Button button, String bgColor, String fontColor) {
        String style = "-fx-background-color: " + bgColor + "; "
                + "-fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0;"
                + "-fx-background-radius: 5px;" +
                "-fx-text-fill:" + fontColor + ";" +
                "-fx-border-color: " + fontColor + ";" +
                "-fx-border-radius: 5%;";
        button.setStyle(style);
    }

    /**
     * @param isAdmin boolean if user is admin
     */
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * @return boolean if user is admin
     */
    public boolean isAdmin() {
        return this.isAdmin;
    }

    /**
     * @return the admin password
     */
    public String getAdminPass() {
        return adminPass;
    }

    /**
     * @param pass the admin password
     */
    public void setAdminPass(String pass) {
        this.adminPass = pass;
    }

    /**
     * Closes the secondary stage if it's visible
     */
    public void closeSecondaryStage() {
        if (secondaryStage.isShowing()) {
            secondaryStage.close();
        }
    }

    /**
     * returns the Secondary Stage
     * @return the secondary stage
     */
    public Stage getSecondaryStage(){
        return secondaryStage;
    }

    /**
     * Closes the help stage if it's visible
     */
    public void closeHelpStage() {
        if (helpStage.isShowing()) {
            helpStage.close();
        }
    }

    /**
     * Sets up the primary stage for change
     *
     * @param scene scene to be shown on the page
     * @param title title of the page
     */
    private void changePrimaryStage(Scene scene, String title) {
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.toFront();
    }

    /**
     * Shows the secondary stage if it's not visible
     *
     * @param scene scene to be shown on the pop up page
     * @param title title of the pop up page
     */
    private void showSecondaryStage(Scene scene, String title) {
        Scene oldScene = secondaryStage.getScene();
        secondaryStage.setTitle(title);
        secondaryStage.setScene(scene);
        secondaryStage.toFront();
        if (!secondaryStage.isShowing()) {
            secondaryStage.centerOnScreen();
            secondaryStage.show();
        } else if (!oldScene.equals(scene)) {
            secondaryStage.centerOnScreen();
        }
    }

    /**
     * Shows the help stage if it's not visible
     */
    private void showHelpStage() {
        helpStage.toFront();
        if (!helpStage.isShowing()) {
            helpStage.centerOnScreen();
            helpStage.show();
        }
    }

    /**
     * Checks whether the pop up page was invoked from the CardListCell entity
     *
     * @param cardList CardList corresponding to the CardListCell
     * @return whether the pop up page was invoked from the CardListCell entity
     */
    public boolean isSecondaryFromCardListCell(CardList cardList) {
        return secondaryStage.isShowing()
                && ((secondaryStage.getScene().equals(addCard) && cardList.getId() == this.getId())
                || (secondaryStage.getScene().equals(changeListName)
                && Objects.equals(cardList.getId(), changeListNameCtrl.getId())));
    }

    /**
     * Checks whether the pop up page was invoked from the CardCell entity
     *
     * @param card Card corresponding to the CardCell
     * @return whether the pop up page was invoked from the CardCell entity
     */
    public boolean isSecondaryFromCardCell(Card card) {
        return secondaryStage.isShowing()
                && secondaryStage.getScene().equals(editCard) && card.getId() == cardId;
    }

    /**
     * Checks whether the pop up page was invoked from the TagCell entity
     *
     * @param tag Tag corresponding to the TagCell
     * @return whether the pop up page was invoked from the TagCell entity
     */
    public boolean isSecondaryFromTagCell(Tag tag) {
        return secondaryStage.isShowing()
                && secondaryStage.getScene().equals(editTag)
                && tag.getId() == editTagCtrl.getTag().getId();
    }


    /**
     * @return the primary stage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Returns the currently focused node
     *
     * @return the currently focused node in the primary stage
     */
    public Node getFocusedNode() {
        return primaryStage.getScene().getFocusOwner();
    }

    /**
     * Getter for the boardOverviewCtrl
     * @return the boardOverviewCtrl
     */
    public BoardsOverviewCtrl getBoardsOverviewCtrl() {
        return this.overviewCtrl;
    }
        /**
         * Event listener for shortcuts
         * @param event the key event
         * @param primary whether the key listener concerns primary stage
         */
    private void keyEventListener(KeyEvent event, boolean primary) {
        Node focused = primary? primaryStage.getScene().getFocusOwner()
                : secondaryStage.getScene().getFocusOwner();
        if (!(focused instanceof TextField || focused instanceof TextArea)) {
            if (event.getCode() == KeyCode.SLASH) {
                showHelpStage();
            }
        }
    }

    /**
     * Gets the map of saved passwords
     * @return Map of board ID to saved password
     */
    public HashMap<Long, String> getSavedPasswords() {
        return savedPasswords;
    }

    /**
     * Sets the map of saved passwords
     * @param savedPasswords Map of Board ID to Password (String)
     */
    public void setSavedPasswords(HashMap<Long, String> savedPasswords) {
        this.savedPasswords = savedPasswords;
    }

    /**
     * Updates the map of saved passwords by replacing a password if one exists,
     * otherwise adding it
     * @param id Board ID
     * @param pass Password
     */
    public void updatePassword(long id, String pass) {
        if (this.savedPasswords.containsKey(id)) {
            this.savedPasswords.replace(id, pass);
        } else {
            this.savedPasswords.put(id, pass);
        }
    }

    /**
     * When the user changes, all saved passwords should be forgotten
     */
    public void forgetPasswords() {
        savedPasswords = new HashMap<>();
    }
}