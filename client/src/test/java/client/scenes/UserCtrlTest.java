package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class UserCtrlTest {

    private ServerUtils serverUtils;
    private MainCtrl mainCtrl;
    private UserCtrl sut;
    private User uTest;
    private List<Board> boards;
    private List<User> mockUsers;

    /**
     * setup the mocks for each test
     */
    @BeforeEach
    public void setup(){
        serverUtils = Mockito.mock(ServerUtils.class);
        mainCtrl = Mockito.mock(MainCtrl.class);
        sut = new UserCtrl(serverUtils,mainCtrl);
        mockUsers = new ArrayList<>();
        uTest = new User("default");
        uTest.setId(3L);
        mockUsers.add(uTest);

        boards = new ArrayList<>();

        Mockito.when(serverUtils.getUserByUsername("default")).thenReturn(mockUsers);
        Mockito.when(mainCtrl.getCurrentUser()).thenReturn(uTest);
        Mockito.when(serverUtils.getBoardsByUserId(3L)).thenReturn(boards);
    }

    /**
     * get username test
     */
    @Test
    public void getUsernameTest(){
        assertEquals(sut.getUsername(),"default");
    }

    /**
     * log in user test
     */
    @Test
    public void logInUserTest(){
        sut.logUserIn();
        Mockito.verify(mainCtrl, Mockito.times(2)).getCurrentUser();
        Mockito.verify(serverUtils, Mockito.times(1)).getBoardsByUserId(3L);
        Mockito.verify(serverUtils, Mockito.times(2)).getUserByUsername("default");
        Mockito.verify(mainCtrl).loadPasswords();
        Mockito.verify(mainCtrl).showOverview();
        assertEquals(uTest.getBoardList(),boards);
    }

    /**
     * create User Test
     */
    @Test
    public void createUserTest(){
        mockUsers.remove(uTest);
        Mockito.doAnswer(invocation -> {
            mockUsers.add(invocation.getArgument(0));
            return null;
        }).when(serverUtils).addUser(Mockito.any());
        sut.createUser();

        Mockito.verify(mainCtrl, Mockito.times(1)).getCurrentUser();
        Mockito.verify(serverUtils, Mockito.times(2)).getUserByUsername("default");
        Mockito.verify(mainCtrl).loadPasswords();
        Mockito.verify(mainCtrl).showOverview();
        assertEquals(uTest.getBoardList(),boards);
    }
}
