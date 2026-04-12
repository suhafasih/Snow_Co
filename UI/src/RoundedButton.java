import javax.swing.*;
import java.awt.*;

/**
 * Custom JButton that has rounded corners and hover effects.
 *
 * Button removes default Swing styling and it gets replaced
 * with a smooth, modern look for the UI.
 */
class RoundedButton extends JButton {

    /**
     * Constructor to initialize button with text
     *
     * @param text the label that is displayed on the button
     */
    public RoundedButton(String text) {


        super(text);

        // Disables the default button styling
        setFocusPainted(false);
        setContentAreaFilled(false);  // This is key - prevents default background painting

        // Sets the color and font
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setBorderPainted(false);  // Add this to prevent border painting
    }

    /**
     * Custom painting for the button background and the hover effect
     */
    @Override
    protected void paintComponent(Graphics g) {

        // Creates a copy of Graphics2D
        Graphics2D g2 = (Graphics2D) g.create();

        // This enables smooth edges
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Changes the color on hover (this is the rollover effect)
        if (getModel().isRollover()) {
            g2.setColor(new Color(160, 120, 100));
        } else {
            g2.setColor(new Color(140, 100, 85));
        }

        // Fill the rounded rectangle
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

        // Draw the text
        super.paintComponent(g2);

        // Cleans up graphics
        g2.dispose();
    }

    /**
     * Border rendering that is optional
     */
    @Override
    protected void paintBorder(Graphics g) {
        // Don't paint border by default
        // If you want a border, uncomment this:
         Graphics2D g2 = (Graphics2D) g.create();

         // Sets the border color
         g2.setColor(new Color(120, 80, 65));

         // Drawing for the rounded border
         g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
         g2.dispose();
    }
}