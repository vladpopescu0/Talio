package client.communication;

import commons.Board;
import commons.Card;
import commons.CardList;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static client.utils.ServerUtils.unpackCardList;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class CardListCommunication {

    private static final String SERVER = "http://localhost:8080/";
//    @SuppressWarnings("unused")
//    private final SocketHandler handler; //not using this yet because no synchronization

//    public CardListCommunication(SocketHandler handler) {
//        this.handler = handler;
//    }

    private static ExecutorService EXEC = Executors.newSingleThreadExecutor();

    public void cardListUpdates(Consumer<CardList> cons) {

        EXEC.submit(() -> {
            while (!Thread.interrupted()) {
                var list = ClientBuilder.newClient(new ClientConfig()) //
                        .target(SERVER).path("api/lists/updates") //
                        .request(APPLICATION_JSON) //
                        .accept(APPLICATION_JSON) //
                        .get(Response.class);

                if (list.getStatus() == 204) {
                    continue;
                }
                var q = list.readEntity(CardList.class);
                cons.accept(q);
            }
        });

    }

    public void stop(){
        EXEC.shutdownNow();
    }

    /**
     * @param listid the id of the list to be retrieved
     * @return the card list with the specific id
     */
    public CardList getCL(long listid) {
        CardList cl = ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/lists/" + listid) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(CardList.class);
        unpackCardList(cl);
        return cl;
        // can also use switch statement
    }

    /**
     * @param list the list that is posted
     */
    public void addCL(CardList list) {
        Response res = ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/lists/add") //
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
                .target(SERVER).path("api/lists/delete/" + listid) //
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
                .target(SERVER).path("api/lists/" + listid) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(name, APPLICATION_JSON), CardList.class);
        unpackCardList(cl);
        return cl;
    }

    /**
     * @return all lists in the database
     */
    public List<CardList> getAll() {
        List<CardList> list = ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/lists/all") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>(){
                });
        for(CardList cl: list) {
            unpackCardList(cl);
        }
        return list;
    }

    /**
     * Moves the second given Card in front of the first given Card in CardList of provided ID
     * @param id ID of the CardList to be updated
     * @param cards two Cards to be moved
     */
    public void moveCard(long id, List<Card> cards) {
        ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/lists/moveCard/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(cards, APPLICATION_JSON), Card.class);
    }
}
