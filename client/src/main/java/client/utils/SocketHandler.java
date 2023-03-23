package client.utils;

//import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
//import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
//import org.springframework.web.socket.client.standard.StandardWebSocketClient;
//import org.springframework.web.socket.messaging.WebSocketStompClient;
import java.lang.reflect.Type;
//import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class SocketHandler {
    private StompSession session;

    /**
     * Empty Constructor for the socketHandler class
     */
    @SuppressWarnings("unused")
    public SocketHandler(){

    }

    /**
     * Constructor for the SocketHandler class using a URL
     * @param server the name of the server
     */
    public SocketHandler(String server) {
        if(server.contains("localhost")){
            String url = server.replace("http","ws") + "websocket";
            session = connect(url);
            return;
        }
        String url = "ws://" + server + "/websocket";
        session = connect(url);
    }

    /**
     * Connects the handler to a URL
     * @param URL the URL to be used
     * @return the session
     *
     */
    private StompSession connect(String URL) {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            return stomp.connect(URL, new StompSessionHandlerAdapter() {
            }).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException();
    }

    /**
     * Looks for updates
     * @param destination the target of the updates
     * @param type the entity type to look for updates
     * @param consumer the consumer
     * @param <T> generics
     */
    public <T> void registerForUpdates(String destination, Class<T> type, Consumer<T> consumer) {
        session.subscribe(destination, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return type;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept((T) payload);
            }
        });
    }

    /**
     * Sends an object
     * @param destination the destination to which the object is sent
     * @param o the object to be sent
     */
    @SuppressWarnings("unused")
    public void send(String destination, Object o) {
        session.send(destination, o);
    }

    /**
     * Closes the session
     */
    @SuppressWarnings("unused")
    public void close() {
        this.session.disconnect();
    }
}
