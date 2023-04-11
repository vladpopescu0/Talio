package client.scenes;


import client.utils.ServerUtils;
import commons.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;

import org.mockito.Mock;
import org.mockito.Mockito;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

public class EditTagCtrlTest {
    private MainCtrl mainCtrlMock;
    private ServerUtils serverUtilsMock;
    private ViewTagsCtrl viewTagsCtrlMock;
    private EditTagCtrl sut;

    private Board board;
    private Tag tag;

    /**
     * Setup before each test
     */
    @BeforeEach
    public void setup(){
        mainCtrlMock = Mockito.mock(MainCtrl.class);
        serverUtilsMock = Mockito.mock(ServerUtils.class);
        viewTagsCtrlMock = Mockito.mock(ViewTagsCtrl.class);

        tag = new Tag("tag");
        tag.setId(1L);
        User u = new User("u");
        board = new Board(u, "name");
        board.addTag(tag);

        sut = new EditTagCtrl(serverUtilsMock, mainCtrlMock, board, tag);

        Mockito.when(serverUtilsMock.modifyTag(eq(tag.getId()), eq(tag))).thenReturn(tag);
        Mockito.when(serverUtilsMock.modifyTag(not(eq(tag.getId())), not(eq(tag)))).thenThrow(new IllegalStateException());
        Mockito.when(mainCtrlMock.getViewTagsCtrl()).thenReturn(viewTagsCtrlMock);
        Mockito.when(viewTagsCtrlMock.getBoard()).thenReturn(board);
    }

    /**
     * Constructor test
     */
    @Test
    public void constructorTest() {
        sut = new EditTagCtrl(serverUtilsMock, mainCtrlMock, board, tag);
        assertNotNull(sut);
    }

    /**
     * Correct OK Test
     */
    @Test
    public void okTest(){
        sut.ok();
        Tag t = new Tag("t");
        Mockito.verify(serverUtilsMock).modifyTag(eq(tag.getId()), not(eq(tag)));
        Mockito.verify(mainCtrlMock).closeSecondaryStage();
        Mockito.verify(mainCtrlMock).showViewTags(board);
    }

    /**
     * Testing the getter for Board
     */
    @Test
    public void testGetTag() {
        Tag t = new Tag("t");
        sut = new EditTagCtrl(serverUtilsMock, mainCtrlMock, board, t);
        assertEquals(t, sut.getTag());
    }

    /**
     * Testing the setter for Board
     */
    @Test
    public void testSetTag() {
        Tag t = new Tag("t");
        t.setId(3L);
        sut.setTag(t);
        assertEquals(sut.getTag(), t);
    }

    /**
     * Testing the method bound to the cancel button
     */
    @Test
    public void cancelTest() {
        sut.cancel();
        Mockito.verify(mainCtrlMock).closeSecondaryStage();
        Mockito.verify(mainCtrlMock).showViewTags(board);
    }

    /**
     * Testing the method returning additional help for that page
     */
    @Test
    public void additionalHelpTest() {
        assertEquals(sut.additionalHelp(),"Edit Tag specific shortcuts:\n"
                + "Enter - Submit a name change\n"
                + "Escape - Close the page");
    }
}