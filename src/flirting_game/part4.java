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
        "เเน่จริงก็เข้ามา!!", 
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
        setTitle("ISEKAI DEMO - Part 4");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        fadeOverlay = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(0, 0, 0, (int)(alpha * 255))); // วาดสีดำตามค่า alpha
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
    };
        fadeOverlay.setBounds(0, 0, 1000, 800);
        fadeOverlay.setOpaque(false);
        layeredPane.add(fadeOverlay, JLayeredPane.DRAG_LAYER); // อยู่ชั้นบนสุด

        startFadeIn(); // เรียกฟังก์ชันให้เริ่มจางลง

        backgroundLabel = new JLabel(scaleImage(imagePaths[0], 1000, 800));
        backgroundLabel.setBounds(0, 0, 1000, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        characterLabel = new JLabel();
        characterLabel.setBounds(0, 0, 1000, 800); 
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
                    new part5().setVisible(true);
                    dispose(); 
                }
            }
        });
    }

    private void setupDialogueUI() {
        // สร้างกล่องข้อความจาก Class VisualNovelBox ที่ประกาศไว้ด้านล่าง
        dialoguePanel = new VisualNovelBox(); 
        dialoguePanel.setLayout(null);
        dialoguePanel.setBounds(50, 550, 900, 180);
        layeredPane.add(dialoguePanel, JLayeredPane.MODAL_LAYER);

        // ปรับสีชื่อเป็นสีน้ำเงินเข้มตามสไตล์ VN
        nameLabel = new JLabel("");
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 26));
        nameLabel.setForeground(new Color(180, 40, 90)); 
        nameLabel.setBounds(60, 10, 300, 40);
        dialoguePanel.add(nameLabel);

        // ปรับสีบทสนทนา
        dialogueArea = new JLabel();
        dialogueArea.setFont(new Font("Tahoma", Font.BOLD, 22));
        dialogueArea.setForeground(new Color(45, 65, 115)); 
        dialogueArea.setBounds(60, 40, 800, 100);
        dialoguePanel.add(dialogueArea);

        // เพิ่มลูกศรฟ้ากะพริบมุมขวาล่าง
        JLabel nextArrow = new JLabel("▼");
        nextArrow.setFont(new Font("Arial", Font.BOLD, 20));
        nextArrow.setForeground(new Color(0, 153, 255));
        nextArrow.setBounds(850, 130, 30, 30);
        dialoguePanel.add(nextArrow);
        Timer arrowTimer = new Timer(500, ev -> nextArrow.setVisible(!nextArrow.isVisible()));
        arrowTimer.start();
    }

    private void updateScene() {
        if (currentIndex < names.length) nameLabel.setText(names[currentIndex]);
        if (currentIndex < imagePaths.length) backgroundLabel.setIcon(scaleImage(imagePaths[currentIndex], 1000, 800));
        if (currentIndex < dialogues.length) {
        updateDialogueDisplay(dialogues[currentIndex]); 
    }

        if (currentIndex < charPaths.length) {
            String path = charPaths[currentIndex];
            if (path.contains("body")) {
                characterLabel.setIcon(scaleImage(path, 500, 700));
                characterLabel.setBounds(250, 50, 500, 700); 
            } else if (path.contains("lung")) {
                // ถ้าเป็นรูป Lung ให้ปรับขนาดเล็กลงและวางตำแหน่งกึ่งกลาง
                characterLabel.setIcon(scaleImage(path, 800, 800));
                characterLabel.setBounds(200, 50, 800, 800);
                
            } else {
                characterLabel.setIcon(scaleImage(path, 800, 800));
                characterLabel.setBounds(100, 0, 800, 800);
            }
        }
    }

    private void startFadeIn() {
    Timer fadeTimer = new Timer(50, e -> {
        alpha -= 0.05f; // ค่อยๆ ลดความดำลงทีละ 0.05
        if (alpha <= 0) {
            alpha = 0;
            ((Timer)e.getSource()).stop();
            layeredPane.remove(fadeOverlay); // ลบหน้ากากออกเมื่อใสแล้ว
            updateDialogueDisplay(dialogues[0]); // เริ่มพิมพ์ข้อความบรรทัดแรก
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
        dialogueArea.setText(""); // ล้างข้อความเก่าออก

        // ตั้งเวลาให้ทำงานทุกๆ 35 มิลลิวินาที (ปรับตัวเลขได้ ยิ่งน้อยยิ่งไว)
        typewriterTimer = new Timer(30, e -> {
        if (charIndex < text.length()) {
            charIndex++;
            String currentText = text.substring(0, charIndex);
            dialogueArea.setText("<html><body style='width: 750px;'>" + currentText + "</body></html>");
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

// Class สำหรับวาด Textbox สไตล์ Visual Novel
class VisualNovelBox extends JPanel {
    private int cornerRadius = 25;

    public VisualNovelBox() {
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // วาดพื้นหลังไล่สี ขาว -> ชมพูอ่อน
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(255, 255, 255, 150), 
            0, getHeight(), new Color(255, 230, 240, 245)
        );
        g2.setPaint(gradient);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        // วาดเส้นขอบสีชมพูเข้ม
        g2.setColor(new Color(255, 120, 180));
        g2.setStroke(new BasicStroke(3)); 
        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);
    }
}