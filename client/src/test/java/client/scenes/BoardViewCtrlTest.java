package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.CardList;
import commons.Tag;
import commons.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BoardViewCtrlTest {

    private ServerUtils serverUtilsMock;
    private MainCtrl mainCtrlMock;
    private BoardViewCtrl sut;
    private Board board;
    private List<Board> boards = new ArrayList<>();
    private User testUser;

    /**
     * setup before each test
     */
    @BeforeEach
    public void setup(){
        serverUtilsMock = Mockito.mock(ServerUtils.class);
        mainCtrlMock = Mockito.mock(MainCtrl.class);
        testUser = new User("john");
        testUser.setId(2L);
        board = new Board(testUser,"test");
        board.setId(3L);
        sut = new BoardViewCtrl(serverUtilsMock,mainCtrlMock,board);
        boards.add(board);
        testUser.setBoardList(boards);
        when(mainCtrlMock.getCurrentUser()).thenReturn(testUser);
        when(serverUtilsMock.updateBoard(board)).thenReturn(board);
        when(serverUtilsMock.getBoardsByUserId(testUser.getId()))
                .thenReturn(boards);
        when(serverUtilsMock.deleteBoard(board.getId())).thenReturn(null);
    }

    /**
     * test for getter and setter of the class
     */
    @Test
    public void setGetBoardTest(){
        board = new Board(testUser,"new");
        sut.setBoard(board);
        assertEquals(sut.getBoard(),board);
    }

    /**
     * test for cancel method
     */
    @Test
    public void cancelTest(){
        sut.cancel();
        verify(mainCtrlMock).closeSecondaryStage();
        verify(mainCtrlMock).showOverview();
    }

    /**
     * test for setfocused method
     */
    @Test
    public void setFocusedBackupTest(){
        sut.setFocusedNodeBackup(null);
        assertNull(sut.getFocusedNodeBackup());
    }

    /**
     * test for remove user
     */
    @Test
    public void removeUserTest(){
        sut.removeUser();
        verify(mainCtrlMock,times(3)).getCurrentUser();
        verify(serverUtilsMock).updateBoard(board);
        verify(serverUtilsMock).getBoardsByUserId(testUser.getId());
        assertFalse(board.getUsers().contains(testUser));
    }

    /**
     * test for unlock test
     */
    @Test
    public void unlockTest(){
        sut.unlock();
        verify(mainCtrlMock).showCheckBoardPasswordView(board);
    }

    /**
     * test for edit password
     */
    @Test
    public void editPasswordTest(){
        sut.editPassword();
        verify(mainCtrlMock).showChangeBoardPasswordView(board);
    }

    /**
     * test for edit title
     */
    @Test
    public void editTitleTest(){
        sut.editTitle();
        verify(mainCtrlMock).showEditBoardNameView(board);
    }

    /**
     * test for user overview
     */
    @Test
    public void toUserOverviewTest(){
        sut.toUserOverview();
        verify(mainCtrlMock).closeSecondaryStage();
        verify(mainCtrlMock).showUserBoardOverview();
    }

    /**
     * test for view tags
     */
    @Test
    public void viewTagsTest(){
        sut.viewTags();
        verify(mainCtrlMock).closeSecondaryStage();
        verify(mainCtrlMock).showViewTags(board);
    }

    /**
     * test for copy link when id has only one digit
     * @throws IOException thrown by clipboard
     * @throws UnsupportedFlavorException thrown by clipboard
     */
    @Test
    public void copyLinkTest() throws IOException, UnsupportedFlavorException {
        sut.isAnimationPlayed = true;
        sut.copyLink();
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        assertEquals(Long.parseLong(clipboard.getData(DataFlavor.stringFlavor).toString())
                ,board.getId());
    }
    /**
     * test for copy link when id has only two digits
     * @throws IOException thrown by clipboard
     * @throws UnsupportedFlavorException thrown by clipboard
     */
    @Test
    public void copyLinkTwoZerosTest() throws IOException, UnsupportedFlavorException {
        sut.isAnimationPlayed = true;
        board.setId(34L);
        sut.copyLink();
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        assertEquals(Long.parseLong(clipboard.getData(DataFlavor.stringFlavor).toString())
                ,board.getId());
    }
    /**
     * test for copy link when id has only three digits
     * @throws IOException thrown by clipboard
     * @throws UnsupportedFlavorException thrown by clipboard
     */
    @Test
    public void copyLinkOneZerosTest() throws IOException, UnsupportedFlavorException {
        sut.isAnimationPlayed = true;
        board.setId(394L);
        sut.copyLink();
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        assertEquals(Long.parseLong(clipboard.getData(DataFlavor.stringFlavor).toString())
                ,board.getId());
    }
    /**
     * test for copy link when id has more than 3 digits
     * @throws IOException thrown by clipboard
     * @throws UnsupportedFlavorException thrown by clipboard
     */
    @Test
    public void copyLinkNoZerosTest() throws IOException, UnsupportedFlavorException {
        sut.isAnimationPlayed = true;
        board.setId(3924L);
        sut.copyLink();
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        assertEquals(Long.parseLong(clipboard.getData(DataFlavor.stringFlavor).toString())
                ,board.getId());
    }

    /**
     * add card list test
     */
    @Test
    public void addCardListTest(){
        sut.addCardList();
        verify(mainCtrlMock).showCreateList(board);
    }

    /**
     * delete board test where the board
     * has card lists and tags
     */
    @Test
    public void deleteBoardHasAllTest(){
        List<Tag> tags = new ArrayList<>();

        Tag tag1 = new Tag("tag1");
        tag1.setId(101);
        tags.add(tag1);

        Tag tag2 = new Tag("tag2");
        tag2.setId(102);
        tags.add(tag2);

        doAnswer(invocation -> {
            tags.remove(tag1);
            return null;
        }).when(serverUtilsMock).removeTag(101);

        doAnswer(invocation -> {
            tags.remove(tag2);
            return null;
        }).when(serverUtilsMock).removeTag(102);

        List<CardList> cardlists = new ArrayList<>();

        CardList cardList1 = new CardList("c1");
        cardList1.setId(201L);
        cardlists.add(cardList1);

        CardList cardList2 = new CardList("c2");
        cardList2.setId(202L);
        cardlists.add(cardList2);

        doAnswer(invocation -> {
            cardlists.remove(cardList1);
            return null;
        }).when(serverUtilsMock).removeCL(201L);

        doAnswer(invocation -> {
            cardlists.remove(cardList2);
            return null;
        }).when(serverUtilsMock).removeCL(202L);

        board = new Board(testUser,cardlists,"test",tags);
        board.setId(3L);
        sut.setBoard(board);
        board.removeUser(testUser);
        sut.deleteBoard();

        verify(serverUtilsMock).updateBoard(board);
        verify(serverUtilsMock).deleteBoard(board.getId());
        verify(mainCtrlMock).showUserBoardOverview();

        //the method used with doAnswer is getting overwritten, and
        //it only deletes the last element, so the size should be one
        //less than the original, having one element is not possible
        //either, since there is a concurrentDeletionException in mockito
        assertEquals(tags.size(),1);
        assertEquals(cardlists.size(),1);
    }
}
