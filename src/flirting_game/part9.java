package flirting_game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class part9 extends JFrame {
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

    // --- แก้ไขจุดนี้: เรียกใช้ฟังก์ชันเพื่อสร้าง Array พื้นหลัง 105 ฉาก ---
    private String[] imagePaths = createBackgrounds();
    
    // ฟังก์ชันกำหนดเงื่อนไขการเปลี่ยน Background
    private String[] createBackgrounds() {
        String[] paths = new String[188];
        for (int i = 0; i < 188; i++) {
            if (i < 10) paths[i] = "res/scene9/s1.png";
            else if (i < 65) paths[i] = "res/scene9/s2.png";
            else if (i < 90) paths[i] = "res/scene9/s3.png";
            else if (i < 96) paths[i] = "res/scene9/s4.png";
            else paths[i] = "res/scene9/s5.png"; // ฉากที่ 105 (ดัชนี 104)
        }
        return paths;
    }
    
    // ส่วนของตัวละคร (คุณชมพู่สามารถเพิ่ม Path เพิ่มเติมใน Array นี้ได้)
    private String[] charPaths = { 
       "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Alice/Girl/Alice-normal1.png",
       "res/empty.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-normal2.png",
       "res/Charactor/Alice/Girl/Alice-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-normal2.png",
       "res/Charactor/Alice/Girl/Alice-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-normal1.png",
       "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-normal1.png", 
       "res/Charactor/Alice/Girl/Alice-fight1.png", "res/Charactor/Alice/Girl/Alice-fight2.png", "res/Charactor/Dan/dan-normal2.png", "res/empty.png",
       "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-fight2.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal2.png",
       "res/Charactor/Alice/Girl/Alice-fight2.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal2.png", 
       "res/Charactor/Alice/Girl/Alice-normal2.png", "res/empty.png", "res/Charactor/Dan/dan-normal1.png",  "res/empty.png",  "res/empty.png",
       "res/Charactor/Dan/dan-normal1.png", "res/empty.png", "res/Charactor/Alice/Girl/Alice-normal1.png", "res/empty.png", "res/empty.png",
       "res/empty.png", "res/Charactor/Dan/dan-normal1.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal1.png",
       "res/Charactor/Dan/dan-normal2.png", "res/empty.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-normal1.png", 
       "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png",
       "res/Charactor/Alice/Girl/Alice-normal1.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal1.png",
       "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-normal1.png", 
       "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
       "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
       "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
       "res/Charactor/Alice/Girl/Alice-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/empty.png", "res/empty.png", "res/empty.png", 
       "res/Charactor/Alice/Girl/Alice-fight1.png", "res/Charactor/Alice/Girl/Alice-fight2.png", "res/empty.png", "res/empty.png",
       "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", 
       "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", 
       "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", 
       "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", 
       "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Alice/Girl/Alice-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/empty.png",
       "res/empty.png", "res/empty.png", "res/empty.png",  "res/empty.png",  "res/empty.png",  "res/empty.png",
       "res/Charactor/Dan/dan-normal1.png", "res/empty.png", "res/Charactor/Alice/Girl/Alice-normal1.png",  "res/empty.png", 
       "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", 
       "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", 
       "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png",  "res/Charactor/Alice/Girl/Alice-cry1.png", "res/empty.png",
       "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
       "res/empty.png", "res/empty.png", "res/empty.png", "res/Charactor/Alice/Girl/Alice-fight1.png",
       "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
       "res/empty.png", "res/empty.png", "res/Charactor/Alice/Girl/Alice-normal1.png", "res/empty.png",
       "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/Charactor/Alice/Girl/Alice-cry2.png",
       "res/empty.png", "res/empty.png",
    };

    private String[] names = { "" };
    
    // บทพูด 1-60 (สามารถเพิ่มให้ครบ 105 ได้ในลักษณะเดียวกัน)
    private String[] dialogues = {
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
        "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
        "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
        "31", "32", "33", "34", "35", "36", "37", "38", "39", "40",
        "41", "42", "43", "44", "45", "46", "47", "48", "49", "50",
        "51", "52", "53", "54", "55", "56", "57", "58", "59", "60",
        "51", "52", "53", "54", "55", "56", "57", "58", "59", "60",
        "51", "52", "53", "54", "55", "56", "57", "58", "59", "60",
        "51", "52", "53", "54", "55", "56", "57", "58", "59", "60",
        "51", "52", "53", "54", "55", "56", "57", "58", "59", "60",
        "51", "52", "53", "54", "55", "56", "57", "58", "59", "60"
    };

    public part9() {
        setTitle("ISEKAI  - Part 9: Path to Darkness");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        backgroundLabel = new JLabel();
        backgroundLabel.setBounds(0, 0, 1280, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

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
                    JOptionPane.showMessageDialog(null, "จบการสาธิต Part 9! เตรียมเข้าสู่ฉากจบเร็วๆ นี้");
                    System.exit(0); 
                }
            }
        });
    }

    private void updateScene() {
        // เช็ค Index ป้องกัน Error
        if (currentIndex < names.length) nameLabel.setText(names[currentIndex]);
        else nameLabel.setText("");

        if (currentIndex < dialogues.length) startTypewriter(dialogues[currentIndex]);
        
        if (currentIndex < imagePaths.length) {
            backgroundLabel.setIcon(getOptimizedImage(imagePaths[currentIndex], 1280, 800));
        }
        
        if (currentIndex < charPaths.length) {
            String path = charPaths[currentIndex];
            if (path.contains("empty.png")) {
                characterLabel.setIcon(null);
            } else {
                int charW, charH, charX, charY;

                // จัดสัดส่วนให้สมส่วน (ไม่บีบ) และอยู่กึ่งกลางอัตโนมัติ
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
        SwingUtilities.invokeLater(() -> new part9().setVisible(true));
    }
}

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