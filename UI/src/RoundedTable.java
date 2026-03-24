import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class RoundedTable extends JPanel {

    private int cornerRadius = 20;

    public RoundedTable(LayoutManager layout) {
        super(layout);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0,0,getWidth(),getHeight(),cornerRadius,cornerRadius);

        g2.dispose();

        super.paintComponent(g);
    }
}