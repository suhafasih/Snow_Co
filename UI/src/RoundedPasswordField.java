import javax.swing.*;
import java.awt.*;

/**
 * Custom JPasswordField that has rounded corners and a semi-transparent background.
 *
 * It matches the style of the RoundTextField for a consistent UI design.
 */
class RoundedPasswordField extends JPasswordField {

    /**
     * Constructor to initialize the password filed
     *
     * @param size Number of columns (the width hint)
     */
    public RoundedPasswordField(int size) {
        super(size);

        // Makes sure background is transparent so we can draw custom shapes
        setOpaque(false);

        // Adds the padding inside the field
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
    }

    /**
     * Custom background painting
     */
    protected void paintComponent(Graphics g) {

        // White background that is semi-transparent
        g.setColor(new Color(255,255,255,230));

        // Drawing the rounded rectangle background
        g.fillRoundRect(0,0,getWidth(),getHeight(),25,25);

        // Let the parent handle the text(passwords dots)
        super.paintComponent(g);
    }

    /**
     * Custom border painting
     */
    protected void paintBorder(Graphics g) {

        // Creates a light gray border
        g.setColor(new Color(200,200,200));

        // Draws a rounded border
        g.drawRoundRect(0,0,getWidth()-1,getHeight()-1,25,25);
    }
}