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
import commons.User;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class MainCtrlTest{

    private MainCtrl sut;


    /**
     * sets up the mainCtrl for the application
     */
    @BeforeEach
    public void setup() {
        sut = new MainCtrl();
    }

    /**
     * test for the color to hex
     */
    @Test
    public void colorToHexTest(){
        String testColor = sut.colorToHex(new Color(1,0,1,1));
        assertEquals(testColor,"#FF00FF");
    }

    /**
     * double test for id setter and getter
     */
    @Test
    public void idSetterAndGetterTest(){
        sut.setId(34);
        assertEquals(sut.getId(),34);
    }

    /**
     * double test for the card id setter and getter
     */
    @Test
    public void cardIdSetterAndGetterTest(){
        sut.setCardId(34);
        assertEquals(sut.getCardId(),34);
    }

    /**
     * set admin and is admin test
     */
    @Test
    public void setAdminIsAdminTest(){
        sut.setAdmin(false);
        assertFalse(sut.isAdmin());
    }

    /**
     * admin password test
     */
    @Test
    public void adminPassTest(){
        String pass = "cabdpRmPiEjh";
        sut.setAdminPass(pass);
        assertEquals(sut.getAdminPass(),pass);
    }

    /**
     * setter and getter current user test
     */
    @Test
    public void setAndGetCurrentUserTest(){
        User u = new User("name");
        sut.setCurrentUser(u);
        assertEquals(new User("name"),sut.getCurrentUser());
    }

    /**
     * saved password test
     */
    @Test
    void getterAndSetterSavedPasswordsTest() {
        HashMap<Long, String> savedPasswords = new HashMap<>();
        savedPasswords.put(13L,"Test");
        savedPasswords.put(14L,"More Test");
        savedPasswords.put(15L,"Extra Test");
        sut.setSavedPasswords(savedPasswords);
        assertEquals(sut.getSavedPasswords(),savedPasswords);
    }

    /**
     * update when there is a board with that password
     */
    @Test
    void updatePasswordExistsTest(){
        HashMap<Long, String> savedPasswords = new HashMap<>();
        savedPasswords.put(13L,"Test");
        savedPasswords.put(14L,"More Test");
        savedPasswords.put(15L,"Extra Test");
        sut.setSavedPasswords(savedPasswords);
        sut.passwordFile = new File("./file.txt");
        sut.updatePassword(13L, "Secret");
        assertEquals("Secret", sut.getSavedPasswords().get(13L));
        sut.passwordFile.delete();
    }

    /**
     * test when no such board has a password
     */
    @Test
    void updatePasswordNotExistsTest(){
        HashMap<Long, String> savedPasswords = new HashMap<>();
        savedPasswords.put(13L,"Test");
        savedPasswords.put(14L,"More Test");
        savedPasswords.put(15L,"Extra Test");
        sut.setSavedPasswords(savedPasswords);
        sut.passwordFile = new File("./file.txt");
        sut.updatePassword(12L, "Secret");
        assertEquals("Secret", sut.getSavedPasswords().get(12L));
        sut.passwordFile.delete();
    }

}