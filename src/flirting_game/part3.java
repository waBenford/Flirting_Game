package flirting_game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class part3 extends JFrame {
    private JLayeredPane layeredPane;
    private JLabel backgroundLabel, characterLabel, dialogueArea, nameLabel;
    private RoundedPanel dialoguePanel;
    private int currentIndex = 0;
    
    private final Font THAI_FONT_PLAIN = new Font("Tahoma", Font.PLAIN, 24);
    private final Font THAI_FONT_BOLD = new Font("Tahoma", Font.BOLD, 24);

    private String[] imagePaths = {
    		"res/scene3/s1.png", "res/scene3/s1.png", "res/scene3/s1.png", "res/scene3/s1.png",
        	"res/scene3/s1.png", "res/scene3/s1.png", "res/scene3/s1.png", "res/scene3/s1.png",
        	"res/scene3/s1.png", "res/scene3/s1.png", "res/scene3/s1.png", "res/scene3/s1.png",
        	"res/scene3/s1.png", "res/scene3/s1.png", "res/scene3/s1.png", "res/scene3/s1.png",
        	"res/scene3/s1.png", "res/scene3/s1.png", "res/scene3/s1.png", "res/scene3/s1.png",
        	"res/scene3/s2.png", "res/scene3/s2.png", "res/scene3/s2.png", "res/scene3/s3.png",
        	"res/scene3/s3.png", "res/scene3/s3.png", "res/scene3/s3.png", "res/scene3/s4.png", 
        	"res/scene3/s4.png", "res/scene3/s4.png", "res/scene3/s5.png", "res/scene3/s5.png",
        	"res/scene3/s5.png", "res/scene3/s5.png", "res/scene3/s5.png", "res/scene3/s5.png",
        	"res/scene3/s5.png", "res/scene3/s5.png", "res/scene3/s5.png", "res/scene3/s5.png",
        	"res/scene3/s5.png", "res/scene3/s5.png", "res/scene3/s5.png", "res/scene3/s5.png",
    };
    
    private String[] charPaths = {
    	"res/empty.png" 
    };
    
    private String[] names = { 
    	""
    };
    
    private String[] dialogues = {
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
        "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
        "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
        "31", "32", "33", "34", "35", "36", "37", "38", "39", "40",
        "41", "42", "43", "44"
    };

    public part3() {
        setTitle("ISEKAI DEMO - Part 3");
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
                // ตรวจสอบว่ายังมีข้อความต่อไปหรือไม่
                if (currentIndex < dialogues.length - 1) {
                    currentIndex++;
                    updateScene();
                } else {
                    UIManager.put("OptionPane.messageFont", THAI_FONT_PLAIN);
                    JOptionPane.showMessageDialog(null, "จบการสาธิตช่วงที่ 3 ขอบคุณค่ะ!");
                    System.exit(0); 
                }
            }
        });
    }

    private void setupDialogueUI() {
        // 1. กล่องข้อความหลัก (ตามที่คุณส่งมา)
        dialoguePanel = new RoundedPanel(40);
        dialoguePanel.setLayout(null);
        dialoguePanel.setBounds(50, 550, 900, 180); 
        dialoguePanel.setBackground(new Color(20, 20, 25, 215));
        layeredPane.add(dialoguePanel, JLayeredPane.MODAL_LAYER);

        // 2. ชื่อตัวละคร
        nameLabel = new JLabel(names[0]);
        nameLabel.setFont(THAI_FONT_BOLD);
        nameLabel.setForeground(new Color(255, 204, 0)); 
        nameLabel.setBounds(60, 20, 300, 40); 
        dialoguePanel.add(nameLabel);

        // 3. บทสนทนา
        dialogueArea = new JLabel();
        dialogueArea.setFont(THAI_FONT_PLAIN);
        dialogueArea.setForeground(Color.WHITE);
        dialogueArea.setVerticalAlignment(SwingConstants.TOP);
        dialogueArea.setBounds(60, 75, 800, 100); 
        dialoguePanel.add(dialogueArea);
        
        // แสดงข้อความแรกทันที
        updateDialogueDisplay(dialogues[0]);
    }

    private void updateScene() {
        // ป้องกัน Error กรณี Array มีขนาดไม่เท่ากัน
        if (currentIndex < names.length) {
            nameLabel.setText(names[currentIndex]);
        } else {
            nameLabel.setText(names[names.length - 1]); // ใช้ชื่อสุดท้ายถ้าข้อมูลหมด
        }
        
        if (currentIndex < dialogues.length) {
            updateDialogueDisplay(dialogues[currentIndex]);
        }

        // อัปเดตภาพพื้นหลัง (วนลูปใช้ภาพที่มี)
        int bgIdx = currentIndex % imagePaths.length;
        backgroundLabel.setIcon(scaleImage(imagePaths[bgIdx], 1000, 800));
    }

    private void updateDialogueDisplay(String text) {
        dialogueArea.setText("<html><body style='width: 750px;'>" + text + "</body></html>");
    }

    public ImageIcon scaleImage(String path, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage();
            return new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new part3().setVisible(true));
    }
}