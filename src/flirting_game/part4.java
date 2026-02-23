package flirting_game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class part4 extends JFrame {
    private JLayeredPane layeredPane;
    private JLabel backgroundLabel, characterLabel, dialogueArea, nameLabel;
    private float alpha = 1.0f; // 1.0 คือดำสนิท
    private JPanel fadeOverlay;
    private VisualNovelBox dialoguePanel; 
    private int currentIndex = 0;
    private Timer typewriterTimer;
    private int charIndex = 0;
    
    // --- ฟอนต์ภาษาไทยมาตรฐาน (ปรับขนาดให้เข้ากับจอ 1280) ---
    private final Font THAI_FONT = new Font("Tahoma", Font.PLAIN, 28);
    private final Font THAI_FONT_BOLD = new Font("Tahoma", Font.BOLD, 30);

    // ... (imagePaths, charPaths, names, dialogues คงเดิมตามที่คุณให้มา) ...
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
            " ", "ฉัน", "ฉัน", "อริส", "ฉัน", "อริส", "ฉัน", "อริส", 
            "อริส", "อริส", "อริส", "อริส", "อริส", "อริส", "ฉัน", "อริส", 
            " ", "อริส", "อริส", "อริส", "ลุง", "อริส", "ลุง", "ลุง", 
            "อริส", "อริส", "ฉัน", "ปีศาจ", "ปีศาจ", "ฉัน", "ปีศาจ", "ฉัน", 
            "ปีศาจ", "ปีศาจ", "อริส","อริส", "ปีศาจ", "อริส", "อริส", " ", 
            "อริส", "ฉัน", "ปีศาจ", "ปีศาจ", "ปีศาจ", "ฉัน", "ปีศาจ","ปีศาจ", 
            "ปีศาจ", "อริส", "ฉัน", "อริส", "อริส", "ฉัน", "ฉัน", "อริส", 
            "อริส", "อริส", "ฉัน",
        };
    
    private String[] dialogues = {
        "เวลาผ่าน2ปี", 
        "นี่ก็ผ่านไป2ปีเเล้ว หลังจากที่ฉันได้มาอยู่ในโลกนี้", 
        "ตอนนี้ฉันก็น่าจะเเข็งเเกร่งขึ้นบ้างละหละ", 
        "...(ชื่อตัวละครเรา) ข้าวเที่ยงเสร็จละนะ", 
        "โอเค กําลังจะไปเดี๋ยวนี้แหละ",  
        "..กําลังยืนดูตัวเรา..", 
        "นี่เธอเเอบดูกล้ามฉันรึปล่าว?", 
        "ปล่าวซะหน่อย ใครมันจะไปดูกัน(เขิน)", 
        "ช่างเรื่องนั้นเถอะ", 
        "นี่..นายคิดว่าชุดนี้เหมาะกับฉันมั้ย?",
        "มันเป็นชุดสําหรับเดินทางหนะ", 
        "น่ารักอะไรกัน..บ้าจริง", 
        "โถ่ว..นี่นายจะไม่ชมฉันเลยบ้างรึไง", 
        "เมื่อกี้นายกําลังฝึกหรอ?", 
        "อือ..ก็นิดหน่อยอะ", 
        "รีบกินสิเดี๋ยวมันจะเย็นเอานะ", 
        "(กําลังกิน)", 
        "เป็นไงอร่อยมั้ย?",
        "จะ..จริงหรอ..งั้นก็กินเยอะๆเลยนะ", 
        "อือๆก็ดีเเล้ว", 
        "นี่!!เปิดประตูหน่อย!!", 
        "เกิดอะไรขึ้นหรอคะ?", 
        "เอ่อ..คือว่า..มันมีปีศาจมาบุกโจมตีหมู่บ้าน", 
        "มีชาวบ้านหลายคนที่ได้รับบาดเจ็บ เเต่ส่วนใหญ่ก็หนีออกมาได้", 
        "เเย่ละสิ! ต้องรีบไปจัดการเเล้ว!", 
        "ไปกันเถอะ..(ชื่อตัวละครเรา)",
        "โอเค!!", 
        "ไม่มีพวกเก่งๆเลยรึไง ฮ่าๆ", 
        "มีเเต่ชาวบ้านกระจอกๆเเบบนี้ ก็ไม่สนุกนะเส้", 
        "นี่เเกกําลังทําอะไร!!", 
        "ก็กําลังเล่นสนุกอยู่ไงหละ ฮ่าๆ", 
        "เล่นสนุกอย่างงั้นหรอ?", 
        "พวกเเกมันก็ไม่ต่างอะไรจากหนอนเเมลง!!", 
        "ชีวิตของพวกเเกก็มีไว้ให้พวกข้าสนุกเท่านั้น", 
        "เลวที่สุด..", 
        "ฉันจะไม่ให้อภัยพวกเเกเด็ดขาด!! ",
        "เเเน่จริงก็เข้ามา!!", 
        "เวทย์นํ้าเเข็ง Ice shot!!", 
        "ขอบคุณที่ช่วยนะ..(ชื่อตัวละครเรา)", 
        "อริสหลบการโจมตีได้", 
        "เวทย์นํ้าเเข็ง Ice floor", 
        "รับไปซะ! เวทย์ลม wind storm", 
        "เอ่อ..พลังเวทย์ขนาดนี้..มันเป็นใครกันนะ!?", 
        "อ้ากกกก!!", 
        "ข้าเเพ้หรอเนี่ย", 
        "ดูเหมือนเเกจะประเมินตัวเองไว้สูงเลยสินะ",
        "เอาหละ..ใครเป็นคนส่งเเกมา", 
        "เเกรู้ไปจะได้อะไรขึ้นมา", 
        "อย่างพวกเเก ไม่มีทางชนะท่านผู้นั้นได้หรอก", 
        "ท่านจอมมารผู้นั้นหนะ..", 
        "จอมมารหรอ?", 
        "อริส เธอรู้เรื่องจอมมารคนนั้นบ้างรึปล่าว?", 
        "ฉันเคยได้ยินว่ามีจอมมารคนนึงที่อยู่ลึกสุดของป่า death end", 
        "เเต่จอมมารคนนั้นดูเหมือนจะเป็น คนที่รักความสงบสุขมาก", 
        "ฟังดูเเล้วไม่มีเหตุที่จอมมารคนนั้นจะทําเรื่องเเบบนี้เลย", 
        "อริส ฉันว่ามันถึงเวลาที่เราต้องออกเดินทางเเล้วหละ", 
        "เเล้วเราจะไปที่ไหนกันหรอ?", 
        "ไปเดทอะไรบ้ารึปล่าว อร๊ายยย", 
        "ขอบคุณนะ", 
        "ไม่งั้นอาจจะมีผู้คนต้องตายไปมากกว่านี้", 
    };

    public part4() {
        setTitle("ISEKAI DEMO - Part 4: The First Magic");
        setSize(1280, 800); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        fadeOverlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 0, 0, (int)(alpha * 255)));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        fadeOverlay.setBounds(0, 0, 1280, 800); 
        fadeOverlay.setOpaque(false);
        layeredPane.add(fadeOverlay, JLayeredPane.DRAG_LAYER);

        startFadeIn();

        backgroundLabel = new JLabel();
        backgroundLabel.setBounds(0, 0, 1280, 800); 
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        characterLabel = new JLabel();
        // ปรับตำแหน่งตัวละคร (x, y, width, height) ให้อยู่กึ่งกลางหน้าจอที่กว้างขึ้น
        characterLabel.setBounds(340, 20, 600, 780); 
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
                    JOptionPane.showMessageDialog(null, "จบ Part 4: การผจญภัยกำลังจะเริ่มขึ้น!");
                    // new part5().setVisible(true); // ปลดคอมเมนต์เมื่อมีไฟล์ part5
                    dispose(); 
                }
            }
        });
    }

    private void setupDialogueUI() {
        dialoguePanel = new VisualNovelBox(); 
        dialoguePanel.setLayout(null);
        dialoguePanel.setBounds(90, 520, 1100, 220); 
        layeredPane.add(dialoguePanel, JLayeredPane.MODAL_LAYER);

        nameLabel = new JLabel("");
        nameLabel.setFont(THAI_FONT_BOLD);
        nameLabel.setForeground(new Color(180, 40, 90)); 
        nameLabel.setBounds(60, 20, 400, 45);
        dialoguePanel.add(nameLabel);

        dialogueArea = new JLabel();
        dialogueArea.setFont(THAI_FONT);
        dialogueArea.setForeground(new Color(45, 65, 115)); 
        dialogueArea.setBounds(60, 80, 980, 110);
        dialoguePanel.add(dialogueArea);

        JLabel nextArrow = new JLabel("▼");
        nextArrow.setFont(new Font("Arial", Font.BOLD, 20));
        nextArrow.setForeground(new Color(0, 153, 255));
        nextArrow.setBounds(1040, 170, 30, 30); 
        dialoguePanel.add(nextArrow);
        Timer arrowTimer = new Timer(500, ev -> nextArrow.setVisible(!nextArrow.isVisible()));
        arrowTimer.start();
    }

    private void updateScene() {
        if (currentIndex < names.length) nameLabel.setText(names[currentIndex]);
        if (currentIndex < imagePaths.length) backgroundLabel.setIcon(scaleImage(imagePaths[currentIndex], 1280, 800));
        if (currentIndex < dialogues.length) {
            updateDialogueDisplay(dialogues[currentIndex]); 
        }

        if (currentIndex < charPaths.length) {
            String path = charPaths[currentIndex];
            if (path.contains("body")) {
                // ปรับขนาดรูป Body ให้เล็กลง (กว้าง 500 สูง 700)
                characterLabel.setIcon(scaleImage(path, 500, 700));
                characterLabel.setBounds(390, 80, 500, 700); 
            } else if (path.contains("lung")) {
                // ปรับขนาดรูปตัวละครอื่นๆ (กว้าง 600 สูง 800)
                characterLabel.setIcon(scaleImage(path, 600, 800));
                characterLabel.setBounds(340, 20, 600, 800);
            } else {
                // รูปตัวละครปกติย่อขนาดลงมาให้สมส่วน
                characterLabel.setIcon(scaleImage(path, 900, 900));
                characterLabel.setBounds(190, 0, 900, 900);
            }
        }
    }

    private void startFadeIn() {
        Timer fadeTimer = new Timer(50, e -> {
            alpha -= 0.05f;
            if (alpha <= 0) {
                alpha = 0;
                ((Timer)e.getSource()).stop();
                layeredPane.remove(fadeOverlay);
                updateDialogueDisplay(dialogues[0]);
            }
            fadeOverlay.repaint();
        });
        fadeTimer.start();
    }

    private void updateDialogueDisplay(String text) {
        if (typewriterTimer != null && typewriterTimer.isRunning()) {
            typewriterTimer.stop();
        }
        charIndex = 0;
        dialogueArea.setText(""); 
        typewriterTimer = new Timer(30, e -> {
            if (charIndex < text.length()) {
                charIndex++;
                String currentText = text.substring(0, charIndex);
                dialogueArea.setText("<html><body style='width: 950px;'>" + currentText + "</body></html>");
            } else {
                typewriterTimer.stop(); 
            }
        });
        typewriterTimer.start();
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

class VisualNovelBox extends JPanel {
    private int cornerRadius = 25;

    public VisualNovelBox() {
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(255, 255, 255, 150), 
            0, getHeight(), new Color(255, 230, 240, 245)
        );
        g2.setPaint(gradient);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2.setColor(new Color(255, 120, 180));
        g2.setStroke(new BasicStroke(3)); 
        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);
    }
}