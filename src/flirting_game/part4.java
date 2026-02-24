package flirting_game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;

public class part4 extends JFrame {
    // --- UI Components ---
    private JLayeredPane layeredPane;
    private JLabel backgroundLabel, characterLabel, characterLabel2, dialogueArea, nameLabel;
    private float alpha = 1.0f;
    private JPanel fadeOverlay;
    private VisualNovelBox dialoguePanel; 
    private int currentIndex = 0;
    private Clip bgmClip;      
    private Clip effectClip;   
    private JButton choiceButton1, choiceButton2;
    private boolean isChoosing = false;
    private Timer typewriterTimer;
    private int charIndex = 0;
    private boolean isTyping = false;
    private Map<String, ImageIcon> imageCache = new HashMap<>();
    
    // --- ฟอนต์ภาษาไทยสำหรับจอ 1280 ---
    private final Font THAI_FONT_PLAIN = new Font("Tahoma", Font.PLAIN, 28);
    private final Font THAI_FONT_BOLD = new Font("Tahoma", Font.BOLD, 30);

    // --- ข้อมูล Array (คงเดิมตามที่คุณส่งมา) ---
    private String[] imagePaths = {
        "res/scene4/s1.png", "res/scene4/s2.png", "res/scene4/s2.png", "res/scene4/s2.png",
        "res/scene4/s2.png", "res/scene4/s2.png", "res/scene4/s2.png", "res/scene4/s2.png",
        "res/scene4/s2.png", "res/scene4/s2.png", "res/scene4/s2.png", "res/scene4/s2.png",
        "res/scene4/s2.png", "res/scene4/s3.png", "res/scene4/s3.png", "res/scene4/s3.png", 
        "res/scene4/s3.png", "res/scene4/s3.png", "res/scene4/s3.png","res/scene4/s3.png",
        "res/scene4/s4.1.png", "res/scene4/s4.png", "res/scene4/s4.png", "res/scene4/s4.png",
        "res/scene4/s4.png", "res/scene4/s4.png", "res/scene4/s4.png", "res/scene4/s5.png", 
        "res/scene4/s5.png", "res/scene4/s5.png", "res/scene4/s5.png", "res/scene4/s5.png", 
        "res/scene4/s5.png", "res/scene4/s5.png", "res/scene4/s5.png", "res/scene4/s5.png",
        "res/scene4/s5.png", "res/scene4/s6.png", "res/scene4/s6.png","res/scene4/s6.png", "res/scene4/s6.png",
        "res/scene4/s6.png", "res/scene4/s6.png", "res/scene4/s6.png", "res/scene4/s6.png",
        "res/scene4/s6.png", "res/scene4/s7.png", "res/scene4/s7.png", "res/scene4/s7.png", 
        "res/scene4/s7.png", "res/scene4/s7.png", "res/scene4/s7.png", "res/scene4/s7.png",
        "res/scene4/s7.png", "res/scene4/s7.png", "res/scene4/s3.png", "res/scene4/s3.png",
        "res/scene4/s3.png", "res/scene4/s3.png", "res/scene4/s3.png", "res/scene4/s3.png",
        "res/scene4/s3.png", "res/scene4/s3.png", "res/scene4/s3.png"
    };
    
