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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;
    private BoardsOverviewCtrl overviewCtrl;
    private Scene overview;

    private BoardViewCtrl boardViewCtrl;
    private Scene boardView;
    private Scene addCard;

    private AddCardCtrl addCtrl;
    private Scene add;
    public long id= -1;
    public Board board;


    private CreateListCtrl createListCtrl;
    private Scene createList;
    private CreateBoardViewCtrl createBoardViewCtrl;
    private Scene createBoard;
    private Scene user;
    private UserCtrl userCtrl;


    /**
     * Initializes the application
     * @param primaryStage the primary stage used
     * @param overview the boardOverview scene
     * @param boardView the boardView scene
     * @param createList the createList scene
     * @param createBoard the createBoard scene
     * @param addCard the addCard scene
     * @param userPage the user log in page
     */
    public void initialize(Stage primaryStage, Pair<BoardsOverviewCtrl, Parent> overview,
            Pair<BoardViewCtrl, Parent> boardView, Pair<CreateListCtrl, Parent> createList,
                           Pair<CreateBoardViewCtrl, Parent> createBoard,
                           Pair<AddCardCtrl,Parent> addCard, Pair<UserCtrl, Parent> userPage) {

        this.primaryStage = primaryStage;

        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.boardViewCtrl = boardView.getKey();
        this.boardView = new Scene(boardView.getValue());


        this.createListCtrl = createList.getKey();
        this.createList = new Scene(createList.getValue());

        this.createBoardViewCtrl = createBoard.getKey();
        this.createBoard = new Scene(createBoard.getValue());

        this.addCtrl=addCard.getKey();
        this.add=new Scene(addCard.getValue());

        this.userCtrl = userPage.getKey();
        this.user = new Scene(userPage.getValue());

        showUserView();
        primaryStage.show();
    }

    /**
     * SHows an overview of all boards
     */
    public void showOverview() {
        primaryStage.setTitle("Main Page");
        primaryStage.setScene(overview);
        this.overviewCtrl.refresh();
    }

    /**
     * Redirects to the Board View page
     * @param board the board to be shown
     */
    public void showBoardView(Board board) {
        primaryStage.setTitle(board.getName());
        primaryStage.setScene(boardView);
        //this.boardViewCtrl.setBoard(board);

        this.board=board;
    }

    /**
     * Shows the add card page
     */
    public void showAddCard(){
        primaryStage.setTitle("Add Card");
        primaryStage.setScene(add);
    }

    /**
     * Shows the createList scene
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
     * @return the boardViewCtrl
     */
    @SuppressWarnings("unused")
    public BoardViewCtrl getBoardViewCtrl() {
        return boardViewCtrl;
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

}