import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class DrawingApp extends JFrame {

    private DrawingPanel drawingPanel;
    public DrawingApp() {
        setTitle("Рисовашкин");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        drawingPanel = new DrawingPanel();
        add(drawingPanel, BorderLayout.CENTER);
        createMenu();
        setVisible(true);
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu colorMenu = new JMenu("Цвет");
        JMenuItem redColor = new JMenuItem("Красный");
        JMenuItem greenColor = new JMenuItem("Зеленый");
        JMenuItem blueColor = new JMenuItem("Синий");

        redColor.addActionListener(e -> drawingPanel.setColor(Color.RED));
        greenColor.addActionListener(e -> drawingPanel.setColor(Color.GREEN));
        blueColor.addActionListener(e -> drawingPanel.setColor(Color.BLUE));

        colorMenu.add(redColor);
        colorMenu.add(greenColor);
        colorMenu.add(blueColor);

        JMenu thicknessMenu = new JMenu("Толщина");
        JMenuItem thinLine = new JMenuItem("Тонкая(1px)");
        JMenuItem mediumLine = new JMenuItem("Средняя(5px)");
        JMenuItem thickLine = new JMenuItem("Толстая(10px)");

        thinLine.addActionListener(e -> drawingPanel.setThickness(1));
        mediumLine.addActionListener(e -> drawingPanel.setThickness(5));
        thickLine.addActionListener(e -> drawingPanel.setThickness(10));

        thicknessMenu.add(thinLine);
        thicknessMenu.add(mediumLine);
        thicknessMenu.add(thickLine);
        menuBar.add(colorMenu);
        menuBar.add(thicknessMenu);

        setJMenuBar(menuBar);
    }

    public static void main(String[] args) {new DrawingApp();}
    private class DrawingPanel extends JPanel {
        private Color currentColor = Color.BLACK;
        private int currentThickness = 1;
        private List<Line> lines = new ArrayList<>();

        public DrawingPanel() {
            setBackground(Color.WHITE);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    lines.add(new Line(currentColor, currentThickness, e.getPoint()));
                    repaint();
                }
            });
            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    Line lastLine = lines.get(lines.size() - 1);
                    lastLine.addPoint(e.getPoint());
                    repaint();
                }
            });
        }

        public void setColor(Color color) {
            this.currentColor = color;
        }
        public void setThickness(int thickness) {
            this.currentThickness = thickness;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (Line line : lines) {
                g2d.setColor(line.getColor());
                g2d.setStroke(new BasicStroke(line.getThickness()));
                List<Point> points = line.getPoints();
                for (int i = 1; i < points.size(); i++) {
                    Point p1 = points.get(i - 1);
                    Point p2 = points.get(i);
                    g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }
        }
    }

    private class Line {
        private Color color;
        private int thickness;
        private List<Point> points;

        public Line(Color color, int thickness, Point startPoint) {
            this.color = color;
            this.thickness = thickness;
            this.points = new ArrayList<>();
            this.points.add(startPoint);
        }

        public Color getColor() {
            return color;
        }
        public int getThickness() {
            return thickness;
        }
        public List<Point> getPoints() {
            return points;
        }
        public void addPoint(Point point) {
            points.add(point);
        }
    }
}