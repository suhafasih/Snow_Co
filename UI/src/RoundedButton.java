import javax.swing.*;
import java.awt.*;

class RoundedButton extends JButton {

    public RoundedButton(String text) {
        super(text);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        if(getModel().isRollover())
            g2.setColor(new Color(160,120,100));
        else
            g2.setColor(new Color(140,100,85));

        g2.fillRoundRect(0,0,getWidth(),getHeight(),30,30);

        super.paintComponent(g);
    }

    protected void paintBorder(Graphics g) {
        g.setColor(new Color(120,80,65));
        g.drawRoundRect(0,0,getWidth()-1,getHeight()-1,30,30);
    }
}