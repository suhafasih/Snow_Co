import javax.swing.*;
import java.awt.*;

class RoundedTextField extends JTextField {

    public RoundedTextField(int size) {
        super(size);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
    }

    protected void paintComponent(Graphics g) {
        g.setColor(new Color(255,255,255,230));
        g.fillRoundRect(0,0,getWidth(),getHeight(),25,25);
        super.paintComponent(g);
    }

    protected void paintBorder(Graphics g) {
        g.setColor(new Color(200,200,200));
        g.drawRoundRect(0,0,getWidth()-1,getHeight()-1,25,25);
    }
}