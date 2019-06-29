package graphic;

import javax.swing.*;

/**
 * this interface use for show song or album image in our middle panel.
 */
public interface Showable {
    /**
     * gets image of a song or album
     * @return imageIcon that is our song or album image.
     */
    ImageIcon getIcon();

    /**
     * updates some elements that influenced by clicking on instance of this interface
     */
    void getClicked();

    /**
     * gets a strring that show name of our showable instance maybe its song or album
     * @return string that is title of that showable.
     */
    String getTitle();
}
