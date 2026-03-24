import javax.swing.*;
import java.awt.*;

class RoundedButton extends JButton {

    public RoundedButton(String text) {
        super(text);
        setFocusPainted(false);
        setContentAreaFilled(false);  // This is key - prevents default background painting
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setBorderPainted(false);  // Add this to prevent border painting
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (getModel().isRollover()) {
            g2.setColor(new Color(160, 120, 100));
        } else {
            g2.setColor(new Color(140, 100, 85));
        }

        // Fill the rounded rectangle
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

        // Draw the text
        super.paintComponent(g2);

        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Don't paint border by default
        // If you want a border, uncomment this:
         Graphics2D g2 = (Graphics2D) g.create();
         g2.setColor(new Color(120, 80, 65));
         g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
         g2.dispose();
    }
}