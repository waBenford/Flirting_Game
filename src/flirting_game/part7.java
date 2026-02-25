package flirting_game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class part7 extends JFrame {
    private JLayeredPane layeredPane;
    private JLabel backgroundLabel, characterLabel, dialogueArea, nameLabel;
    private VisualNovelBox dialoguePanel; 
    private int currentIndex = 0;
    private Timer typewriterTimer;
    private int charIndex = 0;
    private boolean isTyping = false;
    private Map<String, ImageIcon> imageCache = new HashMap<>();
    
    private final Font THAI_FONT_PLAIN = new Font("Tahoma", Font.PLAIN, 28);
    private final Font THAI_FONT_BOLD = new Font("Tahoma", Font.BOLD, 30);

    // --- ข้อมูล Array (คงเดิม) ---
    private String[] imagePaths = {
       "res/scene7/s1.png", "res/scene7/s1.png", "res/scene7/s1.png", "res/scene7/s1.png", 
       "res/scene7/s1.png", "res/scene7/s1.png", "res/scene7/s1.png", "res/scene7/s1.png", 
       "res/scene7/s1.png", "res/scene7/s2.png", "res/scene7/s2.png", "res/scene7/s2.png", 
       "res/scene7/s2.png", "res/scene7/s2.png", "res/scene7/s3.png", "res/scene7/s3.png", 
       "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", 
       "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", 
       "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", 
       "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", 
       "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", 
       "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", 
       "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", 
       "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", 
       "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", 
       "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", 
       "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", 
       "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", 
       "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", 
       "res/scene7/s3.png", "res/scene7/s3.png"
    };
    
    private String[] charPaths = { 
       "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", 
       "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/scene5/Alice-normal1.png", "res/Charactor/Alice/Girl/Alice-shy1.png",
       "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/scene5/Alice-normal1.png",
       "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/scene5/Alice-normal1.png",
       "res/scene5/Alice-normal1.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
       "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/Charactor/Alice/Girl/Alice-fight1.png",
       "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
       "res/scene5/Alice-normal1.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal1.png",
       "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
       "res/empty.png", "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal2.png", "res/scene5/Alice-normal1.png",
       "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
       "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
       "res/scene5/Alice-normal2.png", "res/Charactor/Dan/dan-normal2.png"
    };

    private String[] names = { "" };
    private String[] dialogues = {
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
        "1", "2", "3", "4", "5", "6", "7", "8",
    };

    public part7() {
        setTitle("ISEKAI DEMO - Part 7");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        backgroundLabel = new JLabel();
        backgroundLabel.setBounds(0, 0, 1280, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        // ตัวละครจะถูกตั้งค่า Bounds ใหม่ใน updateScene
        characterLabel = new JLabel();
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
                    JOptionPane.showMessageDialog(null, "End Part 7!");
                    System.exit(0); 
                }
            }
        });
    }

    private void updateScene() {
        if (currentIndex < names.length) nameLabel.setText(names[currentIndex]);
        if (currentIndex < dialogues.length) startTypewriter(dialogues[currentIndex]);
        if (currentIndex < imagePaths.length) backgroundLabel.setIcon(getOptimizedImage(imagePaths[currentIndex], 1280, 800));
        
        if (currentIndex < charPaths.length) {
            String path = charPaths[currentIndex];
            if (path.contains("empty.png")) {
                characterLabel.setIcon(null);
            } else {
                // --- ระบบจัดตำแหน่งและขนาดให้สมส่วนตามชื่อไฟล์ ---
                int charW, charH, charX, charY;

                if (path.contains("dan")) {
                    // สำหรับ Dan: ไหล่กว้าง ใช้สัดส่วนกว้างกว่าอริสเล็กน้อย
                    charW = 1200;
                    charH = 1000;
                    charX = (1280 - charW) / 2; // คำนวณให้อยู่กึ่งกลางจอ 1280
                    charY = 50; 
                } if (path.contains("dan-normal2")) {
                	charW = 1000;
                    charH = 800;
                    charX = (1280 - charW) / 2; // คำนวณให้อยู่กึ่งกลางจอ 1280
                    charY = 70; 
                } else {
                    // สำหรับ อริส: สัดส่วนเพรียวแนวตั้ง
                    charW = 1000;
                    charH = 900;
                    charX = (1280 - charW) / 2; // คำนวณให้อยู่กึ่งกลางจอ 1280
                    charY = 50;
                }

                characterLabel.setBounds(charX, charY, charW, charH);
                characterLabel.setIcon(getOptimizedImage(path, charW, charH));
            }
        }
    }

    private void setupDialogueUI() {
        dialoguePanel = new VisualNovelBox();
        dialoguePanel.setLayout(null);
        dialoguePanel.setBounds(90, 520, 1100, 220); 
        layeredPane.add(dialoguePanel, JLayeredPane.MODAL_LAYER);

        nameLabel = new JLabel();
        nameLabel.setFont(THAI_FONT_BOLD);
        nameLabel.setForeground(new Color(180, 40, 90)); 
        nameLabel.setBounds(60, 25, 400, 45); 
        dialoguePanel.add(nameLabel);

        dialogueArea = new JLabel();
        dialogueArea.setFont(THAI_FONT_PLAIN);
        dialogueArea.setForeground(new Color(45, 65, 115));
        dialogueArea.setVerticalAlignment(SwingConstants.TOP);
        dialogueArea.setBounds(60, 85, 980, 110); 
        dialoguePanel.add(dialogueArea);
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
            } else { stopTypewriter(); }
        });
        typewriterTimer.start();
    }

    private void stopTypewriter() {
        if (typewriterTimer != null) typewriterTimer.stop();
        isTyping = false;
    }

    private ImageIcon getOptimizedImage(String path, int w, int h) {
        String key = path + w + h;
        if (!imageCache.containsKey(key)) {
            try {
                ImageIcon icon = new ImageIcon(path);
                Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
                imageCache.put(key, new ImageIcon(img));
            } catch (Exception e) { return null; }
        }
        return imageCache.get(key);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new part7().setVisible(true));
    }
}

// --- VisualNovelBox Class (คงเดิม) ---
class VisualNovelBox extends JPanel {
    private int cornerRadius = 30;
    public VisualNovelBox() { setOpaque(false); }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gradient = new GradientPaint(0, 0, new Color(245, 250, 255, 180), 0, getHeight(), new Color(255, 235, 245, 230));
        g2d.setPaint(gradient);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2d.setColor(new Color(255, 150, 200, 200));
        g2d.setStroke(new BasicStroke(4f));
        g2d.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, cornerRadius, cornerRadius);
    }
}