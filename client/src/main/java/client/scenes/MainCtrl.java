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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.DataFormat;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.List;
import java.util.Objects;

public class MainCtrl {
    private Board board;
    private Stage primaryStage;
    private Stage secondaryStage;
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
    private Scene editBoardName;
    private EditBoardNameViewCtrl editBoardNameViewCtrl;
    private Scene cardDetails;
    private CardDetailsViewCtr cardDetailsViewCtr;
    private Scene viewTags;
    private ViewTagsCtrl viewTagsCtrl;
    private Scene createTag;
    private CreateTagCtrl createTagCtrl;
    private Scene editTag;
    private EditTagCtrl editTagCtrl;
    private Scene viewAddTag;
    private ViewAddTagsCtrl viewAddTagsCtrl;
    public static final DataFormat cardDataFormat = new DataFormat("card");
    private User currentUser;

    private JoinBoardByLinkCtrl joinBoardByLinkCtrl;
    private Scene joinBoardByLink;

    /**
     * Initializes the application
     * @param primaryStage the primary stage used
     * @param secondaryStage the secondary stage used
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
     * @param createTag the createTag scene
     * @param viewTags the viewTags scene
     * @param editTag the editTag scene
     * @param joinBoardByLink the JoinBoardByLink scene
     * @param viewAddTag the viewAddTag scene
     * @param details the cardDetails scene
     */
    public void initialize(Stage primaryStage, Stage secondaryStage,
                           Pair<BoardsOverviewCtrl, Parent> overview,
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
                           Pair<CardDetailsViewCtr, Parent> details,
                           Pair<ViewTagsCtrl, Parent> viewTags,
                           Pair<CreateTagCtrl, Parent> createTag,
                           Pair<EditTagCtrl, Parent> editTag,
                           Pair<ViewAddTagsCtrl, Parent> viewAddTag) {
        this.primaryStage = primaryStage;
        this.secondaryStage = secondaryStage;

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

        showUserView();
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            closeSecondaryStage();
        });
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
     * @param card the card whose details are to be shown
     * @param board the board to which the card belongs
     */
    public void showCardDetailsView(Card card, Board board) {
        changePrimaryStage(cardDetails, card.getName());

        this.cardDetailsViewCtr.setCard(card);
        this.cardDetailsViewCtr.setBoard(board);
    }

    /**
     * Redirects to the edit Board name page
     * @param board the board whose name is to be changed
     */
    public void showEditBoardNameView(Board board) {
        showSecondaryStage(editBoardName, "Edit board name: " + board.getName());

        this.editBoardNameViewCtrl.setBoard(board);
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
     * @return the CardDetailsViewCtr
     */
    public CardDetailsViewCtr getCardDetailsViewCtr() {return cardDetailsViewCtr;}

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
     * @return the viewTagsCtrl
     */
    @SuppressWarnings("unused")
    public ViewTagsCtrl getViewTagsCtrl() {
        return viewTagsCtrl;
    }

    /**
     * Getter for viewAddTagsCtrl
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

    /** Shows the ChangeListName scene
     * @param id id of the current cardList
     */
    public void showChangeListName(Long id) {
//        primaryStage.setTitle(list.getName());
        Board board = getBoardViewCtrl().getBoard();
        showSecondaryStage(changeListName, primaryStage.getTitle());

        this.changeListNameCtrl.setId(id);
        this.changeListNameCtrl.setBoard(board);
//        this.boardViewCtrl.refresh();
    }

    /**
     * Shows the Change Server scene
     */
    public void showChangeServer() {
        showSecondaryStage(changeServer, "Change Server");
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
     * @param board the Board of which Tag overview is to be shown
     */
    public void showViewTags(Board board) {
        changePrimaryStage(viewTags, "Tags Overview");

        viewTagsCtrl.setBoard(board);
        viewTagsCtrl.refresh();
    }

    /**
     * Shows the add Tag page
     * @param board the Board to add a Tag to
     */
    public void showAddTag(Board board) {
        showSecondaryStage(createTag, "Add Tag");
        createTagCtrl.setBoard(board);
    }

    /**
     * Shows the edit Tag page
     * @param tag Tag to be edited
     */
    public void showEditTag(Tag tag) {
        showSecondaryStage(editTag, "Edit Tag");
        editTagCtrl.setTag(tag);
        editTagCtrl.updateFields();
    }

    /**
     * Shows the Add Tag to Card page
     * @param board the board to which the Card belongs
     * @param card the Card to which a Tag might be added
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
    public void showJoinBoardByLink(){
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
     * Closes the secondary stage if it's visible
     */
    public void closeSecondaryStage() {
        if (secondaryStage.isShowing()) {
            secondaryStage.close();
        }
    }

    /**
     * Sets up the primary stage for change
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
     * @param scene scene to be shown on the pop up page
     * @param title title of the pop up page
     */
    private void showSecondaryStage(Scene scene, String title) {
        secondaryStage.setTitle(title);
        secondaryStage.setScene(scene);
        secondaryStage.centerOnScreen();
        secondaryStage.toFront();
        if (!secondaryStage.isShowing()) {
            secondaryStage.show();
        }
    }

    /**
     * Checks whether the pop up page was invoked from the CardListCell entity
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
     * @param card Card corresponding to the CardCell
     * @return whether the pop up page was invoked from the CardCell entity
     */
    public boolean isSecondaryFromCardCell(Card card) {
        return secondaryStage.isShowing()
                && secondaryStage.getScene().equals(editCard) && card.getId() == cardId;
    }

    /**
     * Checks whether the pop up page was invoked from the TagCell entity
     * @param tag Tag corresponding to the TagCell
     * @return whether the pop up page was invoked from the TagCell entity
     */
    public boolean isSecondaryFromTagCell(Tag tag) {
        return secondaryStage.isShowing()
                && secondaryStage.getScene().equals(editTag)
                && tag.getId() == editTagCtrl.getTag().getId();
    }
}