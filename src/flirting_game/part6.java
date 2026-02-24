package flirting_game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class part6 extends JFrame {
    private JLayeredPane layeredPane;
    private JLabel backgroundLabel, characterLabel, dialogueArea, nameLabel;
    private RoundedPanel dialoguePanel;
    private int currentIndex = 0;
    private Timer typewriterTimer;
    private int charIndex = 0;
    private boolean isTyping = false;
    
    private final Font THAI_FONT = new Font("Tahoma", Font.PLAIN, 28);
    private final Font THAI_FONT_BOLD = new Font("Tahoma", Font.BOLD, 30);

    private String[] imagePaths = {
       "res/scene6/s1.png", "res/scene6/s1.png", "res/scene6/s1.png", "res/scene6/s1.png",
       "res/scene6/s1.png", "res/scene6/s1.png", "res/scene6/s1.png", "res/scene6/s1.png",
       "res/scene6/s1.png", "res/scene6/s1.png", "res/scene6/s2.png", "res/scene6/s2.png",
       "res/scene6/s2.png", "res/scene6/s2.png", "res/scene6/s2.png", "res/scene6/s2.png", 
       "res/scene6/s2.png", "res/scene6/s2.png", "res/scene6/s2.png", "res/scene6/s2.png", 
       "res/scene6/s2.png", "res/scene6/s2.png", "res/scene6/s2.png", "res/scene6/s2.png", 
       "res/scene6/s2.png", "res/scene6/s2.png", "res/scene6/s2.png", "res/scene6/s2.png", 
       "res/scene6/s2.png", "res/scene6/s2.png", "res/scene6/s2.png", "res/scene6/s2.png", 
       "res/scene6/s2.png", "res/scene6/s2.png", "res/scene6/s2.png", "res/scene6/s2.png",
       "res/scene6/s2.png", "res/scene6/s2.png", "res/scene6/s2.png", "res/scene6/s2.png", 
       "res/scene6/s2.png", "res/scene6/s2.png", "res/scene6/s2.png", "res/scene6/s2.png", 
    };
    
    private String[] charPaths = {
       "res/scene5/Alice-shower2.png", "res/scene5/Alice-shower2.png", "res/scene5/Alice-shower2.png", "res/Charactor/Dan/dan-showhand1.png",
       "res/Charactor/Dan/dan-showhand1.png", "res/scene5/Alice-shower3.png", "res/scene5/Alice-shower3.png", "res/scene5/Alice-shower3.png", 
       "res/scene5/Alice-shower1.png", "res/scene5/Alice-shower1.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png",
       "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-shy1.png", "res/Charactor/Alice/Girl/Alice-normal1.png", "res/Charactor/Dan/dan-normal1.png",
       "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png",
       "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png",
       "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png",
       "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png",
       "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png",
       "res/Charactor/Dan/dan-normal2.png","res/Charactor/Alice/Girl/Alice-normal1.png", "res/Charactor/Alice/Girl/Alice-normal2.png","res/Charactor/Alice/Girl/Alice-normal1.png", 
       "res/Charactor/Alice/Girl/Alice-shy1.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", 
       "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Alice/Girl/Alice-normal1.png", "res/empty.png"
    };
    
    private String[] names = { 
    		"" 
    		};
    
    private String[] dialogues = {
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
        "1", "2", "3", "4", "5", "6", "7", "8",
    };

    public part6() {
        setTitle("ISEKAI DEMO - Part 6: Hidden Shadow");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        // 1. พื้นหลัง
        backgroundLabel = new JLabel();
        backgroundLabel.setBounds(0, 0, 1280, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        // 2. ตัวละคร (พิกัดมาตรฐาน 190, 100, 900, 900)
        characterLabel = new JLabel();
        characterLabel.setBounds(190, 100, 900, 900); 
        layeredPane.add(characterLabel, JLayeredPane.PALETTE_LAYER);

        setupDialogueUI();
        updateScene();

        layeredPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isTyping) {
                    stopTypewriter();
                    dialogueArea.setText("<html><body style='width: 950px;'>" + dialogues[currentIndex] + "</body></html>");
                    return;
                }

                if (currentIndex < dialogues.length - 1) {
                    currentIndex++;
                    updateScene();
                } else {
                    UIManager.put("OptionPane.messageFont", THAI_FONT);
                    JOptionPane.showMessageDialog(null, "End Part 6!");
                    System.exit(0); 
                }
            }
        });
    }

    private void setupDialogueUI() {
        dialoguePanel = new RoundedPanel(50);
        dialoguePanel.setLayout(null);
        dialoguePanel.setBounds(90, 520, 1100, 220);
        dialoguePanel.setBackground(new Color(20, 20, 25, 215));
        layeredPane.add(dialoguePanel, JLayeredPane.MODAL_LAYER);

        nameLabel = new JLabel();
        nameLabel.setFont(THAI_FONT_BOLD);
        nameLabel.setForeground(new Color(255, 204, 0)); 
        nameLabel.setBounds(60, 25, 400, 45); 
        dialoguePanel.add(nameLabel);

        dialogueArea = new JLabel();
        dialogueArea.setFont(THAI_FONT);
        dialogueArea.setForeground(Color.WHITE);
        dialogueArea.setVerticalAlignment(SwingConstants.TOP);
        dialogueArea.setBounds(60, 85, 980, 110); 
        dialoguePanel.add(dialogueArea);
    }

    private void updateScene() {
        if (currentIndex < names.length) {
            nameLabel.setText(names[currentIndex]);
        } else {
            nameLabel.setText(""); // ป้องกัน Error ถ้าใส่ชื่อไม่ครบ
        }
        
        if (currentIndex < dialogues.length) startTypewriter(dialogues[currentIndex]);
        if (currentIndex < imagePaths.length) backgroundLabel.setIcon(scaleImage(imagePaths[currentIndex], 1280, 800));
        
        if (currentIndex < charPaths.length) {
            String path = charPaths[currentIndex];
            
            // ตรวจสอบว่าถ้าเป็นภาพ Dan ให้ปรับความกว้างเพิ่มขึ้นเพื่อไม่ให้ภาพดูบีบ
            if (path.contains("dan-normal2")) {
                characterLabel.setBounds(90, 100, 1100, 900); 
                characterLabel.setIcon(scaleImage(path, 1100, 900));
            } else if (path.contains("dan")) { 
                characterLabel.setBounds(140, 100, 1000, 900);
                characterLabel.setIcon(scaleImage(path, 1000, 900));
            } else {
                characterLabel.setBounds(190, 100, 900, 900);
                characterLabel.setIcon(scaleImage(path, 900, 900));
            }
        }
    }

    private void startTypewriter(String text) {
        stopTypewriter();
        isTyping = true;
        charIndex = 0;
        dialogueArea.setText("");
        typewriterTimer = new Timer(30, e -> {
            if (charIndex < text.length()) {
                charIndex++;
                dialogueArea.setText("<html><body style='width: 950px;'>" + text.substring(0, charIndex) + "</body></html>");
            } else {
                stopTypewriter();
            }
        });
        typewriterTimer.start();
    }

    private void stopTypewriter() {
        if (typewriterTimer != null) typewriterTimer.stop();
        isTyping = false;
    }

    public ImageIcon scaleImage(String path, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(path);
            return new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        } catch (Exception e) { return null; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new part6().setVisible(true));
    }
}

class RoundedPanel extends JPanel {
    private int cornerRadius;

    public RoundedPanel(int radius) {
        this.cornerRadius = radius;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
    }
}