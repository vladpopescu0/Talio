package client.scenes;

import javafx.scene.Node;
import javafx.scene.Scene;

public class MockStage {

    private SceneMock scene;

    private String title;
    private Node focusedNode;
    public MockStage(){

    }

    public SceneMock getScene() {
        return scene;
    }

    public void setScene(SceneMock scene) {
        this.scene = scene;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Node getFocusedNode() {
        return focusedNode;
    }

    public void setFocusedNode(Node focusedNode) {
        this.focusedNode = focusedNode;
    }
}