    private String[] charPaths2 = {
        "res/empty.png", "res/empty.png", "res/empty.png", 
        "res/Charactor/Alice/Girl/Alice-normal1.png", "res/Charactor/Alice/Girl/Alice-normal2.png",
        "res/Charactor/Alice/Girl/Alice-normal1.png", "res/Charactor/Alice/Girl/Alice-shy1.png",
        "res/Charactor/Alice/Girl/Alice-shy2.png", "res/Charactor/Alice/Girl/Alice-shy1.png",
        "res/Charactor/Alice/Girl/Alice-normal1.png", "res/Charactor/Alice/Girl/Alice-normal2.png",
        "res/Charactor/Alice/Girl/Alice-shy2.png", "res/Charactor/Alice/Girl/Alice-shy1.png",
        "res/Charactor/Alice/Girl/Alice-normal1.png", "res/Charactor/Alice/Girl/Alice-normal2.png",
        "res/Charactor/Alice/Girl/Alice-normal1.png", "res/Charactor/Alice/Girl/Alice-normal2.png",
        "res/Charactor/Alice/Girl/Alice-shy1.png", "res/Charactor/Alice/Girl/Alice-shy1.png",
        "res/Charactor/Alice/Girl/Alice-shy2.png", "res/empty.png",
        "res/Charactor/Alice/Girl/Alice-normal1.png", "res/Charactor/Alice/Girl/Alice-normal2.png",
        "res/Charactor/Alice/Girl/Alice-normal1.png", "res/Charactor/Alice/Girl/Alice-normal1.png",
        "res/Charactor/Alice/Girl/Alice-normal2.png", "res/Charactor/Alice/Girl/Alice-normal1.png",
        "res/empty.png", "res/empty.png", "res/Charactor/Alice/Girl/Alice-fight2.png", "res/Charactor/Alice/Girl/Alice-fight1.png",
        "res/Charactor/Alice/Girl/Alice-fight1.png", "res/Charactor/Alice/Girl/Alice-fight2.png",
        "res/Charactor/Alice/Girl/Alice-fight1.png", "res/Charactor/Alice/Girl/Alice-fight2.png",
        "res/Charactor/Alice/Girl/Alice-fight1.png", "res/empty.png", "res/empty.png", "res/empty.png", 
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
        "res/empty.png", "res/Charactor/Alice/Girl/Alice-fight2.png", "res/Charactor/Alice/Girl/Alice-fight1.png", 
        "res/Charactor/Alice/Girl/Alice-fight2.png", "res/Charactor/Alice/Girl/Alice-fight1.png", 
        "res/Charactor/Alice/Girl/Alice-fight2.png", "res/Charactor/Alice/Girl/Alice-fight1.png",
        "res/Charactor/Alice/Girl/Alice-fight1.png", "res/Charactor/Alice/Girl/Alice-fight2.png",
        "res/Charactor/Alice/Girl/Alice-fight1.png","res/Charactor/Alice/Girl/Alice-normal1.png",
        "res/Charactor/Alice/Girl/Alice-normal2.png","res/Charactor/Alice/Girl/Alice-normal1.png",
        "res/Charactor/Alice/Girl/Alice-shy1.png","res/Charactor/Alice/Girl/Alice-normal2.png",
        "res/Charactor/Alice/Girl/Alice-normal1.png",
    };

    private String[] charPaths = {
        "res/empty.png", "res/Charactor/Mc/body1.PNG", "res/Charactor/Mc/body1.PNG", 
        "res/Charactor/Mc/body1.PNG", "res/Charactor/Mc/body2.PNG", "res/Charactor/Mc/body1.PNG", 
        "res/Charactor/Mc/body2.PNG", "res/Charactor/Mc/body1.PNG", "res/Charactor/Mc/body1.PNG",
        "res/Charactor/Mc/body1.PNG", "res/Charactor/Mc/body1.PNG", "res/Charactor/Mc/body1.PNG",
        "res/Charactor/Mc/body1.PNG", "res/Charactor/Mc/body1.PNG", "res/Charactor/Mc/body1.PNG",
        "res/Charactor/Mc/body1.PNG", "res/Charactor/Mc/body1.PNG", "res/Charactor/Mc/body1.PNG", 
        "res/Charactor/Mc/body1.PNG", "res/Charactor/Mc/body1.PNG", "res/empty.png", 
        "res/Charactor/factor/Uncle.png", "res/Charactor/factor/Uncle.png", "res/Charactor/factor/Uncle.png", 
        "res/Charactor/factor/Uncle.png", "res/Charactor/factor/Uncle.png", "res/Charactor/factor/Uncle.png", 
        "res/Charactor/factor/demon1.PNG", "res/Charactor/factor/demon1.PNG", "res/Charactor/factor/demon1.PNG",
        "res/Charactor/factor/demon1.PNG", "res/Charactor/factor/demon1.PNG", "res/Charactor/factor/demon1.PNG",
        "res/Charactor/factor/demon1.PNG", "res/Charactor/factor/demon1.PNG", "res/Charactor/factor/demon1.PNG",
        "res/Charactor/factor/demon1.PNG", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/Charactor/factor/demon2.PNG", 
        "res/Charactor/factor/demon2.png", "res/Charactor/factor/demon2.png", "res/Charactor/factor/demon2.png", 
        "res/Charactor/factor/demon2.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png"
    };

