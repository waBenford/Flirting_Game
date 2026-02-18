package flirting_game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class part2 extends JFrame {
    private JLayeredPane layeredPane;
    private JLabel backgroundLabel, characterLabel, dialogueArea, nameLabel;
    private RoundedPanel dialoguePanel;
    private int currentIndex = 0;

    private String[] imagePaths = {
            "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s1.png",
            "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s1.png",
            "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s2.png", "res/scene2/s2.png",
            "res/scene2/s2.png", "res/scene2/s2.png", "res/scene2/s2.png", "res/scene2/s3.png",
            "res/scene2/s4.png", "res/scene2/s4.png", "res/scene2/s5.png", "res/scene2/s5.png",
            "res/scene2/s5.png", "res/scene2/s5.png", "res/scene2/s5.png", "res/scene2/s5.png",
            "res/scene2/s5.png", "res/scene2/s5.png", "res/scene2/s5.png", "res/scene2/s5.png",
            "res/scene2/s5.png", "res/scene2/s5.png"
    };
    
    private String[] charPaths = {
            "res/scene2/s1g.png", "res/scene2/s1g.png", "res/scene2/s1sitdown.png",  
            "res/scene2/s1sitdown.png", "res/scene2/s1sitdown.png", "res/scene2/s1sitdown.png",
            "res/scene2/s1sitdown.png", "res/scene2/s1sitdown.png", "res/scene2/s1sitdown.png", 
            "res/scene2/s1sitdown.png", "res/scene2/s1sitdown.png", "res/empty.png",
            "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png","res/empty.png",
            "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
            "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
            "res/empty.png", "res/empty.png", "res/empty.png"
    };
    
    // ปรับให้มีจำนวนเท่ากับบทสนทนาเพื่อกัน Error
    private String[] names = new String[30]; 
    
    private String[] dialogues = {
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10.",
        "11", "12", "13", "14", "15", "16", "17", "18", "19", "20.",
        "21", "22", "23", "24", "25", "26", "27", "28", "29", "30.",
    };

    public part2() {
        // กำหนดชื่อเริ่มต้น
        java.util.Arrays.fill(names, "เด็กผู้หญิง");

        setTitle("ISEKAI DEMO - Part 2: First Encounter");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        backgroundLabel = new JLabel(scaleImage(imagePaths[0], 1000, 800));
        backgroundLabel.setBounds(0, 0, 1000, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        characterLabel = new JLabel(scaleImage(charPaths[0], 1000, 800));
        characterLabel.setBounds(0, 0, 1000, 800);
        layeredPane.add(characterLabel, JLayeredPane.PALETTE_LAYER);

        setupDialogueUI();

        layeredPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                currentIndex++;
                if (currentIndex < dialogues.length) {
                    updateScene();
                } else {
                    JOptionPane.showMessageDialog(null, "จบการทดสอบ Part 2");
                    System.exit(0);
                }
            }
        });
    }

    private void setupDialogueUI() {
        dialoguePanel = new RoundedPanel(30);
        dialoguePanel.setLayout(null);
        dialoguePanel.setBounds(50, 580, 900, 160);
        dialoguePanel.setBackground(new Color(20, 20, 25, 215));
        layeredPane.add(dialoguePanel, JLayeredPane.MODAL_LAYER);

        TopLeftRoundedPanel nameBox = new TopLeftRoundedPanel(25);
        nameBox.setLayout(null);
        nameBox.setBackground(new Color(255, 204, 0));
        nameBox.setBounds(0, -35, 160, 35); // ปรับตำแหน่งให้ลอยเหนือกล่องข้อความ
        dialoguePanel.add(nameBox);

        nameLabel = new JLabel(names[0], SwingConstants.CENTER);
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        nameLabel.setForeground(Color.BLACK);
        nameLabel.setBounds(0, 0, 160, 35);
        nameBox.add(nameLabel);

        dialogueArea = new JLabel("<html>" + dialogues[0] + "</html>");
        dialogueArea.setFont(new Font("Tahoma", Font.PLAIN, 22));
        dialogueArea.setForeground(Color.WHITE);
        dialogueArea.setVerticalAlignment(SwingConstants.TOP);
        dialogueArea.setBounds(25, 30, 850, 110);
        dialoguePanel.add(dialogueArea);
    }

    private void updateScene() {
        if (currentIndex < names.length) nameLabel.setText(names[currentIndex]);
        if (currentIndex < dialogues.length) dialogueArea.setText("<html>" + dialogues[currentIndex] + "</html>");
        if (currentIndex < imagePaths.length) backgroundLabel.setIcon(scaleImage(imagePaths[currentIndex], 1000, 800));
        if (currentIndex < charPaths.length) characterLabel.setIcon(scaleImage(charPaths[currentIndex], 1000, 800));
    }

    public ImageIcon scaleImage(String path, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage();
            return new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            return null; // ป้องกันค้างถ้าหาไฟล์ภาพไม่เจอ
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new part2().setVisible(true));
    }
}

// --- เพิ่มคลาสที่ขาดหายไปเพื่อให้ Error หาย ---
class RoundedPanel extends JPanel {
    private int cornerRadius;
    public RoundedPanel(int radius) {
        this.cornerRadius = radius;
        setOpaque(false); 
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
    }
}

class TopLeftRoundedPanel extends JPanel {
    private int cornerRadius;
    public TopLeftRoundedPanel(int radius) {
        this.cornerRadius = radius;
        setOpaque(false);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        int w = getWidth();
        int h = getHeight();
        java.awt.geom.Area area = new java.awt.geom.Area(new java.awt.geom.RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));
        area.add(new java.awt.geom.Area(new java.awt.Rectangle(cornerRadius, 0, w - cornerRadius, h)));
        area.add(new java.awt.geom.Area(new java.awt.Rectangle(0, cornerRadius, w, h - cornerRadius)));
        g2.fill(area);
    }
}