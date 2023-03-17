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

import commons.Board;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import commons.Card;
import commons.CardList;
import org.glassfish.jersey.client.ClientConfig;

import commons.User;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
public class ServerUtils {

    private static final String SERVER = "http://localhost:8080/";

    //public SocketHandler handler = new SocketHandler("ws://localhost:8080/websocket");

    /**
     *Method that gets all boards from the database
     *through the /boards api
     * @return A list of all boards added to the database
     */
    public List<Board> getBoards() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/boards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {});
    }

    /** Returns a board with the specific id, if it exists
     * @param id id of the searched board
     * @return the board
     */
    public Board getBoardByID(Long id) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/boards/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(Board.class);
    }

    /**
     * Adds a board to the database
     * @param board the board to be added
     * @return the new board
     */
    public Board addBoard(Board board) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/boards/add") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(board, APPLICATION_JSON), Board.class);

    }

    /**
     * @param board the board whose name needs to be modified
     * @return the modified board
     */
    public Board modifyBoard(Board board) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/boards/modify") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(board, APPLICATION_JSON), Board.class);
    }

    /**
     * Adds a user to the database
     * @param user the user to be added
     * @return the new user
     */
    public User addUser(User user) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/users/add") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(user, APPLICATION_JSON), User.class);

    }

    /**
     * Get a CardList from the database using its id
     * @param id the id to search in the database, gets bad request if it is not proper
     * @return the CardList that was found
     */
    public CardList getCardListById(long id){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/lists/"+id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {});
    }

    /**
     * Adds a user to the database
     * @param card the card to be added
     * @return the new card
     */
    public Card addCard(Card card){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/cards/add") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    /**
     * Get a list of cards by having a list id, solving the recursion problem
     * @param id the id of the card list
     * @param card the card that needs to be added
     * @return the cards that are connected to that card list
     */
    public Card addCardToList(Card card,long id){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/lists/addCard/"+id)//
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(card,APPLICATION_JSON),Card.class);
    }

    /**
     * @param name name that needs to be updated
     * @param id id of the card
     * @return the new name (if it worked)
     */
    public String updateCard(String name,long id){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/cards/"+id)//
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(name,APPLICATION_JSON),String.class);
    }

    /**
     * @param id id of the searched card
     * @return the searched card
     */
    public Card getCardById(long id){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/cards/"+id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {});
    }


}
