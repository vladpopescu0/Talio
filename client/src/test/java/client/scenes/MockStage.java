package client.scenes;

import javafx.scene.Node;

public class MockStage {

    private SceneMock scene;

    private String title;
    private Node focusedNode;

    /**
     * empty constructor
     */
    public MockStage(){

    }

    /**
     * getter for the Scene Mock field
     * @return current scene
     */
    public SceneMock getScene() {
        return scene;
    }

    /**
     * setter for the SceneMock
     * @param scene new SceneMock
     */
    public void setScene(SceneMock scene) {
        this.scene = scene;
    }

    /**
     * getter for the title field
     * @return current title to get
     */
    public String getTitle() {
        return title;
    }

    /**
     * setter for the title field
     * @param title new title to change to
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * getter for focused node
     * @return the focused node
     */
    public Node getFocusedNode() {
        return focusedNode;
    }

    /**
     * setter for focused node
     * @param focusedNode new focused node
     */
    public void setFocusedNode(Node focusedNode) {
        this.focusedNode = focusedNode;
    }
}
