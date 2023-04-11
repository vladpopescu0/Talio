package client.scenes;


import client.utils.ServerUtils;
import commons.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;

import org.mockito.Mockito;


import static org.junit.jupiter.api.Assertions.*;

public class EditCardCtrlTest {
    private MainCtrl mainCtrlMock;
    private ServerUtils serverUtilsMock;
    private BoardViewCtrl boardViewCtrlMock;
    private EditCardCtrl sut;
    private Card card;
    private Board board;

    /**
     * Setup before each test
     */
    @BeforeEach
    public void setup() {
        mainCtrlMock = Mockito.mock(MainCtrl.class);
        serverUtilsMock = Mockito.mock(ServerUtils.class);
        boardViewCtrlMock = Mockito.mock(BoardViewCtrl.class);

        card = new Card("card");
        card.setId(5L);
        User u = new User("u");
        board=  new Board(u, "name");

        sut = new EditCardCtrl(serverUtilsMock, mainCtrlMock);

        Mockito.when(serverUtilsMock
                .updateCard(eq(card.getName()), eq(card.getId())))
                .thenReturn(card);
        Mockito.when(mainCtrlMock.getBoardViewCtrl())
                .thenReturn(boardViewCtrlMock);
        Mockito.when(boardViewCtrlMock.getBoard()).thenReturn(board);
        Mockito.when(serverUtilsMock.
                updateCard(eq("Card"), not(eq(0L))))
                .thenThrow(new IllegalStateException());
    }

    /**
     * Constructor test
     */
    @Test
    public void constructorTest() {
        sut = new EditCardCtrl(serverUtilsMock, mainCtrlMock);
        assertNotNull(sut);
    }

    /**
     * Correct OK Test
     */
    @Test
    public void okTest(){
        sut.ok();
        Mockito.verify(serverUtilsMock).updateCard(eq("Card"), eq(0L));
        Mockito.verify(mainCtrlMock).closeSecondaryStage();
        Mockito.verify(mainCtrlMock).getBoardViewCtrl();
        Mockito.verify(boardViewCtrlMock).getBoard();
        Mockito.verify(boardViewCtrlMock).refresh();
        Mockito.verify(boardViewCtrlMock).checkUser();
    }

    /**
     * Testing the method bound to the cancel button
     */
    @Test
    public void cancelTest() {
        sut.cancel();
        Mockito.verify(mainCtrlMock).closeSecondaryStage();
    }

    /**
     * Testing the method returning additional help for that page
     */
    @Test
    public void additionalHelpTest() {
        assertEquals(sut.additionalHelp(),"Edit Card specific shortcuts:\n"
                + "Enter - Submit the name change\n"
                + "Escape - Close the page");
    }
}