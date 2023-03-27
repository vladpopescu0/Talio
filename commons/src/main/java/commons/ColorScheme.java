package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public class ColorScheme implements Serializable {
    private String colorBGlight;
    private String colorBGdark;
    private String colorLighter;
    private String colorFont;

    public ColorScheme(String colorBGlight, String colorBGdark, String colorLighter, String colorFont) {
        this.colorBGlight = colorBGlight;
        this.colorBGdark = colorBGdark;
        this.colorLighter = colorLighter;
        this.colorFont = colorFont;
    }
    public ColorScheme(){
        this.colorBGlight = "#FFFFFF"; //white
        this.colorBGdark = "#000000"; //black
        this.colorLighter = "#808080"; // grey
        this.colorFont = "#000000"; //black
    }
    public String getColorBGlight() {
        return colorBGlight;
    }

    public void setColorBGlight(String colorBGlight) {
        this.colorBGlight = colorBGlight;
    }

    public String getColorBGdark() {
        return colorBGdark;
    }

    public void setColorBGdark(String colorBGdark) {
        this.colorBGdark = colorBGdark;
    }

    public String getColorLighter() {
        return colorLighter;
    }

    public void setColorLighter(String colorLighter) {
        this.colorLighter = colorLighter;
    }

    public String getColorFont() {
        return colorFont;
    }

    public void setColorFont(String colorFont) {
        this.colorFont = colorFont;
    }

    @Override
    public boolean equals(Object obj) { return EqualsBuilder.reflectionEquals(this, obj); }

    /**
     * Hash Code method for Board
     * @return hash code
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * toString method for the board class
     * @return this as a String
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
