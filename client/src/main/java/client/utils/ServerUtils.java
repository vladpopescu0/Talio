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
import commons.User;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
public class ServerUtils {

    private static final String SERVER = "http://localhost:8080/";

    //public SocketHandler handler = new SocketHandler("ws://localhost:8080/websocket");

//    public void getQuotesTheHardWay() throws IOException {
//        var url = new URL("http://localhost:8080/api/quotes");
//        var is = url.openConnection().getInputStream();
//        var br = new BufferedReader(new InputStreamReader(is));
//        String line;
//        while ((line = br.readLine()) != null) {
//            System.out.println(line);
//        }
//    }

//    public List<Quote> getQuotes() {
//        return ClientBuilder.newClient(new ClientConfig()) //
//                .target(SERVER).path("api/quotes") //
//               .request(APPLICATION_JSON) //
//             .accept(APPLICATION_JSON) //
//                .get(new GenericType<>() {
//                });
//    }

    /**
     *Method that gets all boards from the database
     *through the /boards api
     * @return A list of all boards added to the database
     */
    public List<Board> getBoards() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("boards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {});
    }

    public Response addBoard(Board board) {
        System.out.println("coaie board");
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/boards/add") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(board, APPLICATION_JSON));

    }

    public Response addUser(User user) {
        System.out.println("coaie user");
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/users/add") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(user, APPLICATION_JSON));

    }



//    public void updateQuotes(Consumer<Quote> quote){
//        Future<Response> future = ClientBuilder.newClient(new ClientConfig()) //
//                .target(SERVER).path("api/quotes") //
//                .request(APPLICATION_JSON) //
//                .accept(APPLICATION_JSON) //
//                .async()
//                .get(new InvocationCallback<>() {
//                            @Override
//                            public void completed(Response r) {
//                            }
//
//                            @Override
//                            public void failed(Throwable throwable) {
//                                System.out.println("Doesn't work");
//                            }
//                        });
//    }
}