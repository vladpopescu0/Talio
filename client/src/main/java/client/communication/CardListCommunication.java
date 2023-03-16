package client.communication;

import commons.CardList;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;


import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class CardListCommunication {

    private static final String SERVER = "http://localhost:8080/";
//    @SuppressWarnings("unused")
//    private final SocketHandler handler; //not using this yet because no synchronization

//    public CardListCommunication(SocketHandler handler) {
//        this.handler = handler;
//    }

    /**
     * @param listid the id of the list to be retrieved
     * @return the card list with the specific id
     */
    @SuppressWarnings("unused")
    public CardList getCL(long listid) {
        Response res = ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/lists/" + listid) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get();

        if (res.getStatus() != 200) {
            System.out.println("error");
            return null;
        } else {
            return res.readEntity(CardList.class);
        }

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
    public void removeCL(long listid) {
        Response res = ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/lists/delete/" + listid) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();

        if (res.getStatus() != 200) {
            System.out.println("error");
        }
    }
    @SuppressWarnings("unused")
    public CardList modifyNameCL(long listid, String name) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/lists/" + listid) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(name, APPLICATION_JSON), CardList.class);

    }

    public List<CardList> getAll() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/lists/all") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>(){
                });
    }


    //        req.submit(new InvocationCallback<Response>() {
//            @Override
//            public void completed(Response response) {
//                if (response.getStatus() == 200) {
//                    return response.readEntity(CardList.class);
//                } else {
//                    System.out.println("Status: " + response.getStatus());
//                }
//            }
//
//            @Override
//            public void failed(Throwable throwable) {
//                throwable.printStackTrace();
//            }
//        });

//        try {
//            response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//        if (response.statusCode() != 200) {
//            System.out.println("Status: " + response.statusCode());
//        }
}
