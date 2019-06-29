package graphic;

import javax.swing.*;
import java.awt.*;

/**
 * this class create any element that show in meddle panel
 * it could be album or media and extending JButton and
 * has instance of Showable interface that sets by an object of a class
 * that implement that interface
 */
public class ShowcaseButton extends JButton {
    private Showable content;

    /**
     * create a button and sets properties
     * @param content that gives imageIcon and title for button
     */
    ShowcaseButton(Showable content) {
        this.content = content;
        this.setPreferredSize(new Dimension(175, 175));
        this.setMaximumSize(new Dimension(175, 175));
        this.setMinimumSize(new Dimension(175, 175));
        this.setForeground(Color.WHITE);
        this.setBackground(Color.BLACK);
        this.setFocusable(false);
        this.setBorderPainted(false);
        this.setText("    " + content.getTitle());
        ImageIcon icon = content.getIcon();
        if (icon == null) {
            icon = new ImageIcon(new ImageIcon("./resources/New Icons/Music-icon.png").getImage().getScaledInstance(164, 164, Image.SCALE_DEFAULT));
        } else {
            icon = new ImageIcon(icon.getImage().getScaledInstance(164, 164, Image.SCALE_DEFAULT));
        }
        this.setIcon(icon);
        this.addActionListener(event -> content.getClicked());

    }

    @Override
    public String toString() {
        return "ShowcaseButton{" +
                "content=" + content +
                '}';
    }
}
