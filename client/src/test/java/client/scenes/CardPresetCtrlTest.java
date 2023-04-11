package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class CardPresetCtrlTest {

    private MainCtrl mainCtrl;
    private ServerUtils serverUtils;
    private CardPresetCtrl sut;
    private Board board;
    private Card card;

    /**
     * what to do before each test
     */
    @BeforeEach
    public void setup(){
        mainCtrl = Mockito.mock(MainCtrl.class);
        serverUtils = Mockito.mock(ServerUtils.class);
        board = new Board(null,"test");
        card = new Card("test");
        sut = new CardPresetCtrl(serverUtils, mainCtrl,card,board);
    }

    /**
     * setter and getter test for board
     */
    @Test
    public void getSetBoardTest() {
        Board b = new Board(null,"testExtra");
        sut.setBoard(b);
        assertEquals(sut.getBoard(),b);
    }

    /**
     * test for setter and getter of card
     */
    @Test
    public void setGetCardTest() {
        Card c = new Card("testExtra");
        sut.setCard(c);
        assertEquals(sut.getCard(),c);
    }

    /**
     * back test
     */
    @Test
    public void backTest() {
        Mockito.when(serverUtils.getBoardByID(board.getId())).thenReturn(board);
        sut.back();
        Mockito.verify(mainCtrl).closeSecondaryStage();
        Mockito.verify(mainCtrl).showBoardView(board);
    }
}
