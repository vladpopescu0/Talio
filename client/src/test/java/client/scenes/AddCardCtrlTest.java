package client.scenes;

import client.utils.ServerUtils;

public class AddCardCtrlTest extends AddCardCtrl{

    private MainCtrlTest m;
    private ServerUtils s;
    //to change this to serverUtilsMock or serverUtilsTest
    //hope this works
    public AddCardCtrlTest(ServerUtils serverUtils, MainCtrlTest mainCtrlTest){
        super(serverUtils,mainCtrlTest);
    }

}
