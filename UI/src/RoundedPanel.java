import javax.swing.*;
import java.awt.*;

/**
 * Custom JPanel that has rounded corners
 *
 * Shows great use as a container for grouping the UI elements with modern, user friendly
 * appearance.
 */
public class RoundedPanel extends JPanel {

    // The radius for the rounder corners
    private int radius;

    /**
     * Constructor that sets the corner radius
     *
     * @param radius the roundness of the panel corners
     */
    public RoundedPanel(int radius) {
        this.radius = radius;

        //Makes the panels transparent so the custom drawing is visible
        setOpaque(false);
    }

    /**
     * Custom painting for the rounded background
     */
    @Override
    protected void paintComponent(Graphics g) {

        // Graphic2D is cast for advanced rendering
        Graphics2D g2 = (Graphics2D) g;

        // This enables smooth edges (anti-aliasing)
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Sets the background color
        g2.setColor(getBackground());

        // Draws the rounded rectangle background
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        // Calls the parent to extend child components
        super.paintComponent(g);
    }
}