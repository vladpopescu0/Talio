package client.scenes;

import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;

import static org.junit.jupiter.api.Assertions.*;

public class AdminCheckCtrlTest {

    private MainCtrl mainCtrlMock;
    private ServerUtils serverUtilsMock;
    private AdminCheckCtrl sut;
    private BoardsOverviewCtrl boardsOverviewCtrlMock;

    /**
     * setup tests sut and doc
     */
    @BeforeEach
    public void setup() throws NoSuchFieldException {
        mainCtrlMock = Mockito.mock(MainCtrl.class);
        serverUtilsMock = Mockito.mock(ServerUtils.class);
        boardsOverviewCtrlMock = Mockito.mock(BoardsOverviewCtrl.class);

        String password ="";

        sut = new AdminCheckCtrl(mainCtrlMock,serverUtilsMock);
        Mockito.when(mainCtrlMock.getOverviewCtrl())
                .thenReturn(boardsOverviewCtrlMock);
        Mockito.when(serverUtilsMock.isAdmin("password"))
                .thenReturn(true);
        Mockito.when(serverUtilsMock.isAdmin(not(eq("password"))))
                .thenReturn(false);
        Mockito.when(mainCtrlMock.getAdminPass()).thenReturn(password);
    }

    /**
     * check admin test
     */

    @Test
    public void checkAdminTest(){
        sut.checkAdmin();

        Mockito.verify(serverUtilsMock).isAdmin("password");
        Mockito.verify(mainCtrlMock).setAdmin(true);
        Mockito.verify(mainCtrlMock).setAdminPass("password");
        Mockito.verify(mainCtrlMock).closeSecondaryStage();
        Mockito.verify(mainCtrlMock).showOverview();

    }

    /**
     * additional Help test
     */
    @Test
    public void additionalHelpTest(){
        assertEquals(sut.additionalHelp(),"Admin Login specific shortcuts:\n"
                + "Enter - Submit the password\n"
                + "Escape - Close the page");
    }
}
