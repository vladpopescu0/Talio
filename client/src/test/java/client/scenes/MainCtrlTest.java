/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;
import client.utils.ServerUtils;
import commons.User;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MainCtrlTest extends MainCtrl{

    private MainCtrl sut;
    private MockStage primaryStageMock;
    private MockStage secondaryStageMock;
    private MockStage helpStageMock;

    private AddCardCtrlTest addCardCtrlTest;

    /**
     * sets up the mainCtrl for the application
     */
    @BeforeEach
    public void setup() {
        sut = new MainCtrl();
        primaryStageMock = new MockStage();
        secondaryStageMock = new MockStage();
        helpStageMock = new MockStage();
        addCardCtrlTest = new AddCardCtrlTest(new ServerUtils(),this);
    }

    @Test
    public void colorToHexTest(){
        String testColor = sut.colorToHex(new Color(1,0,1,1));
        assertEquals(testColor,"#FF00FF");
    }

    @Test
    public void idSetterAndGetterTest(){
        sut.setId(34);
        assertEquals(sut.getId(),34);
    }

    @Test
    public void cardIdSetterAndGetterTest(){
        sut.setCardId(34);
        assertEquals(sut.getCardId(),34);
    }

    @Test
    public void setAdminIsAdminTest(){
        sut.setAdmin(false);
        assertFalse(sut.isAdmin());
    }

    @Test
    public void adminPassTest(){
        String pass = "cabdpRmPiEjh";
        sut.setAdminPass(pass);
        assertEquals(sut.getAdminPass(),pass);
    }

    @Test
    public void setAndGetCurrentUserTest(){
        User u = new User("name");
        sut.setCurrentUser(u);
        assertEquals(new User("name"),sut.getCurrentUser());
    }
    @Override
    public void closeSecondaryStage(){
        secondaryStageMock=null;
    }
    @Override
    public void closeHelpStage(){
        helpStageMock=null;
    }
    @Override
    public Node getFocusedNode(){
        return primaryStageMock.getFocusedNode();
    }

    @Override
    public void showAddCard(){
        secondaryStageMock.setScene(new SceneMock("addCard"));
        secondaryStageMock.setTitle("Add Card");
    }
}