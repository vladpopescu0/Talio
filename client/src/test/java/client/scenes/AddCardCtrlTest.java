package client.scenes;


import client.utils.ServerUtils;
import com.sun.javafx.application.PlatformImpl;
import commons.Board;
import commons.Card;
import commons.CardList;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;

import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddCardCtrlTest{

    private TextField textFieldMock;
    private MainCtrl mainCtrlMock;
    private ServerUtils serverUtilsMock;
    private BoardViewCtrl boardViewCtrlMock;
    private MainCtrl mainMock;
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
        card = new Card("Test");

        Mockito.when(serverUtilsMock.addCardToList(eq(card), eq(2L))).thenReturn(card);
        Mockito.when(serverUtilsMock.addCardToList(eq(card)
                , not(eq(2L)))).thenThrow(new IllegalStateException());
        Mockito.when(mainCtrlMock.getBoardViewCtrl()).thenReturn(boardViewCtrlMock);
        Mockito.when(boardViewCtrlMock.getBoard()).thenReturn(board);

    }

    /**
     * this makes the fxml work in tests
     */
    @BeforeAll
    void initJfxRuntime() {
        PlatformImpl.startup(() -> {});
    }

    /**
     * testing the cancel method
     * binded to the back button
     */
    @Test
    public void cancelTest(){
        textFieldMock = new TextField("Test");
        sut.title = textFieldMock;
        sut.cancel();
        Mockito.verify(mainCtrlMock).closeSecondaryStage();
    }

    /**
     * ok test normal
     */
    @Test
    public void okTest(){
        Mockito.when(mainCtrlMock.getId()).thenReturn(2L);
        textFieldMock = new TextField("Test");
        sut.title = textFieldMock;
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
        textFieldMock = new TextField("Test");
        sut.title = textFieldMock;
        assertThrows(IllegalStateException.class,()->sut.ok());
        Mockito.verify(serverUtilsMock).addCardToList(eq(card), eq(3L));
    }

    /**
     * test when the text field has an empty field
     */
    @Test
    public void okTestEmptyField(){
        textFieldMock = new TextField("");
        sut.title = textFieldMock;
        sut.ok();
        Mockito.verify(serverUtilsMock,times(0)).addCardToList(eq(card), eq(2L));
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
