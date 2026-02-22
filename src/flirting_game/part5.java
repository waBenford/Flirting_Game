package flirting_game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class part5 extends JFrame {
    private JLayeredPane layeredPane;
    private JLabel backgroundLabel, characterLabel, dialogueArea, nameLabel;
    private RoundedPanel dialoguePanel;
    private int currentIndex = 0;
    
    // --- ฟอนต์ภาษาไทยมาตรฐาน ---
    private final Font THAI_FONT = new Font("Tahoma", Font.PLAIN, 24);
    private final Font THAI_FONT_BOLD = new Font("Tahoma", Font.BOLD, 24);

    private String[] imagePaths = {
       "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png",
       "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", 
       "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", 
       "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s2.png", 
       "res/scene5/s2.png", "res/scene5/s2.png", "res/scene5/s2.png", "res/scene5/s2.png", 
       "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", 
       "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", 
       "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", 
       "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", 
       "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s4.png", "res/scene5/s4.png", 
       "res/scene5/s4.png", "res/scene5/s4.png", "res/scene5/s4.png", "res/scene5/s3.png", 
       "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png",
       "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s5.png",
       "res/scene5/s5.png", "res/scene5/s5.png", 
    };

    private String[] charPaths = {
       "res/empty.png", "res/scene5/Alice-normal1.png", "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", 
       "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", 
       "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", "res/scene5/Alice-normal2.png", "res/scene5/Alice-shy1.png", 
       "res/scene5/Alice-shy2.png", "res/scene5/Alice-normal1.png", "res/scene5/Alice-normal2.png", "res/empty.png",
       "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
       "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/Alice-normal1.png", 
       "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", 
       "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/Alice-shy1.png",
       "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/Alice-normal1.png", "res/scene5/Alice-normal1.png",
       "res/scene5/Alice-normal2.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", 
       "res/scene5/demogigi1.png", "res/scene5/demogigi2.png", "res/empty.png", "res/scene5/Alice-normal1.png", 
       "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", 
       "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", "res/scene5/Alice-shower1.png", "res/scene5/Alice-shower1.png",
       "res/scene5/Alice-shower2.png", 
       
       
    };
    
    private String[] names = { "อริส" };
    
    private String[] dialogues = {
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
        "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
        "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
        "31", "32", "33", "34", "35", "36", "37", "38", "39", "40",
        "41", "42", "43", "44", "45", "46", "47", "48", "49", "50",
        "51", "52", "53", "54"
    };

    public part5() {
        setTitle("ISEKAI DEMO - Part 5: The Journey Begins");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        // 1. พื้นหลัง (Layer ล่างสุด)
        backgroundLabel = new JLabel(scaleImage(imagePaths[0], 1000, 800));
        backgroundLabel.setBounds(0, 0, 1000, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        // 2. เพิ่มตัวละครตรงกลาง (Layer กลาง)
        // ขนาด 600x750 วางที่ x=200 เพื่อให้อยู่ตรงกลางจอพอดี
        characterLabel = new JLabel(scaleImage(charPaths[0], 600, 800));
        characterLabel.setBounds(200, 50, 600, 800);
        layeredPane.add(characterLabel, JLayeredPane.PALETTE_LAYER);

        setupDialogueUI();
        updateScene();

        layeredPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentIndex < dialogues.length - 1) {
                    currentIndex++;
                    updateScene();
                } else {
                    UIManager.put("OptionPane.messageFont", THAI_FONT);
                    JOptionPane.showMessageDialog(null, "End Part5");
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
        // ป้องกัน Error กรณีลืมใส่ชื่อหรือรูปให้ครบเท่า dialogues
        if (currentIndex < names.length) {
            nameLabel.setText(names[currentIndex]);
        } else {
            nameLabel.setText(names[names.length - 1]);
        }

        if (currentIndex < dialogues.length) {
            dialogueArea.setText("<html><body style='width: 750px;'>" + dialogues[currentIndex] + "</body></html>");
        }

        if (currentIndex < imagePaths.length) {
            backgroundLabel.setIcon(scaleImage(imagePaths[currentIndex], 1000, 800));
        }

        if (currentIndex < charPaths.length) {
            characterLabel.setIcon(scaleImage(charPaths[currentIndex], 600, 750));
        }
    }

    public ImageIcon scaleImage(String path, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(path);
            return new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        } catch (Exception e) { return null; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new part5().setVisible(true));
    }
}