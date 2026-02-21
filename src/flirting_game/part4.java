package flirting_game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class part4 extends JFrame {
    private JLayeredPane layeredPane;
    private JLabel backgroundLabel, characterLabel, dialogueArea, nameLabel;
    private RoundedPanel dialoguePanel;
    private int currentIndex = 0;
    
    private final Font THAI_FONT = new Font("Tahoma", Font.PLAIN, 24);
    private final Font THAI_FONT_BOLD = new Font("Tahoma", Font.BOLD, 24);

    private String[] imagePaths = {
       "res/scene4/s1.png", "res/scene4/s2.png", "res/scene4/s2.png", "res/scene4/s2.png",
       "res/scene4/s2.png", "res/scene4/s2.png", "res/scene4/s2.png", "res/scene4/s2.png",
       "res/scene4/s2.png", "res/scene4/s2.png", "res/scene4/s2.png", "res/scene4/s2.png",
       "res/scene4/s3.png", "res/scene4/s3.png", "res/scene4/s3.png", "res/scene4/s3.png",
       "res/scene4/s3.png", "res/scene4/s3.png", "res/scene4/s4.1.png","res/scene4/s4.png",
       "res/scene4/s4.png", "res/scene4/s4.png", "res/scene4/s4.png", "res/scene4/s4.png",
       "res/scene4/s4.png", "res/scene4/s4.png", "res/scene4/s5.png", "res/scene4/s5.png",
       "res/scene4/s5.png", "res/scene4/s5.png", "res/scene4/s5.png", "res/scene4/s5.png",
       "res/scene4/s5.png", "res/scene4/s5.png", "res/scene4/s5.png", "res/scene4/s5.png",
       "res/scene4/s5.png", "res/scene4/s6.png", "res/scene4/s6.png", "res/scene4/s6.png",
       "res/scene4/s6.png", "res/scene4/s6.png", "res/scene4/s6.png", "res/scene4/s6.png",
       "res/scene4/s6.png", "res/scene4/s6.png", "res/scene4/s6.png", "res/scene4/s7.png",
       "res/scene4/s7.png", "res/scene4/s7.png", "res/scene4/s7.png", "res/scene4/s7.png",
       "res/scene4/s7.png", "res/scene4/s7.png", "res/scene4/s3.png", "res/scene4/s3.png",
       "res/scene4/s3.png", "res/scene4/s3.png", "res/scene4/s3.png", "res/scene4/s3.png",
       "res/scene4/s3.png", "res/scene4/s3.png", "res/scene4/s3.png"
    };
    
    private String[] charPaths = {
       "res/empty.png", "res/scene4/body1.png", "res/scene4/body2.png", "res/scene4/alice1.png",
       "res/scene4/body1.png", "res/scene4/alice1.png", "res/scene4/body2.png", "res/scene4/alice3.png",
       "res/scene4/alice4.png", "res/scene4/alice2.png", "res/scene4/alice1.png", "res/scene4/alice2.png", 
       "res/scene4/alice1.png", "res/scene4/alice2.png", "res/scene4/alice1.png", "res/scene4/alice2.png", 
       "res/scene4/alice1.png", "res/scene4/alice2.png", "res/scene4/empty","res/scene4/lung.png", "res/scene4/alice1.png", 
       "res/scene4/lung.png",  "res/scene4/lung.png",  "res/scene4/alice1.png",  "res/scene4/alice2.png",
       "res/scene4/alice1.png", "res/scene4/gigi.png", "res/scene4/gigi.png", "res/scene4/gigi.png", 
       "res/scene4/gigi.png", "res/scene4/gigi.png", "res/scene4/gigi.png", "res/scene4/gigi.png", 
       "res/scene4/alice1.png", "res/scene4/alice2.png", "res/scene4/gigi.png", "res/scene4/alice1.png",
       "res/scene4/aliceatk1.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/scene4/aliceatk2.png",
       "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
       "res/scene4/gigi2.png", "res/scene4/gigi2.png", "res/scene4/gigi2.png", "res/scene4/gigi2.png", 
       "res/scene4/gigi2.png", "res/scene4/gigi2.png", "res/scene4/gigi2.png", "res/scene4/alice1.png",
       "res/scene4/alice2.png", "res/scene4/alice1.png", "res/scene4/alice2.png", "res/scene4/alice1.png",
       "res/scene4/alice2.png", "res/scene4/alice1.png", "res/scene4/alice3.png", "res/scene4/alice1.png"
    };
    
    private String[] names = {
    		"" 
    		};
    
    private String[] dialogues = {
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
        "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
        "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
        "31", "32", "33", "34", "35", "36", "37", "38", "39", "40",
        "41", "42", "43", "44", "45", "46", "47", "48", "49", "50",
        "51", "52", "53", "54", "55", "56", "57", "58", "59", "60",
        "61", "62", "63"
    };

    public part4() {
        setTitle("ISEKAI DEMO - Part 4");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        backgroundLabel = new JLabel(scaleImage(imagePaths[0], 1000, 800));
        backgroundLabel.setBounds(0, 0, 1000, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        characterLabel = new JLabel();
        characterLabel.setBounds(0, 0, 1000, 800); // ตั้งค่าเริ่มต้น
        layeredPane.add(characterLabel, JLayeredPane.PALETTE_LAYER);

        setupDialogueUI();
        updateScene(); // เรียกครั้งแรกเพื่อตั้งค่าภาพตัวละคร

        layeredPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentIndex < dialogues.length - 1) {
                    currentIndex++;
                    updateScene();
                } else {
                    JOptionPane.showMessageDialog(null, "End Part 4!");
                    System.exit(0);
                }
            }
        });
    }

    private void setupDialogueUI() {
        dialoguePanel = new RoundedPanel(40);
        dialoguePanel.setLayout(null);
        dialoguePanel.setBounds(50, 550, 900, 180);
        dialoguePanel.setBackground(new Color(20, 20, 25, 215));
        layeredPane.add(dialoguePanel, JLayeredPane.MODAL_LAYER);

        nameLabel = new JLabel(names[0]);
        nameLabel.setFont(THAI_FONT_BOLD);
        nameLabel.setForeground(new Color(255, 204, 0));
        nameLabel.setBounds(60, 20, 300, 40);
        dialoguePanel.add(nameLabel);

        dialogueArea = new JLabel();
        dialogueArea.setFont(THAI_FONT);
        dialogueArea.setForeground(Color.WHITE);
        dialogueArea.setVerticalAlignment(SwingConstants.TOP);
        dialogueArea.setBounds(60, 75, 800, 100);
        dialoguePanel.add(dialogueArea);
    }

    private void updateScene() {
        if (currentIndex < names.length) nameLabel.setText(names[currentIndex]);
        if (currentIndex < dialogues.length) dialogueArea.setText("<html><body style='width: 750px;'>" + dialogues[currentIndex] + "</body></html>");
        if (currentIndex < imagePaths.length) backgroundLabel.setIcon(scaleImage(imagePaths[currentIndex], 1000, 800));
        
        // --- ส่วนที่แก้ไข: เช็คว่าเป็นรูป body หรือไม่ ---
        if (currentIndex < charPaths.length) {
            String path = charPaths[currentIndex];
            if (path.contains("body")) {
                // ถ้าเป็นรูป body ให้ปรับขนาดเล็กลง (เช่น 500x700) และวางตำแหน่งกึ่งกลาง
                characterLabel.setIcon(scaleImage(path, 500, 700));
                characterLabel.setBounds(250, 50, 500, 700); 
            } else if (path.contains("lung")) {
                // ถ้าเป็นรูป Lung ให้ปรับขนาดเล็กลงและวางตำแหน่งกึ่งกลาง
                characterLabel.setIcon(scaleImage(path, 800, 800));
                characterLabel.setBounds(200, 50, 800, 800);
                
            } else {
                // ถ้าเป็นรูปตัวละครอื่น (Alice, Gigi ฯลฯ) ให้ใช้ขนาดปกติ
                characterLabel.setIcon(scaleImage(path, 800, 800));
                characterLabel.setBounds(100, 0, 800, 800);
            }
        }
    }

    public ImageIcon scaleImage(String path, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage();
            return new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH));
        } catch (Exception e) { return null; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new part4().setVisible(true));
    }
}