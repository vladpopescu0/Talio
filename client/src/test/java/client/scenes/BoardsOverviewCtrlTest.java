package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BoardsOverviewCtrlTest {

    private ServerUtils serverUtilsMock;
    private MainCtrl mainCtrlMock;
    private List<Board> boards;
    private BoardsOverviewCtrl sut;
    private User test;

    /**
     * setup before each test
     */
    @BeforeEach
    public void setup(){
        serverUtilsMock = Mockito.mock(ServerUtils.class);
        mainCtrlMock = Mockito.mock(MainCtrl.class);

        sut = new BoardsOverviewCtrl(serverUtilsMock,mainCtrlMock);

        test = new User("test");
        test.setId(3);
        boards = new ArrayList<>();
        boards.add(new Board(null,"test"));

        when(mainCtrlMock.isAdmin()).thenReturn(true);
        when(mainCtrlMock.getCurrentUser()).thenReturn(test);
        when(serverUtilsMock.updateBoard(any())).thenReturn(any());
        when(serverUtilsMock.getBoardsByUserId(3)).thenReturn(boards);
    }

    /**
     * init test basic
     */
    @Test
    public void initTest(){
        sut.init();
        verify(serverUtilsMock).setSession(ServerUtils.getUrl());
        verify(serverUtilsMock).getBoardUpdates(any());
        verify(serverUtilsMock).registerForUpdates(
                eq("/topic/boardsUpdate"),
                eq(Long.class),
                any()
        );
        verify(serverUtilsMock).registerForUpdates(
                eq("/topic/boardsRenameDeleteAdd"),
                eq(Long.class),
                any()
        );
        verify(serverUtilsMock).registerForUpdates(
                eq("/topic/deleteBoard"),
                eq(Long.class),
                any()
        );
    }

    /**
     * create board test
     */
    @Test
    public void createBoardTest(){
        sut.createBoard();
        verify(mainCtrlMock).createBoardView();
    }
    /**
     * mocking joining with admin functionality
     * in main ctrl
     */
    @Test
    public void joinBoardIsAdminTest(){
        sut.joinBoard();
        verify(mainCtrlMock, times(3)).getCurrentUser();
        verify(mainCtrlMock).showBoardView(any());
        verify(mainCtrlMock).closeSecondaryStage();
        verify(serverUtilsMock).getBoardsByUserId(3);
        verify(serverUtilsMock).updateBoard(any());
        assertEquals(test.getBoardList(),boards);
    }

    /**
     * mocking joining without admin functionality
     * in main ctrl
     */
    @Test
    public void joinBoardNotAdminTest(){
        when(mainCtrlMock.isAdmin()).thenReturn(false);
        HashMap<Long,String> passwords = new HashMap<>();
        passwords.put(0L,"pass");
        when(mainCtrlMock.getSavedPasswords()).thenReturn(passwords);
        when(serverUtilsMock.checkBoardPassword("pass",0L)).thenReturn(true);
        sut.joinBoard();

        verify(mainCtrlMock, times(3)).getCurrentUser();
        verify(mainCtrlMock).showBoardView(any());
        verify(mainCtrlMock).closeSecondaryStage();
    }

    /**
     * check admin test
     */
    @Test
    public void checkAdminTest(){
        sut.checkAdmin();
        verify(mainCtrlMock).showAdminCheck();
    }

    /**
     * change server test
     */
    @Test
    public void changeServerTest(){
        sut.changeServer();
        verify(mainCtrlMock).showChangeServer();
    }

    /**
     * user Boards test
     */
    @Test
    public void userBoardsTest(){
        sut.userBoards();
        verify(mainCtrlMock).showUserBoardOverview();
        verify(mainCtrlMock).closeSecondaryStage();
    }

    /**
     * to join by link test
     */
    @Test
    public void toJoinByLinkTest(){
        sut.toJoinByLink();
        verify(mainCtrlMock).showJoinBoardByLink();
    }

    /**
     * stop test
     */
    @Test
    public void stopTest(){
        sut.stop();
        verify(serverUtilsMock).stop();
    }

    /**
     * change user test
     */
    @Test
    public void changeUserTest(){
        sut.changeUser();
        verify(mainCtrlMock).showUserView();
    }
}
