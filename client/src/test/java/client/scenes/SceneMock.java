package client.scenes;

public class SceneMock {
    private String name;

    /**
     * Constructor for the mocked scene
     * @param name of the scene
     */
    public SceneMock(String name){
        this.name = name;
    }

    /**
     * getter for the name field
     * @return the name of the mocked scene
     */
    public String getName() {
        return name;
    }

    /**
     * setter for the name field
     * @param name new value for field
     */
    public void setName(String name) {
        this.name = name;
    }
}
