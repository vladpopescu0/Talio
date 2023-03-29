package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ColorPair {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String colorBG;

    private String colorFont;

    /**
     * Empty constructor
     */
    public ColorPair(){

    }

    /**
     * @param colorBG the background color
     * @param colorFont the font color
     */
    public ColorPair(String colorBG, String colorFont){
        this.colorBG = colorBG;
        this.colorFont = colorFont;
    }

    /**
     * @return the background color
     */
    public String getColorBG() {
        return colorBG;
    }
    /**
     * @return the font color
     */
    public String getColorFont() {
        return colorFont;
    }

    /**
     * @param colorBG sets the background color
     */
    public void setColorBG(String colorBG) {
        this.colorBG = colorBG;
    }
    /**
     * @param colorFont sets the background color
     */
    public void setColorFont(String colorFont) {
        this.colorFont = colorFont;
    }
    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }
}