    private String[] names = {
        " ", "ฉัน", "ฉัน", "อริส", "ฉัน", "อริส", "ฉัน", "อริส", 
        "อริส", "อริส", "อริส", "อริส", "อริส", "อริส", "ฉัน", "อริส", 
        " ", "อริส", "อริส", "อริส", "ลุง", "อริส", "ลุง", "ลุง", 
        "อริส", "อริส", "ฉัน", "ปีศาจ", "ปีศาจ", "ฉัน", "ปีศาจ", "ฉัน", 
        "ปีศาจ", "ปีศาจ", "อริส","อริส", "ปีศาจ", "อริส", " ", "อริส", " ", 
        "อริส", "ฉัน", "ปีศาจ", "ปีศาจ", "ปีศาจ", "ฉัน", "ฉัน","ปีศาจ", 
        "ปีศาจ", "ปีศาจ", "อริส", "ฉัน", "อริส", "อริส", "ฉัน", "อริส", 
        "อริส", "อริส", "ฉัน"
    };
    
    private String[] dialogues = {
        "เวลาผ่าน2ปี", "นี่ก็ผ่านไป2ปีเเล้ว หลังจากที่ฉันได้มาอยู่ในโลกนี้", 
        "ตอนนี้ฉันก็น่าจะเเข็งเเกร่งขึ้นบ้างละหละ", "...(ชื่อตัวละครเรา) ข้าวเที่ยงเสร็จละนะ", 
        "โอเค กําลังจะไปเดี๋ยวนี้แหละ", "..กําลังยืนดูตัวเรา..", "นี่เธอเเอบดูกล้ามฉันรึปล่าว?", 
        "ปล่าวซะหน่อย ใครมันจะไปดูกัน", "ช่างเรื่องนั้นเถอะ", "นี่..นายคิดว่าชุดนี้เหมาะกับฉันมั้ย?", 
        "มันเป็นชุดสําหรับเดินทางหนะ", "น่ารักอะไรกัน..บ้าจริง", "โถ่ว..นี่นายจะไม่ชมฉันเลยบ้างรึไง", 
        "เมื่อกี้นายกําลังฝึกหรอ?", "อือ..ก็นิดหน่อยอะ", "รีบกินสิเดี๋ยวมันจะเย็นเอานะ", 
        "(กําลังกิน)", "เป็นไงอร่อยมั้ย?", "จะ..จริงหรอ..งั้นก็กินเยอะๆเลยนะ", 
        "อือๆก็ดีเเล้ว", "นี่!!เปิดประตูหน่อย!!", "เกิดอะไรขึ้นหรอคะ?", 
        "เอ่อ..คือว่า..มันมีปีศาจมาบุกโจมตีหมู่บ้าน", "มีชาวบ้านหลายคนที่ได้รับบาดเจ็บ เเต่ส่วนใหญ่ก็หนีออกมาได้", 
        "เเย่ละสิ! ต้องรีบไปจัดการเเล้ว!", "ไปกันเถอะ..(ชื่อตัวละครเรา)", "โอเค!!", 
        "ไม่มีพวกเก่งๆเลยรึไง ฮ่าๆ", "มีเเต่ชาวบ้านกระจอกๆเเบบนี้ ก็ไม่สนุกนะเส้", 
        "นี่เเกกําลังทําอะไร!!", "ก็กําลังเล่นสนุกอยู่ไงหละ ฮ่าๆ", "เล่นสนุกอย่างงั้นหรอ?", 
        "พวกเเกมันก็ไม่ต่างอะไรจากหนอนเเมลง!!", "ชีวิตของพวกเเกก็มีไว้ให้พวกข้าสนุกเท่านั้น", 
        "เลวที่สุด..", "ฉันจะไม่ให้อภัยพวกเเกเด็ดขาด!! ", "เเน่จริงก็เข้ามา!!", 
        "เวทย์นํ้าเเข็ง Ice shot!!","ปีศาจหลบได้ เเละกําลังจะโจมตี อริส", "ขอบคุณที่ช่วยนะ..(ชื่อตัวละครเรา)", "อริสหลบการโจมตีได้", 
        "เวทย์นํ้าเเข็ง Ice floor", "รับไปซะ! เวทย์ลม wind storm", "เอ่อ..พลังเวทย์ขนาดนี้..มันเป็นใครกันนะ!?", 
        "อ้ากกกก!!", "ข้าเเพ้หรอเนี่ย", "ดูเหมือนเเกจะประเมินตัวเองไว้สูงเลยสินะ", 
        "เอาหละ..ใครเป็นคนส่งเเกมา", "เเกรู้ไปจะได้อะไรขึ้นมา", "อย่างพวกเเก ไม่มีทางชนะท่านผู้นั้นได้หรอก", 
        "ท่านจอมมารผู้นั้นหนะ..", "จอมมารหรอ?", "อริส เธอรู้เรื่องจอมมารคนนั้นบ้างรึปล่าว?", 
        "ฉันเคยได้ยินว่ามีจอมมารคนนึงที่อยู่ลึกสุดของป่า death end", "เเต่จอมมารคนนั้นดูเหมือนจะเป็น คนที่รักความสงบสุขมาก", 
        "ฟังดูเเล้วไม่มีเหตุที่จอมมารคนนั้นจะทําเรื่องเเบบนี้เลย", "อริส ฉันว่ามันถึงเวลาที่เราต้องออกเดินทางเเล้วหละ", 
        "เเล้วเราจะไปที่ไหนกันหรอ?", "ไปเดทอะไรบ้ารึปล่าว อร๊ายยยย", "ขอบคุณนะ", 
        "ไม่งั้นอาจจะมีผู้คนต้องตายไปมากกว่านี้"
    };

