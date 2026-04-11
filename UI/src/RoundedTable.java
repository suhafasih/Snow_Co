import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Custom JPanel which renders with the rounded corners
 *
 * This is meant to wrap comments like JTable to give
 * a cleaner, modern UI appearance.
 */
public class RoundedTable extends JPanel {

    // Radius of the rounded corners
    private int cornerRadius = 20;

    /**
     * Constructor that allows for a custom layout
     *
     * @param layout Layout manager for panel
     */
    public RoundedTable(LayoutManager layout) {
        super(layout);
        // Make sures the panel is transparent so the custom painting is visible
        setOpaque(false);
    }

    /**
     * Custom painting for the rounded background
     */
    @Override
    protected void paintComponent(Graphics g) {

        // Copy is created for graphics to make safer modifications
        Graphics2D g2 = (Graphics2D) g.create();

        // Enables anti-aliasing for much smoother edges (less ragged corners)
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Uses the panel's background color
        g2.setColor(getBackground());

        // Draws a filled rounded rectangle as the background
        g2.fillRoundRect(0,0,getWidth(),getHeight(),cornerRadius,cornerRadius);

        // Disposes graphics context to free resources
        g2.dispose();

        // Calls the parent method to draw child components
        super.paintComponent(g);
    }
}