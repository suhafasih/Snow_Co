import javax.swing.*;
import java.awt.*;

/**
 * Custom JTextField that has rounded corner and also a semi-transparent background.
 *
 * Enhances UI appearance by replacing the default rectangular look
 * with softer, and modern rounded design.
 */
class RoundedTextField extends JTextField {

    /**
     * Constructor that creates a rounded text field
     *
     * @param size Number of columns (Shows width hint for text field)
     */
    public RoundedTextField(int size) {
        super(size);

        // Makes the background transparent so we are able to custom paint it
        setOpaque(false);

        // Internal padding added (left, top, right, bottom)
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
    }

    /**
     * Custom painting for background
     */
    protected void paintComponent(Graphics g) {

        // Sets a semi-transparent white color
        g.setColor(new Color(255,255,255,230));

        // Draws a filled rounded rectangle as the background
        g.fillRoundRect(0,0,getWidth(),getHeight(),25,25);

        // Lets the parent class handle the text rendering
        super.paintComponent(g);
    }

    /**
     * Custom painting for border
     */
    protected void paintBorder(Graphics g) {

        // light gray color for the border
        g.setColor(new Color(200,200,200));

        // Draws a rounded border slightly inside the component bounds
        g.drawRoundRect(0,0,getWidth()-1,getHeight()-1,25,25);
    }
}