    public part4() {
        setTitle("ISEKAI DEMO - Part 4");
        setSize(1280, 800); // มาตรฐาน 1280x800
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        playSE("res/sound/soundtrack5.wav", true, -10.0f);

        backgroundLabel = new JLabel();
        backgroundLabel.setBounds(0, 0, 1280, 800); // BG เต็มจอ
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        characterLabel = new JLabel(); // เลเยอร์สำหรับ Mc
        layeredPane.add(characterLabel, JLayeredPane.PALETTE_LAYER);
        
        characterLabel2 = new JLabel(); // เลเยอร์สำหรับ อริส
        layeredPane.add(characterLabel2, JLayeredPane.PALETTE_LAYER);

        setupDialogueUI();

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

        layeredPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleNext();
            }
        });
    }

    private void handleNext() {
        if (isChoosing) return;

        if (isTyping) {
            typewriterTimer.stop();
            isTyping = false;
            dialogueArea.setText("<html><body style='width: 950px;'>" + dialogues[currentIndex] + "</body></html>");
            return;
        }

        // --- Choice Logic ---
        if (currentIndex == 10) { 
            showChoices("น่ารักมากๆเลย เหมาะกับเธอสุดๆ", "ก็พอได้นะ", 11, 12); 
            return; 
        } 
        if (currentIndex == 11) { 
            currentIndex = 13; 
            updateScene(); 
            return; 
        }
        if (currentIndex == 17) { 
            showChoices("ฉันชอบอาหารฝีมือเธอที่สุดเลย", "ก็อร่อยดีนะ", 18, 19); 
            return; 
        }
        if (currentIndex == 18) { 
            currentIndex = 20; 
            updateScene(); 
            return; 
        }
        if (currentIndex == 38) { 
            showChoices("พุ่งเข้าไปปกป้องอริส", "บอกให้อริสหลบเอง", 39, 40); 
            return; 
        }
        if (currentIndex == 39) { 
            currentIndex = 41; 
            updateScene(); 
            return; 
        }
        if (currentIndex == 57) { 
            showChoices("เราจะไปเดทกันไงละจ๊ะ อริสจัง", "ที่อยู่ของจอมมารยังไงหละ ", 58, 59);
            return; 
        }
        if (currentIndex == 58) { 
            currentIndex = 60; 
            updateScene(); 
            return; 
        }

        if (currentIndex < dialogues.length - 1) {
            currentIndex++;
            updateScene();
        } else {
            finishGame();
        }
    }

    private void updateScene() {
        if (currentIndex < names.length) {
            nameLabel.setText(names[currentIndex]);
        } else {
            nameLabel.setText("");
        }
        if (currentIndex < dialogues.length) {
            updateDialogueDisplay(dialogues[currentIndex]);
        }

        handleSoundEffects(currentIndex);

        backgroundLabel.setIcon(getOptimizedImage(imagePaths[currentIndex], 1280, 800));
        
        // เลเยอร์ที่ 1 (Mc ยืนซ้าย)
        updateCharacterLayer(characterLabel, charPaths, true); 
        // เลเยอร์ที่ 2 (อริส ยืนขวา)
        updateCharacterLayer(characterLabel2, charPaths2, false);
        
        layeredPane.repaint();
    }

    private void updateCharacterLayer(JLabel label, String[] paths, boolean isFirstLayer) {
        if (currentIndex >= paths.length || paths[currentIndex].contains("empty")) {
            label.setIcon(null);
            return;
        }

        String path = paths[currentIndex];

        // --- ระบบตรวจสอบและปรับขนาดตามตัวละคร ---
        if (path.contains("Mc/body")) {
            // Mc (ตัวละครชาย) ใช้ขนาด 900x900 และวางฝั่งซ้าย
            label.setIcon(getOptimizedImage(path, 500, 900));
            label.setBounds(50, 100, 600, 900); 
        } 
        else if (path.contains("Alice") || path.contains("Girl")) {
            // --- จุดสำคัญ: ปรับอริสเป็น 600x900 เพื่อไม่ให้บีบอ้วน ---
            label.setIcon(getOptimizedImage(path, 1050, 700));
            // ขยับ X ไปที่ 680 เพื่อให้ยืนห่างจาก Mc อย่างสวยงาม
            label.setBounds(420, 70, 1300, 900); 
        } 
        else if (path.contains("Uncle.png") && path.contains("factor")) {
            // --- จุดสำคัญ: ปรับอริสเป็น 600x900 เพื่อไม่ให้บีบอ้วน ---
            label.setIcon(getOptimizedImage(path, 900, 900));
            // ขยับ X ไปที่ 680 เพื่อให้ยืนห่างจาก Mc อย่างสวยงาม
            label.setBounds(-100, 225, 900, 900); 
        } 
        else {
            // สำหรับตัวละครอื่นๆ หรือฉากต่อสู้ ใช้พิกัดมาตรฐาน
            label.setIcon(getOptimizedImage(path, 800, 900));
            label.setBounds(420, 100, 800, 900);
        }
    }

    private void setupDialogueUI() {
        dialoguePanel = new VisualNovelBox(); 
        dialoguePanel.setLayout(null);
        dialoguePanel.setBounds(225, 520, 800, 200);
        layeredPane.add(dialoguePanel, JLayeredPane.MODAL_LAYER);

        nameLabel = new JLabel("");
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 26));
        nameLabel.setForeground(new Color(180, 40, 90)); 
        nameLabel.setBounds(60, 10, 300, 40);
        dialoguePanel.add(nameLabel);

        dialogueArea = new JLabel();
        dialogueArea.setFont(new Font("Tahoma", Font.BOLD, 22));
        dialogueArea.setForeground(new Color(45, 65, 115)); 
        dialogueArea.setBounds(60, 60, 800, 110);
        dialogueArea.setVerticalAlignment(SwingConstants.TOP);
        dialoguePanel.add(dialogueArea);

        JLabel nextArrow = new JLabel("▼");
        nextArrow.setFont(new Font("Arial", Font.BOLD, 20));
        nextArrow.setForeground(new Color(0, 153, 255));
        nextArrow.setBounds(850, 130, 30, 30);
        dialoguePanel.add(nextArrow);
        Timer arrowTimer = new Timer(500, ev -> nextArrow.setVisible(!nextArrow.isVisible()));
        arrowTimer.start();
    }

    // ... (ส่วน Sound และ Fade คงเดิม) ...

    private void finishGame() {
        stopBGM();
        UIManager.put("OptionPane.messageFont", THAI_FONT_PLAIN);
        JOptionPane.showMessageDialog(null, "จบ Part 4: การผจญภัยกำลังจะเริ่มขึ้น!");
        new part5().setVisible(true);
        dispose();
    }

    private void updateDialogueDisplay(String text) {
        if (typewriterTimer != null) typewriterTimer.stop();
        charIndex = 0; isTyping = true; dialogueArea.setText("");
        typewriterTimer = new Timer(25, e -> {
            if (charIndex < text.length()) {
                charIndex++;
                dialogueArea.setText("<html><body style='width: 950px;'>" + text.substring(0, charIndex) + "</body></html>");
            } else { typewriterTimer.stop(); isTyping = false; }
        });
        typewriterTimer.start();
    }

    public void playEffect(String path, float volume) {
        try {
            File soundFile = new File(path); 
            if (soundFile.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                Clip temporaryClip = AudioSystem.getClip(); 
                temporaryClip.open(audioIn);
                FloatControl gainControl = (FloatControl) temporaryClip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volume); 
                temporaryClip.start();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void playSE(String path, boolean loop, float volume) {
        try {
            File soundFile = new File(path);
            if (!soundFile.exists()) return;
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volume);
            if (loop) clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            if (path.contains("soundtrack")) this.bgmClip = clip;
            else this.effectClip = clip;
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void stopBGM() {
        if (bgmClip != null) { bgmClip.stop(); bgmClip.close(); bgmClip = null; }
    }

    private void handleSoundEffects(int index) {
        if (index == 11) playEffect("res/sound/baka.wav", 5.0f);
        if (index == 12) playEffect("res/sound/muuuu.wav", 5.0f);
        if (index == 18) playEffect("res/sound/hhonto.wav", 5.0f);
        if (index == 19) playEffect("res/sound/emmm.wav", 5.0f);
        if (index == 22) { 
            stopBGM(); 
            playSE("res/sound/soundtrack6.wav", false, -10.0f); 
        }
        if (index == 27) { playEffect("res/sound/evillaugh.wav", -10.0f); playEffect("res/sound/housefire.wav", -10.0f); }
        if (index == 42) playEffect("res/sound/winddash.wav", -10.0f);
        if (index == 46) { stopBGM(); playSE("res/sound/soundtrack7.wav", true, -10.0f); }
        if (index == 58) playEffect("res/sound/Baka janai no.wav", 5.0f);
        if (index == 59) playEffect("res/sound/Arigato.wav", 5.0f);
    }

    private ImageIcon getOptimizedImage(String path, int w, int h) {
        String key = path + w + h;
        if (!imageCache.containsKey(key)) {
            imageCache.put(key, scaleImage(path, w, h));
        }
        return imageCache.get(key);
    }

    public ImageIcon scaleImage(String path, int width, int height) {
        try {
            return new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        } catch (Exception e) { return null; }
    }

    private void startFadeIn() {
        Timer fadeTimer = new Timer(45, e -> {
            alpha -= 0.05f;
            if (alpha <= 0) { alpha = 0; ((Timer)e.getSource()).stop(); layeredPane.remove(fadeOverlay); updateScene(); }
            fadeOverlay.repaint();
        });
        fadeTimer.start();
    }

    private void showChoices(String text1, String text2, int t1, int t2) {
        isChoosing = true; 
        choiceButton1 = createChoiceButton(text1, 380, t1); //y: ขึ้น=ลง
        choiceButton2 = createChoiceButton(text2, 450, t2); //y: ขึ้น=ลง
        layeredPane.add(choiceButton1, JLayeredPane.POPUP_LAYER);
        layeredPane.add(choiceButton2, JLayeredPane.POPUP_LAYER);
        layeredPane.repaint();
    }

    private JButton createChoiceButton(String text, int y, int target) {
        JButton btn = new JButton(text) {
            // Override paintComponent เพื่อวาดปุ่มให้มีขอบโค้งมน
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // วาดพื้นหลังโค้งมน
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                // วาดเส้นขอบโค้งมน
                g2.setColor(new Color(225, 105, 180)); // สีขอบเดิม
                g2.setStroke(new BasicStroke(2));   // ความหนาขอบเดิม
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 22, 22);

                g2.dispose();

                // วาดข้อความและส่วนอื่นๆ ทับลงไป
                super.paintComponent(g);
            }
        };

        // เลื่อนปุ่มไปทางขวา
        btn.setBounds(800, y, 350, 60); 
        btn.setFont(new Font("Tahoma", Font.BOLD, 20));
        btn.setForeground(new Color(45,65,115)); // สีข้อความ
        btn.setBackground(new Color(255, 255, 255, 150)); 
        // ตั้งค่าเพื่อให้วาดปุ่มแบบกำหนดเองได้
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false); // ปิดการวาดขอบสี่เหลี่ยมเดิม

        btn.setBorder(BorderFactory.createLineBorder(new Color(255, 204, 0), 2)); 
        btn.addActionListener(e -> {
            layeredPane.remove(choiceButton1); layeredPane.remove(choiceButton2);
            isChoosing = false; currentIndex = target; updateScene(); 
        });
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new part4().setVisible(true));
    }
}

// คลาสเสริมที่ใช้ร่วมกันทก Part
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