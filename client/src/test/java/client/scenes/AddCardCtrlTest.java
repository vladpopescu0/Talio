package client.scenes;


import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;

import org.mockito.Mockito;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

public class AddCardCtrlTest{
    private MainCtrl mainCtrlMock;
    private ServerUtils serverUtilsMock;
    private BoardViewCtrl boardViewCtrlMock;
    private AddCardCtrl sut;
    private Card card;
    //to change this to serverUtilsMock or serverUtilsTest
    //hope this works

    /**
     * setup tests sut and doc
     */
    @BeforeEach
    public void setup(){
        mainCtrlMock = Mockito.mock(MainCtrl.class);
        serverUtilsMock = Mockito.mock(ServerUtils.class);
        boardViewCtrlMock = Mockito.mock(BoardViewCtrl.class);

        sut = new AddCardCtrl(serverUtilsMock,mainCtrlMock);

        CardList cardList = new CardList("list test");
        cardList.setId(2L);
        Board board = new Board();
        board.setId(3L);
        card = new Card("Card");

        Mockito.when(serverUtilsMock.addCardToList(eq(card), eq(2L))).thenReturn(card);
        Mockito.when(serverUtilsMock.addCardToList(eq(card)
                , not(eq(2L)))).thenThrow(new IllegalStateException());
        Mockito.when(mainCtrlMock.getBoardViewCtrl()).thenReturn(boardViewCtrlMock);
        Mockito.when(boardViewCtrlMock.getBoard()).thenReturn(board);
    }

    /**
     * testing the cancel method
     * binded to the back button
     */
    @Test
    public void cancelTest(){
        sut.cancel();
        Mockito.verify(mainCtrlMock).closeSecondaryStage();
    }

    /**
     * ok test normal
     */
    @Test
    public void okTest(){
        Mockito.when(mainCtrlMock.getId()).thenReturn(2L);
        sut.ok();
        Mockito.verify(serverUtilsMock).addCardToList(eq(card), eq(2L));
        Mockito.verify(mainCtrlMock).closeSecondaryStage();

        Mockito.verify(mainCtrlMock, times(2)).getBoardViewCtrl();

        Mockito.verify(mainCtrlMock.getBoardViewCtrl()).setBoard(Mockito.any(Board.class));
        Mockito.verify(mainCtrlMock.getBoardViewCtrl()).refresh();
        Mockito.verify(mainCtrlMock.getBoardViewCtrl()).checkUser();
    }

    /**
     * test when there is no such id
     */
    @Test
    public void okTestBadRequest(){
        Mockito.when(mainCtrlMock.getId()).thenReturn(3L);
        assertThrows(IllegalStateException.class,()->sut.ok());
        Mockito.verify(serverUtilsMock).addCardToList(eq(card), eq(3L));
    }

    /**
     * additional help basic test
     */
    @Test
    public void additionalHelpTest(){
        assertEquals(sut.additionalHelp(),"Add Card specific shortcuts:\n"
                + "Enter - Create a card\n"
                + "Escape - Close the page");
    }

    /**
     * contstructor test
     */
    @Test
    public void constructorTest(){
        sut = new AddCardCtrl(serverUtilsMock,mainCtrlMock);
        assertNotNull(sut);
    }
}
