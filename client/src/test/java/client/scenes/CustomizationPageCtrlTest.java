package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.ColorScheme;
import jakarta.ws.rs.WebApplicationException;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;

import static org.junit.jupiter.api.Assertions.*;

public class CustomizationPageCtrlTest {

    private MainCtrl mainCtrl;
    private ServerUtils serverUtils;
    private Board board;
    private ColorScheme colorScheme;
    private CustomizationPageCtrl sut;

    /**
     * setup before each test
     */
    @BeforeEach
    public void setup(){
        mainCtrl = Mockito.mock(MainCtrl.class);
        serverUtils = Mockito.mock(ServerUtils.class);
        board = new Board(null,"TestBoard");
        colorScheme = new ColorScheme("#027883"
                ,"#027883","#027883","#FFFFFF");
        board.getColorScheme().setColorBGlight("red");
        board.getColorScheme().setColorBGdark("blue");
        board.getColorScheme().setColorLighter("yellow");
        board.getColorScheme().setColorFont("black");
        board.setId(3L);
        sut = new CustomizationPageCtrl(mainCtrl,board,serverUtils);
        Mockito.when(mainCtrl.colorToHex(Color.WHITE)).thenReturn("#FFFFFF");
        Mockito.when(serverUtils.updateBoard(Mockito.any(Board.class))).thenReturn(board);
        Mockito.when(serverUtils.getBoardByID(3L)).thenReturn(board);
        Mockito.when(serverUtils.getBoardByID(not(eq(3L))))
                .thenThrow(new WebApplicationException());
        Mockito.when(serverUtils
                .addColorScheme(Mockito.any(ColorScheme.class))).thenReturn(colorScheme);
    }
    /**
     * get board font test
     */
    @Test
    public void getBoardFontTest(){
        assertNull(sut.getBoardFont());
    }
    /**
     * get board bg test
     */
    @Test
    public void getBoardBGTest(){
        assertNull(sut.getBoardBG());
    }
    /**
     * get list font test
     */
    @Test
    public void getListFontTest(){
        assertNull(sut.getListFont());
    }

    /**
     * get list bg test
     */
    @Test
    public void getListBGTest(){
        assertNull(sut.getListBG());
    }

    /**
     * reset board color test
     */
    @Test
    public void resetTest(){
        sut.reset();
        assertEquals(board.getColorScheme(),colorScheme);
        Mockito.verify(serverUtils).updateBoard(Mockito.any(Board.class));
    }

    /**
     * reset lists test
     */
    @Test
    public void resetListsTest(){
        sut.resetLists();
        assertEquals(board.getListsColorScheme(),colorScheme);
        Mockito.verify(serverUtils).updateBoard(Mockito.any(Board.class));
    }

    /**
     * getter and setter test
     */
    @Test
    public void getBoardTest(){
        sut.setBoard(board);
        assertEquals(sut.getBoard(),board);
    }

    /**
     * add preset when not thrown test
     */
    @Test
    public void addPresetNormalTest(){
        sut.addPreset();
        assertTrue(board.getCardsColorSchemesList().contains(colorScheme));
        Mockito.verify(serverUtils).updateBoard(board);
    }
    /**
     * add preset throws exception
     */
    @Test
    public void addPresetThrowTest(){
        board.setId(30L);
        sut.addPreset();
        Mockito.verify(serverUtils, Mockito.times(0)).updateBoard(board);
    }
}
