import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class FlowerBouquetGame extends JFrame {
    private JPanel leftPanel, rightPanel;
    private JLabel gardenLabel, infoLabel;
    private JButton addButton;
    private DefaultListModel<String> bouquetModel;
    private JList<String> bouquetList;
    
    private ArrayList<Flower> flowers = new ArrayList<>();
    private Flower selectedFlower = null;

    public FlowerBouquetGame() {
        setTitle("Wildflower Bouquet Builder");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        loadFlowerData();

        // --- LEFT PANEL: Garden and Info ---
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(500, 700));

        // Garden Image
        ImageIcon gardenIcon = new ImageIcon("garden.png");
        // Scale image if it exists
        if (gardenIcon.getIconWidth() > 0) {
            Image img = gardenIcon.getImage().getScaledInstance(500, 400, Image.SCALE_SMOOTH);
            gardenLabel = new JLabel(new ImageIcon(img));
        } else {
            gardenLabel = new JLabel("[ Garden Drawing Not Found ]");
            gardenLabel.setPreferredSize(new Dimension(500, 400));
            gardenLabel.setOpaque(true);
            gardenLabel.setBackground(Color.LIGHT_GRAY);
        }
        gardenLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(gardenLabel);

        // Information Text
        infoLabel = new JLabel("<html><body style='width: 300px; text-align: center;'>" +
                "Select a flower to learn more!</body></html>");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        leftPanel.add(infoLabel);

        // Add Button
        addButton = new JButton("Add to Bouquet");
        addButton.setEnabled(false);
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.setBackground(new Color(255, 215, 0));
        addButton.addActionListener(e -> addToBouquet());
        leftPanel.add(addButton);

        add(leftPanel, BorderLayout.WEST);

        // --- RIGHT PANEL: Selection Buttons ---
        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));

        JLabel title = new JLabel("Pick Your Flowers:");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        rightPanel.add(title);

        for (Flower f : flowers) {
            JButton btn = new JButton(f.name);
            btn.setMaximumSize(new Dimension(200, 30));
            btn.addActionListener(e -> displayFlower(f));
            rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            rightPanel.add(btn);
        }

        // Bouquet List
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        rightPanel.add(new JLabel("Your Bouquet:"));
        bouquetModel = new DefaultListModel<>();
        bouquetList = new JList<>(bouquetModel);
        rightPanel.add(new JScrollPane(bouquetList));

        add(rightPanel, BorderLayout.CENTER);
    }

    private void loadFlowerData() {
        try {
            File file = new File("flower.txt");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String name = scanner.nextLine().trim();
                if (name.isEmpty()) continue;
                String desc = scanner.nextLine().trim();
                String imgPath = scanner.nextLine().trim();
                flowers.add(new Flower(name, desc, imgPath));
            }
            scanner.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading flower.txt");
        }
    }

    private void displayFlower(Flower f) {
        selectedFlower = f;
        infoLabel.setText("<html><div style='text-align: center;'><b>" + f.name + "</b><br><br>" + f.description + "</div></html>");
        addButton.setEnabled(true);
    }

    private void addToBouquet() {
        if (selectedFlower != null) {
            bouquetModel.addElement(selectedFlower.name);
        }
    }

    // Helper class to store flower info
    static class Flower {
        String name, description, imagePath;
        Flower(String n, String d, String i) {
            this.name = n;
            this.description = d;
            this.imagePath = i;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FlowerBouquetGame().setVisible(true);
        });
    }
}