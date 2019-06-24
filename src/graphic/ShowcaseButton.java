package graphic;

import javax.swing.*;
import java.awt.*;

public class ShowcaseButton extends JButton {
    private Showable content;

    public ShowcaseButton(Showable content) {
        this.content = content;
        this.setPreferredSize(new Dimension(175,175));
        this.setMaximumSize(new Dimension(175,175));
        this.setMinimumSize(new Dimension(175,175));
        this.setFocusable(false);
        this.setBorderPainted(false);
        this.setText(content.getTitle());
        ImageIcon icon = content.getIcon();
        if(icon == null){
            icon = new ImageIcon(new ImageIcon("./resources/New Icons/Music-icon.png").getImage().getScaledInstance(164,164,Image.SCALE_DEFAULT));
        } else {
            icon = new ImageIcon(icon.getImage().getScaledInstance(164,164,Image.SCALE_DEFAULT));
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
