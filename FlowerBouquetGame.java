import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class FlowerBouquetGame extends JFrame {

    private JPanel leftPanel, rightPanel;
    private JLabel infoLabel, flowerImageLabel;
    private JButton addButton, clearButton;

    private ArrayList<Flower> flowers = new ArrayList<>();
    private ArrayList<Flower> bouquetFlowers = new ArrayList<>();

    private Flower selectedFlower = null;
    private BouquetPanel bouquetPanel;

    public FlowerBouquetGame() {
        setTitle("Wildflower Bouquet Builder");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        loadFlowerData();

        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(420, 700));

        flowerImageLabel = new JLabel("Select a flower to see its picture.");
        flowerImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        flowerImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        flowerImageLabel.setPreferredSize(new Dimension(350, 280));
        leftPanel.add(flowerImageLabel);

        infoLabel = new JLabel("<html><div style='text-align:center; width:350px;'>Select a flower to learn more!</div></html>");
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        leftPanel.add(infoLabel);

        addButton = new JButton("Add to Bouquet");
        addButton.setEnabled(false);
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.addActionListener(e -> addToBouquet());
        leftPanel.add(addButton);

        clearButton = new JButton("Clear Bouquet");
        clearButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        clearButton.addActionListener(e -> {
            bouquetFlowers.clear();
            bouquetPanel.repaint();
        });

        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(clearButton);

        add(leftPanel, BorderLayout.WEST);

        bouquetPanel = new BouquetPanel(bouquetFlowers);
        add(bouquetPanel, BorderLayout.CENTER);

        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 20));
        rightPanel.setPreferredSize(new Dimension(240, 700));

        JLabel title = new JLabel("Pick Your Flowers:");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        rightPanel.add(title);

        for (Flower f : flowers) {
            JButton btn = new JButton(f.name);
            btn.setMaximumSize(new Dimension(220, 32));
            btn.addActionListener(e -> displayFlower(f));

            rightPanel.add(Box.createRigidArea(new Dimension(0, 8)));
            rightPanel.add(btn);
        }

        add(rightPanel, BorderLayout.EAST);
    }

    private void loadFlowerData() {
        try {
            File file = new File("flower.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String name = scanner.nextLine().trim();

                if (name.isEmpty()) {
                    continue;
                }

                String desc = scanner.nextLine().trim();
                String normalImage = scanner.nextLine().trim();
                String bouquetImage = getBouquetImageName(normalImage);

                flowers.add(new Flower(name, desc, normalImage, bouquetImage));
            }

            scanner.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading flower.txt");
        }
    }

    private String getBouquetImageName(String normalImage) {
        if (normalImage.equals("poppy.jpeg")) {
            return "poppy-removebg-preview.png";
        } else if (normalImage.equals("yarrow.jpeg")) {
            return "yarrow-removebg-preview.png";
        } else if (normalImage.equals("sunflower.jpeg")) {
            return "sunflower-removebg-preview.png";
        } else if (normalImage.equals("blueFlax.jpeg")) {
            return "blueFlax-removebg-preview.png";
        } else if (normalImage.equals("chuparosa.jpeg") || normalImage.equals("chaparosa.jpeg")) {
            return "chaparosa-removebg-preview.png";
        } else if (normalImage.equals("arroyoLupine.jpeg")) {
            return "arroyoLupine-removebg-preview.png";
        } else if (normalImage.equals("winecupClarkia.jpeg") || normalImage.equals("winecupClarika.jpeg")) {
            return "winecupClarika-removebg-preview.png";
        }

        return normalImage;
    }

    private void displayFlower(Flower f) {
        selectedFlower = f;

        infoLabel.setText("<html><div style='text-align:center; width:350px;'>" +
                "<b>" + f.name + "</b><br><br>" + f.description +
                "</div></html>");

        ImageIcon icon = new ImageIcon(f.normalImagePath);

        if (icon.getIconWidth() > 0) {
            Image img = icon.getImage().getScaledInstance(230, 230, Image.SCALE_SMOOTH);
            flowerImageLabel.setIcon(new ImageIcon(img));
            flowerImageLabel.setText("");
        } else {
            flowerImageLabel.setIcon(null);
            flowerImageLabel.setText("[ Missing: " + f.normalImagePath + " ]");
        }

        addButton.setEnabled(true);
    }

    private void addToBouquet() {
        if (selectedFlower != null && !bouquetFlowers.contains(selectedFlower)) {
            bouquetFlowers.add(selectedFlower);
            bouquetPanel.repaint();
        }
    }

    static class Flower {
        String name;
        String description;
        String normalImagePath;
        String bouquetImagePath;

        Flower(String n, String d, String normalImg, String bouquetImg) {
            name = n;
            description = d;
            normalImagePath = normalImg;
            bouquetImagePath = bouquetImg;
        }
    }

    static class BouquetPanel extends JPanel {
        private ArrayList<Flower> bouquetFlowers;
        private Image bouquetCover;

        public BouquetPanel(ArrayList<Flower> bouquetFlowers) {
            this.bouquetFlowers = bouquetFlowers;
            bouquetCover = new ImageIcon("bouquetCover.jpeg").getImage();

            setBackground(Color.WHITE);
            setBorder(BorderFactory.createTitledBorder("Your Bouquet"));
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            int panelW = getWidth();
            int centerX = panelW / 2;

            int coverW = 280;
            int coverH = 350;
            int coverX = centerX - coverW / 2;
            int coverY = 135;

            int flowerSize = 175;

            int[][] spots = {
                {centerX - 95, coverY + 25},
                {centerX - 50, coverY + 5},
                {centerX - 10, coverY + 20},
                {centerX + 35, coverY + 10},
                {centerX - 75, coverY + 65},
                {centerX - 30, coverY + 55},
                {centerX + 10, coverY + 65}
            };
            double[] angles = {
                    -0.35, -0.18, -0.05, 0.20, -0.25, 0.08, 0.25
            };

         
            g2.drawImage(bouquetCover, coverX, coverY, coverW, coverH, this);

           
            for (int i = 0; i < bouquetFlowers.size() && i < spots.length; i++) {
                Flower f = bouquetFlowers.get(i);
                ImageIcon icon = new ImageIcon(f.bouquetImagePath);

                if (icon.getIconWidth() > 0) {
                    int x = spots[i][0];
                    int y = spots[i][1];

                    Graphics2D copy = (Graphics2D) g2.create();
                    copy.rotate(angles[i], x + flowerSize / 2, y + flowerSize / 2);
                    copy.drawImage(icon.getImage(), x, y, flowerSize, flowerSize, this);
                    copy.dispose();
                } else {
                    g2.setColor(Color.RED);
                    g2.drawString("Missing: " + f.bouquetImagePath, 20, 70 + i * 20);
                }
            }

           
            Shape oldClip = g2.getClip();
            g2.setClip(coverX, coverY + 205, coverW, coverH - 205);
            g2.drawImage(bouquetCover, coverX, coverY, coverW, coverH, this);
            g2.setClip(oldClip);

            g2.setFont(new Font("Arial", Font.BOLD, 18));
            g2.setColor(Color.BLACK);
            g2.drawString("Selected Flowers: " + bouquetFlowers.size(), 20, 35);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FlowerBouquetGame().setVisible(true));
    }
}
