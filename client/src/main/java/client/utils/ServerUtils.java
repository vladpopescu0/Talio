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
package client.utils;

import commons.*;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

public class ServerUtils {

    private static String server = "http://localhost:8080/";

    /**
     * Sets static server variable
     * @param server New server host
     */
    public static void setServer(String server){
        ServerUtils.server = server;
    }

    /**
     * Get current server host
     * @return Current server host
     */
    public static String getServer() { return ServerUtils.server; };

    /**
     * Method that gets all boards from the database
     * through the /boards api
     *
     * @return A list of all boards added to the database
     */
    public List<Board> getBoards() {
        List<Board> boards = ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {});
        for(Board b: boards) {
            setCardsParent(b);
        }
        return boards;
    }

    /**
     * Returns a board with the specific id, if it exists
     *
     * @param id id of the searched board
     * @return the board
     */
    public Board getBoardByID(Long id) {
        Board board = ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(Board.class);
        setCardsParent(board);
        return board;
    }

    /**
     * Adds a board to the database
     *
     * @param board the board to be added
     * @return the new board
     */
    public Board addBoard(Board board) {
        Board b = ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards/add") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(board, APPLICATION_JSON), Board.class);
        setCardsParent(b);
        return b;
    }

    /**
     * @param board the board whose name needs to be modified
     * @return the modified board
     */
    public Board modifyBoard(Board board) {
        Board b = ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards/modify") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(board, APPLICATION_JSON), Board.class);
        setCardsParent(b);
        return b;
    }

    /**
     * Adds a user to the database
     *
     * @param user the user to be added
     * @return the new user
     */
    public User addUser(User user) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/users/add") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(user, APPLICATION_JSON), User.class);

    }

    /**
     * Get a CardList from the database using its id
     *
     * @param id the id to search in the database, gets bad request if it is not proper
     * @return the CardList that was found
     */
    public CardList getCardListById(long id) {
        CardList cl = ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/lists/"+id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {});
        setCardsParent(cl);
        return cl;
    }

    /**
     * Adds a user to the database
     *
     * @param card the card to be added
     * @return the new card
     */
    public Card addCard(Card card) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/cards/add") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    /**
     * Checks whether a username is already used
     *
     * @param username the username in search
     * @return true if the username exists in the database;
     * false otherwise
     */
    public List<User> getUserByUsername(String username) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/users/username/" + username) //
                .request(APPLICATION_JSON)//
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

    /**
     * Gets user by ID
     *
     * @param id the id in search
     * @return the user in search
     */
    public User getUserById(long id) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/users/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });

    }

    /**
     * Get a list of cards by having a list id, solving the recursion problem
     *
     * @param id   the id of the card list
     * @param card the card that needs to be added
     * @return the cards that are connected to that card list
     */
    public Card addCardToList(Card card, long id) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/lists/addCard/"+id)//
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(card,APPLICATION_JSON),Card.class);
    }

    /**
     * Adds a task to a card
     * @param task the task to be added
     * @param id the id of the card
     * @return the task
     */
    public Task addTaskToCard(Task task, long id) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/cards/addTask/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(task, APPLICATION_JSON), Task.class);
    }

    /**
     * @param name name that needs to be updated
     * @param id   id of the card
     * @return the new name (if it worked)
     */
    public Card updateCard(String name, long id) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server)
                .path("api/cards/"+id)//
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(name, APPLICATION_JSON), Card.class);
    }

    /**
     * Updates a board in the database
     *
     * @param board the board to be updated
     * @return the updated board
     */
    public Board updateBoard(Board board) {
        Board b = ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards/update/" + board.getId()) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(board, APPLICATION_JSON), Board.class);
        setCardsParent(b);
        return b;
    }

    /**
     * Updates the details of a card
     * @param card the card to be updated
     * @return the card
     */
    public Card updateCardDetails(Card card) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/cards/update/" + card.getId()) //
                .request(APPLICATION_JSON)//
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    /**
     * Updates a task
     * @param task the task to be updated
     * @return the task
     */
    public Task updateTask(Task task) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/tasks/update/" + task.getId()) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(task, APPLICATION_JSON), Task.class);
    }
    /**
     * @param id id of the searched card
     * @return the searched card
     */
    public Card getCardById(long id) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/cards/"+id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(Card.class);
        //Get parent
    }

    /**
     * Updates the parent CardList of a Card with provided ID
     *
     * @param id    ID of the Card to be updated
     * @param lists old and new CardList of the provided Card
     */
    public void updateParent(long id, List<CardList> lists) {
        ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/cards/updateParent/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(lists, APPLICATION_JSON), Card.class);
    }

    /**
     * Gets all the boards a user has joined
     *
     * @param id the id of the user
     * @return the list of all boards the user has joined
     */
    public List<Board> getBoardsByUserId(long id) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards/user/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

    /**
     * Updates the Card references to corresponding CardLists in the Board
     *
     * @param b Board the Cards of which will have updated references to CardLists
     */
    public static void setCardsParent(Board b) {
        for(CardList cl: b.getList()) {
            setCardsParent(cl);
        }
    }

    /**
     * Updates the Card references of a CardList
     *
     * @param cl CardList the Cards of which will have updated references
     */
    public static void setCardsParent(CardList cl) {
        for(Card c: cl.getCards()) {
            c.setParentCardList(cl);
        }
    }

    /**
     * the delete method for a card from a list, mapped to "deleteCardFromList"
     * @param id the id of the list from which the card should be removed
     * @param cardId the id of the removed card
     * @return  200-OK when the card was deleted or an error
     */
    public Response deleteCardfromList(long id, long cardId) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/lists/" + id + "/delete/" + cardId)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete();
    }

    /**
     * Deletes a Task from a card
     * @param id the id of the card
     * @param taskId the id of the task
     * @return 200 - Ok if the task is successfully deleted
     */
    public Response deleteTaskFromCard(long id, long taskId) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/cards/" + id + "/delete/" + taskId) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON)
                .delete();
    }

    /**
     * Deletes task from the database
     * @param id the id of the task
     * @return 200 - ok if the task was deleted
     */
    public Response deleteTask(long id) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/tasks/delete/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();
    }

    /**
     * Deletes a card from the database
     * @param id the id of the card to be deleted
     * @return 200 - Ok if the card is deleted
     */
    public Response deleteCard(long id) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/cards/delete/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();
    }

    /**
     * @param listid the id of the list to be retrieved
     * @return the card list with the specific id
     */
    public CardList getCL(long listid) {
        CardList cl = ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/lists/" + listid) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(CardList.class);
        setCardsParent(cl);
        return cl;
        // can also use switch statement
    }

    /**
     * @param list the list that is posted
     */
    public void addCL(CardList list) {
        Response res = ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/lists/add") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(list, APPLICATION_JSON));

        if (res.getStatus() != 200) {
            System.out.println("error");
        }
    }

    /**
     * Removes a cardList
     * @param listid the id of the list to be removed
     */
    @SuppressWarnings("unused")
    public void removeCL(long listid) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/lists/delete/" + listid) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();
    }

    /**
     * Modifies the name of the cardList
     * @param listid the id of the list to rename
     * @param name the new name of the cardList
     * @return the CardList with modified name
     */
    @SuppressWarnings("unused")
    public CardList modifyNameCL(long listid, String name) {
        CardList cl = ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/lists/" + listid) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(name, APPLICATION_JSON), CardList.class);
        setCardsParent(cl);
        return cl;
    }

    /**
     * Removes a Tag with a given ID
     * @param id ID of the Tag to be removed
     */
    public void removeTag(long id) {
        ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/tags/delete/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete();
    }

    /**
     * Modifies the Tag of the given ID to the given Tag
     * @param id ID of the Tag to be modified
     * @param tag Tag that the target Tag will be modified to
     * @return modified Tag
     */
    public Tag modifyTag(long id, Tag tag) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/tags/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(tag, APPLICATION_JSON), Tag.class);
    }

    /**
     * @return all lists in the database
     */
    public List<CardList> getAll() {
        List<CardList> list = ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/lists/all") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>(){
                });
        for(CardList cl: list) {
            setCardsParent(cl);
        }
        return list;
    }

    /**
     * Moves the second given Card in front of the first given Card in CardList of provided ID
     * @param id ID of the CardList to be updated
     * @param cards two Cards to be moved
     * @return modified CardList
     */
    public CardList moveCard(long id, List<Card> cards) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/lists/moveCard/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(cards, APPLICATION_JSON), CardList.class);
    }


    /**
     * Updates the colorScheme of the board
     * @param colorScheme the object to be updated
     * @return the ColorScheme response
     */
    public ColorScheme updateColorScheme(ColorScheme colorScheme) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards/updateColorScheme/" + colorScheme.getId()) //
                .request(APPLICATION_JSON)//
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(colorScheme, APPLICATION_JSON), ColorScheme.class);
    }


    /**
     * Sends a request to the server to check if the given password classifies the user
     * as an admin
     * @param password Given password to check for
     * @return True if password is correct, false otherwise
     */
    public boolean isAdmin(String password){
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/admin/" + password)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(Boolean.class);
    }

    /**
     * Sends a request to the server to delete a board with admin privileges
     * @param password Admin password
     * @param boardId Board ID
     * @return True if successful, false otherwise
     */
    public boolean adminDelete(String password, long boardId){
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/admin/" + password + "/delete/" + boardId)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(Boolean.class);
    }

    /**
     * adds a colorScheme to the database
     * @param colorScheme the posted colorScheme
     * @return the posted entity
     */
    public ColorScheme addColorScheme(ColorScheme colorScheme){
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/colors/add")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(colorScheme,APPLICATION_JSON), ColorScheme.class);
    }
    /**
     * gets a colorScheme by id
     * @param id of the searched colorscheme
     * @return the given entity, can throw
     * server exception if not found
     */
    public ColorScheme getColorSchemeById(long id){
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/colors/"+id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(ColorScheme.class);
    }
}
