package View;

import java.awt.*;

public class OverlapLayout implements LayoutManager {
    private final int overlap;

    public OverlapLayout(int overlap) {
        this.overlap = overlap;
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {}

    @Override
    public void removeLayoutComponent(Component comp) {}

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        int width = 0;
        int height = 0;
        for (Component comp : parent.getComponents()) {
            Dimension d = comp.getPreferredSize();
            width = Math.max(width, d.width);
            height += d.height - overlap;
        }
        return new Dimension(width, height + overlap);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    @Override
    public void layoutContainer(Container parent) {
        int y = 0;
        Component[] components = parent.getComponents();
        for (int i = components.length - 1; i >= 0; i--) {
            Component comp = components[i];
            Dimension d = comp.getPreferredSize();
            comp.setBounds(0, y, d.width, d.height);
            y += d.height - overlap;
        }
    }
}