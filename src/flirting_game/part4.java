package flirting_game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.*;
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
    private boolean isFading = false;
    private Map<String, ImageIcon> imageCache = new HashMap<>();

    // --- Character Fade & Animation ---
    private float charAlpha1 = 0.0f; 
    private float charAlpha2 = 0.0f; 
    private Timer charFadeTimer1, charFadeTimer2;
    private boolean isAnimatingEntry = false;
    private Timer pulseTimer;
    private boolean pulseDown = true;
    
    // --- Leaderboard UI ---
    private JLabel affinityLabel, statusLabel;
    private JPanel statusOverlay;
    private JLabel onlineCountLabel, affinityStatusLabel;

    private PrintWriter networkOut;
    private final Font THAI_FONT_PLAIN = new Font("Tahoma", Font.PLAIN, 28);

    // --- Data Arrays (ปรับขนาดเป็น 61 ตัวเพื่อกัน Index 59 Error) ---
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
        "res/scene4/s3.png", "res/scene4/s3.png", "res/scene4/s3.png", "res/scene4/s3.png"
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
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png"
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
        "res/Charactor/Alice/Girl/Alice-normal1.png"
    };

    private String[] names = {
        " ", "ฉัน", "ฉัน", "อริส", "ฉัน", "อริส", "ฉัน", "อริส", 
        "อริส", "อริส", "อริส", "อริส", "อริส", "อริส", "ฉัน", "อริส", 
        " ", "อริส", "อริส", "อริส", "ลุง", "อริส", "ลุง", "ลุง", 
        "อริส", "อริส", "ฉัน", "ปีศาจ", "ปีศาจ", "ฉัน", "ปีศาจ", "ฉัน", 
        "ปีศาจ", "ปีศาจ", "อริส","อริส", "ปีศาจ", "อริส", " ", "อริส", " ", 
        "อริส", "ฉัน", "ปีศาจ", "ปีศาจ", "ปีศาจ", "ฉัน", "ฉัน","ปีศาจ", 
        "ปีศาจ", "ปีศาจ", "อริส", "ฉัน", "อริส", "อริส", "ฉัน", "อริส", 
        "อริส", "อริส", "ฉัน", "ฉัน"
    };
    
    private String[] dialogues = {
        "เวลาผ่าน2ปี", "นี่ก็ผ่านไป2ปีเเล้ว หลังจากที่ฉันได้มาอยู่ในโลกนี้", 
        "ตอนนี้ฉันก็น่าจะเเข็งเเกร่งขึ้นบ้างละหละ", "(ชื่อตัวละครเรา) ข้าวเที่ยงเสร็จละนะ", 
        "โอเค กําลังจะไปเดี๋ยวนี้แหละ", "..กําลังยืนดูตัวเรา..", "นี่เธอเเอบดูกล้ามฉันรึปล่าว?", 
        "ปล่าวซะหน่อย ใครมันจะไปดูกัน", "ช่างเรื่องนั้นเถอะ", "นี่..นายคิดว่าชุดนี้เหมาะกับฉันมั้ย?", 
        "มันเป็นชุดสําหรับเดินทางหนะ", "น่ารักอะไรกัน..บ้าจริง", "โถ่ว..นี่นายจะไม่ชมฉันเลยบ้างรึไง", 
        "เมื่อกี้นายกําลังฝึกหรอ?", "อือ..ก็นิดหน่อยอะ", "รีบกินสิเดี๋ยวมันจะเย็นเอานะ", 
        "(กําลังกิน)", "เป็นไงอร่อยมั้ย?", "จะ..จริงหรอ..งั้นก็กินเยอะๆเลยนะ", 
        "อือๆก็ดีเเล้ว", "นี่!!เปิดประตูหน่อย!!", "เกิดอะไรขึ้นหรอคะ?", 
        "เอ่อ..คือว่า..มันมีปีศาจมาบุกโจมตีหมู่บ้าน", "มีชาวบ้านหลายคนที่ได้รับบาดเจ็บ เเต่ส่วนใหญ่ก็หนีออกมาได้", 
        "เเย่ละสิ! ต้องรีบไปจัดการเเล้ว!", "ไปกันเถอะ (ชื่อตัวละครเรา)", "โอเค!!", 
        "ไม่มีพวกเก่งๆเลยรึไง ฮ่าๆ", "มีเเต่ชาวบ้านกระจอกๆเเบบนี้ ก็ไม่สนุกนะเส้", 
        "นี่เเกกําลังทําอะไร!!", "ก็กําลังเล่นสนุกอยู่ไงหละ ฮ่าๆ", "เล่นสนุกอย่างงั้นหรอ?", 
        "พวกเเกมันก็ไม่ต่างอะไรจากหนอนเเถลง!!", "ชีวิตของพวกเเกก็มีไว้ให้พวกข้าสนุกเท่านั้น", 
        "เลวที่สุด..", "ฉันจะไม่ให้อภัยพวกเเกเด็ดขาด!! ", "เเน่จริงก็เข้ามา!!", 
        "เวทย์นํ้าเเข็ง Ice shot!!","ปีศาจหลบได้ เเละกําลังจะโจมตี อริส", "ขอบคุณที่ช่วยนะ (ชื่อตัวละครเรา)", "อริสหลบการโจมตีได้", 
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
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setFocusTraversalKeysEnabled(false);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        playSE("res/sound/soundtrack5.wav", true, -10.0f);

        backgroundLabel = new JLabel();
        backgroundLabel.setBounds(0, 0, 1280, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        characterLabel = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, charAlpha1)); 
                super.paintComponent(g2d); g2d.dispose();
            }
        };
        layeredPane.add(characterLabel, JLayeredPane.PALETTE_LAYER);

        characterLabel2 = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, charAlpha2)); 
                super.paintComponent(g2d); g2d.dispose();
            }
        };
        layeredPane.add(characterLabel2, JLayeredPane.PALETTE_LAYER);

        setupDialogueUI();
        for (int i = 0; i < dialogues.length; i++) {
            if (dialogues[i].contains("(ชื่อตัวละครเรา)")) {
                dialogues[i] = dialogues[i].replace("(ชื่อตัวละครเรา)", relationdata.playerName);
            }
        }

        setupRelationshipUI();
        setupStatusOverlay(); 
        setupTabKeyBinding(); 
        setupFadeOverlay();
        initNetwork();

        startFadeIn();
        updateScene();

        layeredPane.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { handleNext(); }
        });
    }

    private void handleNext() {
        if (isChoosing || isFading || isAnimatingEntry) return;
        if (isTyping) { 
            if(typewriterTimer != null) typewriterTimer.stop(); 
            isTyping = false; 
            updateDialogueDisplay(dialogues[currentIndex]); return; 
        }

        if (currentIndex == 10) { showChoices("น่ารักมากๆเลย เหมาะกับเธอสุดๆ", "ก็พอได้นะ", 11, 12); return; }
        if (currentIndex == 17) { showChoices("ฉันชอบอาหารฝีมือเธอที่สุดเลย", "ก็อร่อยดีนะ", 18, 19); return; }
        if (currentIndex == 38) { showChoices("พุ่งเข้าไปปกป้องอริส", "บอกให้อริสหลบเอง", 39, 40); return; }
        if (currentIndex == 57) { showChoices("เราจะไปเดทกันไงละจ๊ะ อริสจัง", "ที่อยู่ของจอมมารยังไงหละ", 58, 59); return; }

        int nextIdx = currentIndex;
        if (currentIndex == 11) nextIdx = 13;
        else if (currentIndex == 18) nextIdx = 20;
        else if (currentIndex == 39) nextIdx = 41;
        else if (currentIndex == 58) nextIdx = 60;
        else nextIdx++;

        if (nextIdx < dialogues.length) {
            if (!imagePaths[currentIndex].equals(imagePaths[nextIdx])) {
                final int target = nextIdx;
                performSceneFade(() -> { currentIndex = target; syncOnline(); updateScene(); });
            } else { currentIndex = nextIdx; syncOnline(); updateScene(); }
        } else { finishPart(); }
    }

    // --- ระบบ Animation เข้าฉาก (เลื่อนและ Fade) ---
    private void animateCharacterEntry(JLabel label, String path, int w, int h, int startX, int endX, int targetY, boolean isChar1) {
        isAnimatingEntry = true;
        label.setIcon(getOptimizedImage(path, w, h));
        label.setBounds(startX, targetY, w, h);
        if (isChar1) charAlpha1 = 0.0f; else charAlpha2 = 0.0f;

        Timer fadeIn = new Timer(30, null);
        fadeIn.addActionListener(e -> {
            if (isChar1) charAlpha1 += 0.1f; else charAlpha2 += 0.1f;
            if ((isChar1 ? charAlpha1 : charAlpha2) >= 1.0f) {
                if (isChar1) charAlpha1 = 1.0f; else charAlpha2 = 1.0f;
                fadeIn.stop();
                
                // เริ่มการเลื่อน (Slide)
                Timer slide = new Timer(15, null);
                final long start = System.currentTimeMillis();
                slide.addActionListener(ev -> {
                    float p = Math.min(1.0f, (System.currentTimeMillis() - start) / 500.0f);
                    int curX = (int) (startX + (endX - startX) * p);
                    label.setBounds(curX, targetY, w, h);
                    if (p >= 1.0f) { slide.stop(); isAnimatingEntry = false; }
                });
                slide.start();
            }
            label.repaint();
        });
        fadeIn.start();
    }

    private void updateScene() {
        if (currentIndex < names.length) nameLabel.setText(names[currentIndex]);
        if (currentIndex < dialogues.length) startTypewriter(dialogues[currentIndex]);
        handleSoundEffects(currentIndex);

        String currentBG = imagePaths[currentIndex];
        String prevBG = (currentIndex > 0) ? imagePaths[currentIndex - 1] : "";
        backgroundLabel.setIcon(getOptimizedImage(currentBG, 1280, 800));

        String currentP1 = (currentIndex < charPaths.length) ? charPaths[currentIndex] : "res/empty.png";
        String prevP1 = (currentIndex > 0) ? charPaths[currentIndex - 1] : "res/empty.png";
        String currentP2 = (currentIndex < charPaths2.length) ? charPaths2[currentIndex] : "res/empty.png";
        String prevP2 = (currentIndex > 0) ? charPaths2[currentIndex - 1] : "res/empty.png";

        // --- 1. ตรรกะพิเศษสำหรับฉาก s3.png หรือ s4.png (Fade กลางแล้วเลื่อนแยก) ---
        if ((currentBG.contains("s3.png") && !prevBG.contains("s3.png")) || 
            (currentBG.contains("s4.png") && !prevBG.contains("s4.png"))) {
            
            if (!currentP1.contains("empty")) {
                animateCharacterEntry(characterLabel, currentP1, 500, 900, 390, 50, 100, true);
            }
            if (!currentP2.contains("empty")) {
                animateCharacterEntry(characterLabel2, currentP2, 1050, 900, 115, 420, 70, false);
            }
        } 
        // --- 2. ตรรกะปกติ: ตัวละครโผล่ครั้งแรก (จากไม่มีรูป เป็นมีรูป) ---
        else if (prevP1.contains("empty") && !currentP1.contains("empty")) {
            int w = currentP1.contains("Mc") ? 500 : 800;
            int targetY = currentP1.contains("Uncle") ? 225 : 100;
            animateCharacterEntry(characterLabel, currentP1, w, 900, 390, 50, targetY, true);
        } 
        else if (prevP2.contains("empty") && !currentP2.contains("empty")) {
            int w = currentP2.contains("Alice") ? 1050 : 800;
            int targetY = currentP2.contains("Alice") ? 130 : 100;
            animateCharacterEntry(characterLabel2, currentP2, w, 900, 115, 420, targetY, false);
        }
        // --- 3. ตรรกะ Fade ตอนเปลี่ยนสีหน้า (Path เปลี่ยนแต่ตัวละครอยู่บนจอแล้ว) ---
        else {
            if (!currentP1.equals(prevP1) && !currentP1.contains("empty")) {
                updateCharacterLayer(characterLabel, charPaths);
                startCharacterFadeIn1();
            }
            if (!currentP2.equals(prevP2) && !currentP2.contains("empty")) {
                updateCharacterLayer(characterLabel2, charPaths2);
                startCharacterFadeIn2();
            }
        }

        // จัดการ Layer กรณีไม่มี Animation และซ่อนรูปถ้าเป็น empty
        if (!isAnimatingEntry) {
            updateCharacterLayer(characterLabel, charPaths);
            updateCharacterLayer(characterLabel2, charPaths2);
            
            // ถ้า Path เป็น empty ให้หายไปเลย (alpha = 0) 
            // แต่ถ้าไม่ใช่ ให้คงค่า Alpha เดิมไว้ (เพื่อให้ Timer ทำงานต่อได้)
            if (currentP1.contains("empty")) charAlpha1 = 0.0f;
            else if (!currentP1.equals(prevP1)) { /* ปล่อยให้ Timer จัดการ */ }
            else charAlpha1 = 1.0f;

            if (currentP2.contains("empty")) charAlpha2 = 0.0f;
            else if (!currentP2.equals(prevP2)) { /* ปล่อยให้ Timer จัดการ */ }
            else charAlpha2 = 1.0f;
        }
        layeredPane.repaint();
    }

    public void screenShake(int intensity, int duration) {
        Point originalLoc = getLocation();
        Timer shakeTimer = new Timer(20, null);
        final long startTime = System.currentTimeMillis();
        shakeTimer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            if (elapsed < duration) {
                // สุ่มตำแหน่ง X และ Y ตามความแรง (intensity)
                int x = (int) (Math.random() * intensity * 2 - intensity);
                int y = (int) (Math.random() * intensity * 2 - intensity);
                setLocation(originalLoc.x + x, originalLoc.y + y);
            } else {
                // เมื่อครบเวลา ให้กลับไปตำแหน่งเดิมและหยุด Timer
                setLocation(originalLoc);
                ((Timer) e.getSource()).stop();
            }
        });
        shakeTimer.start();
    }

    private void updateCharacterLayer(JLabel label, String[] paths) {
        if (currentIndex >= paths.length || paths[currentIndex].contains("empty")) { label.setIcon(null); return; }
        String path = paths[currentIndex];
        if (path.contains("Mc/body")) { label.setIcon(getOptimizedImage(path, 500, 900)); label.setBounds(50, 100, 600, 900); } 
        else if (path.contains("Alice") || path.contains("Girl")) { label.setIcon(getOptimizedImage(path, 1050, 700)); label.setBounds(420, 70, 1300, 900); } 
        else if (path.contains("Uncle.png")) { label.setIcon(getOptimizedImage(path, 900, 900)); label.setBounds(-100, 225, 900, 900); } 
        else if (path.contains("demon")) { label.setIcon(getOptimizedImage(path, 800, 900)); label.setBounds(50, 100, 800, 900); }
        else { label.setIcon(getOptimizedImage(path, 800, 900)); label.setBounds(420, 100, 800, 900); }
    }

    private void startCharacterFadeIn1() {
        charAlpha1 = 0.0f;
        if (charFadeTimer1 != null && charFadeTimer1.isRunning()) charFadeTimer1.stop();
        charFadeTimer1 = new Timer(30, e -> {
            charAlpha1 += 0.1f;
            if (charAlpha1 >= 1.0f) {
                charAlpha1 = 1.0f;
                ((Timer)e.getSource()).stop();
            }
            characterLabel.repaint();
        });
        charFadeTimer1.start();
    }

    private void startCharacterFadeIn2() {
        charAlpha2 = 0.0f;
        if (charFadeTimer2 != null && charFadeTimer2.isRunning()) charFadeTimer2.stop();
        charFadeTimer2 = new Timer(30, e -> {
            charAlpha2 += 0.1f;
            if (charAlpha2 >= 1.0f) {
                charAlpha2 = 1.0f;
                ((Timer)e.getSource()).stop();
            }
            characterLabel2.repaint();
        });
        charFadeTimer2.start();
    }

    // --- Scoreboard (UI เหมือน Part 3) ---
    private void setupStatusOverlay() {
        statusOverlay = new JPanel(new BorderLayout(10, 10));
        statusOverlay.setBackground(new Color(0, 0, 0, 210)); 
        statusOverlay.setBounds(440, 150, 400, 400); 
        statusOverlay.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        statusOverlay.setVisible(false);
        onlineCountLabel = new JLabel("ผู้เล่นออนไลน์: 1", SwingConstants.CENTER);
        onlineCountLabel.setForeground(Color.CYAN); onlineCountLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        JLabel titleLabel = new JLabel("--- ความสัมพันธ์ทั้งหมด ---", SwingConstants.CENTER);
        titleLabel.setForeground(Color.YELLOW); titleLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
        affinityStatusLabel = new JLabel("กำลังโหลดข้อมูล...", SwingConstants.CENTER);
        affinityStatusLabel.setForeground(Color.WHITE); affinityStatusLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        affinityStatusLabel.setVerticalAlignment(SwingConstants.TOP);
        statusOverlay.add(titleLabel, BorderLayout.NORTH); statusOverlay.add(affinityStatusLabel, BorderLayout.CENTER); statusOverlay.add(onlineCountLabel, BorderLayout.SOUTH); 
        layeredPane.add(statusOverlay, JLayeredPane.DRAG_LAYER);
    }

    // --- ป้องกันการค้าง: Null Check ในระบบเสียง ---
    private void stopBGM() { if (bgmClip != null) { if (bgmClip.isRunning()) bgmClip.stop(); bgmClip.close(); bgmClip = null; } }
    private void stopEffect() { if (effectClip != null) { if (effectClip.isRunning()) effectClip.stop(); effectClip.close(); effectClip = null; } }

    private void playSE(String path, boolean loop, float volume) {
        try {
            File soundFile = new File(path); if (!soundFile.exists()) return;
            AudioInputStream ai = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip(); clip.open(ai);
            FloatControl gc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN); gc.setValue(volume);
            if (loop || path.contains("soundtrack")) { this.bgmClip = clip; clip.loop(Clip.LOOP_CONTINUOUSLY); } else { this.effectClip = clip; }
            clip.start();
        } catch (Exception e) {}
    }

    private void handleSoundEffects(int index) {
        if (index == 11) playSE("res/sound/baka.wav", false, 5.0f);
        if (index == 12) playSE("res/sound/muuuu.wav", false, 5.0f);
        if (index == 18) playSE("res/sound/hhonto.wav", false, 5.0f);
        if (index == 19) playSE("res/sound/emmm.wav", false, 5.0f);
        if (index == 22) { stopBGM(); playSE("res/sound/soundtrack6.wav", true, -10.0f); }
        if (index == 27) { playSE("res/sound/evillaugh.wav", false, -10.0f); playSE("res/sound/housefire.wav", true, -10.0f); }
        
        // --- เพิ่ม Effect สั่นหน้าจอตรงนี้ ---
        if (index == 37) { 
            // อริสร่ายเวทย์น้ำแข็ง (สั่นนิดนึง)
            // screenShake(6, 500); 
        }
        if (index == 39) playSE("res/sound/Arigato.wav", false, 0.0f);
        
        if (index == 42) {
            // ฉันร่ายเวทย์ลม (สั่นแรงนิดหน่อย)
            screenShake(20, 1200);
        }
        // ---------------------------------

        if (index == 46) { stopBGM(); stopEffect(); playSE("res/sound/soundtrack7.wav", true, -10.0f); }
        if (index == 58) playSE("res/sound/Baka janai no.wav", false, 5.0f);
        if (index == 59) playSE("res/sound/Arigato.wav", false, 0.0f);
    }

    private void initNetwork() {
        if (!relationdata.isOnlineMode) return;
        new Thread(() -> {
            try {
                Socket socket = new Socket(relationdata.serverIP, 5000);
                networkOut = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                networkOut.println("SET_NAME:" + relationdata.playerName);
                networkOut.println("SET_PART:4");
                networkOut.println("UPDATE_AFFINITY:" + relationdata.aliceRel.getAffinity());
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("LOAD_AFFINITY:")) {
                        int score = Integer.parseInt(line.substring(14));
                        relationdata.aliceRel.setAffinity(score); SwingUtilities.invokeLater(this::updateAffinityUI);
                    } else if (line.startsWith("ALL_STATS:")) { updateLeaderboardUI(line.substring(10)); }
                }
            } catch (Exception e) {}
        }).start();
    }

    private void updateLeaderboardUI(String data) {
        StringBuilder sb = new StringBuilder("<html><body style='padding:10px;'><table width='320' style='color:white; font-family:Tahoma;'>");
        sb.append("<tr style='color:#FFD700;'><th>ผู้เล่น</th><th align='right'>คะแนน</th></tr>");
        for (String p : data.split(",")) { if (p.contains("=")) { String[] parts = p.split("="); String color = parts[0].equals(relationdata.playerName) ? "#00FF7F" : "white"; sb.append("<tr><td style='color:").append(color).append(";'>").append(parts[0]).append("</td><td align='right' style='color:#FF69B4;'>").append(parts[1]).append(" pt</td></tr>"); } }
        sb.append("</table></body></html>");
        SwingUtilities.invokeLater(() -> { affinityStatusLabel.setText(sb.toString()); onlineCountLabel.setText("ผู้เล่นออนไลน์: " + data.split(",").length); });
    }

    // --- ส่วนเสริม UI (คงเดิมแต่ปรับตำแหน่งและฟอนต์) ---
    private void setupDialogueUI() { dialoguePanel = new VisualNovelBox(); dialoguePanel.setLayout(null); dialoguePanel.setBounds(225, 520, 800, 200); layeredPane.add(dialoguePanel, JLayeredPane.MODAL_LAYER); nameLabel = new JLabel(); nameLabel.setFont(new Font("Tahoma", Font.BOLD, 26)); nameLabel.setForeground(new Color(180, 40, 90)); nameLabel.setBounds(60, 15, 300, 40); dialoguePanel.add(nameLabel); dialogueArea = new JLabel(); dialogueArea.setFont(new Font("Tahoma", Font.BOLD, 22)); dialogueArea.setForeground(new Color(45, 65, 115)); dialogueArea.setBounds(60, 65, 700, 110); dialogueArea.setVerticalAlignment(SwingConstants.TOP); dialoguePanel.add(dialogueArea); }
    private void syncOnline() { if (relationdata.isOnlineMode && networkOut != null) networkOut.println("SYNC_INDEX:" + currentIndex); }
    private void setupTabKeyBinding() { layeredPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("TAB"), "toggleTab"); layeredPane.getActionMap().put("toggleTab", new AbstractAction() { @Override public void actionPerformed(java.awt.event.ActionEvent e) { statusOverlay.setVisible(!statusOverlay.isVisible()); } }); }
    private void startTypewriter(String text) { if (typewriterTimer != null) typewriterTimer.stop(); charIndex = 0; isTyping = true; dialogueArea.setText(""); typewriterTimer = new Timer(25, e -> { if (charIndex < text.length()) { charIndex++; updateDialogueDisplay(text.substring(0, charIndex)); } else { typewriterTimer.stop(); isTyping = false; } }); typewriterTimer.start(); }
    private void updateDialogueDisplay(String text) { dialogueArea.setText("<html><body style='width: 700px;'>" + text + "</body></html>"); }
    private void setupRelationshipUI() { JPanel relPanel = new JPanel(new GridLayout(2, 1)); relPanel.setBounds(25, 25, 300, 70); relPanel.setOpaque(false); affinityLabel = new JLabel("ความสนิท: " + relationdata.aliceRel.getAffinity()); affinityLabel.setFont(new Font("Tahoma", Font.BOLD, 22)); affinityLabel.setForeground(Color.WHITE); statusLabel = new JLabel("สถานะ: " + relationdata.aliceRel.getStatus()); statusLabel.setFont(new Font("Tahoma", Font.PLAIN, 20)); statusLabel.setForeground(new Color(255, 204, 0)); relPanel.add(affinityLabel); relPanel.add(statusLabel); layeredPane.add(relPanel, JLayeredPane.POPUP_LAYER); }
    private void updateAffinityUI() { affinityLabel.setText("ความสนิท: " + relationdata.aliceRel.getAffinity()); statusLabel.setText("สถานะ: " + relationdata.aliceRel.getStatus()); }
    private void setupFadeOverlay() { fadeOverlay = new JPanel() { @Override protected void paintComponent(Graphics g) { Graphics2D g2d = (Graphics2D) g; g2d.setColor(new Color(0, 0, 0, (int) (alpha * 255))); g2d.fillRect(0, 0, getWidth(), getHeight()); } }; fadeOverlay.setBounds(0, 0, 1280, 800); fadeOverlay.setOpaque(false); }
    private void startFadeIn() { alpha = 1.0f; if (fadeOverlay.getParent() == null) layeredPane.add(fadeOverlay, JLayeredPane.DRAG_LAYER); Timer fadeTimer = new Timer(40, e -> { alpha -= 0.02f; if (alpha <= 0) { alpha = 0; ((Timer) e.getSource()).stop(); layeredPane.remove(fadeOverlay); } fadeOverlay.repaint(); }); fadeTimer.start(); }
    private void performSceneFade(Runnable onBlack) { isFading = true; alpha = 0.0f; if (fadeOverlay.getParent() == null) layeredPane.add(fadeOverlay, JLayeredPane.DRAG_LAYER); Timer fadeOut = new Timer(30, e -> { alpha += 0.1f; if (alpha >= 1.0f) { alpha = 1.0f; ((Timer)e.getSource()).stop(); onBlack.run(); new Timer(300, ev -> { ((Timer)ev.getSource()).stop(); new Timer(30, eve -> { alpha -= 0.1f; if (alpha <= 0) { alpha = 0; ((Timer)eve.getSource()).stop(); isFading = false; } fadeOverlay.repaint(); }).start(); }).start(); } fadeOverlay.repaint(); }); fadeOut.start(); }
    private ImageIcon getOptimizedImage(String path, int w, int h) { String key = path + w + h; if (!imageCache.containsKey(key)) { imageCache.put(key, scaleImage(path, w, h)); } return imageCache.get(key); }
    public ImageIcon scaleImage(String path, int width, int height) { try { return new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)); } catch (Exception e) { return null; } }
    private void showChoices(String text1, String text2, int t1, int t2) { isChoosing = true; choiceButton1 = createChoiceButton(text1, 380, t1); choiceButton2 = createChoiceButton(text2, 450, t2); layeredPane.add(choiceButton1, JLayeredPane.POPUP_LAYER); layeredPane.add(choiceButton2, JLayeredPane.POPUP_LAYER); layeredPane.repaint(); }
    private JButton createChoiceButton(String text, int y, int target) {
        JButton btn = new JButton(text) {
            // --- ตัวแปรสำหรับ Animation ---
            private double scale = 1.0;
            private int alphaMod = 180; // ความโปร่งใสของพื้นหลัง
            private Timer animTimer;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // --- ส่วนการทำ Animation Scale ---
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                g2.translate(centerX, centerY);
                g2.scale(scale, scale);
                g2.translate(-centerX, -centerY);

                // วาดพื้นหลังปุ่ม (สีขาวใสที่เปลี่ยนค่า alpha ตามการ Hover)
                g2.setColor(new Color(255, 255, 255, alphaMod));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                // วาดเส้นขอบสีชมพู
                g2.setColor(new Color(225, 105, 180));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 22, 22);

                g2.dispose();
                super.paintComponent(g);
            }

            {
                // เพิ่ม MouseListener สำหรับดักจับการ Hover และ Click
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        startAnimation(1.05, 230); // ขยายขึ้น 5% และสว่างขึ้น
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        startAnimation(1.0, 180); // กลับสู่ขนาดปกติ
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        scale = 0.95; // ปุ่มยุบลงตอนกด
                        repaint();
                    }
                });
            }

            private void startAnimation(double targetScale, int targetAlpha) {
                if (animTimer != null && animTimer.isRunning()) animTimer.stop();
                animTimer = new Timer(15, ev -> {
                    // ค่อยๆ ปรับ Scale ให้สมูท
                    if (scale < targetScale) scale += 0.01;
                    else if (scale > targetScale) scale -= 0.01;

                    // ค่อยๆ ปรับความสว่างพื้นหลัง
                    if (alphaMod < targetAlpha) alphaMod += 5;
                    else if (alphaMod > targetAlpha) alphaMod -= 5;

                    if (Math.abs(scale - targetScale) < 0.01 && alphaMod == targetAlpha) {
                        scale = targetScale;
                        ((Timer)ev.getSource()).stop();
                    }
                    repaint();
                });
                animTimer.start();
            }
        };

        // --- การตั้งค่าปุ่มพื้นฐาน ---
        btn.setBounds(800, y, 350, 60);
        btn.setFont(new Font("Tahoma", Font.BOLD, 18));
        btn.setForeground(new Color(45, 65, 115));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // --- Logic การกดปุ่มและระบบ Affinity ของ Part 4 ---
        btn.addActionListener(e -> {
            playSE("res/sound/click.wav", false, 0.0f);
            layeredPane.remove(choiceButton1);
            layeredPane.remove(choiceButton2);
            isChoosing = false;

            // ตรวจสอบเงื่อนไขคะแนนความสนิทตามที่คุณส่งมา
            if (target == 11 || target == 18 || target == 39 || target == 59) {
                relationdata.aliceRel.addAffinity(10);
            } else {
                relationdata.aliceRel.decreaseAffinity(5);
            }

            // ส่งข้อมูลไปยัง Server เพื่อบันทึก SQL (Online Mode)
            if (relationdata.isOnlineMode && networkOut != null) {
                networkOut.println("UPDATE_AFFINITY:" + relationdata.aliceRel.getAffinity());
                networkOut.println("SYNC_INDEX:" + target);
            }

            // อัปเดตการแสดงผลบน UI
            updateAffinityUI(); 

            currentIndex = target;
            updateScene();
        });

        return btn;
    }
    private void finishPart() {
        isFading = true; // ล็อคการคลิกซ้ำ
        stopBGM();
        stopEffect();
        alpha = 0.0f; // เริ่มจากใสไปดำ

        // 1. ตรวจสอบว่ามี fadeOverlay หรือไม่ ถ้าไม่มีให้เพิ่มเข้าเลเยอร์หน้าสุด
        if (fadeOverlay.getParent() == null) {
            layeredPane.add(fadeOverlay, JLayeredPane.DRAG_LAYER);
        }

        // 2. เริ่มการ Fade Out (ค่อยๆ ดำ)
        Timer fadeOut = new Timer(30, e -> {
            alpha += 0.05f; 
            if (alpha >= 1.0f) {
                alpha = 1.0f;
                ((Timer)e.getSource()).stop();

                // ปิดการเชื่อมต่อ Network ก่อนย้ายพาร์ท
                if (networkOut != null) networkOut.close();

                // 3. สลับไป Part 5 หลังจากจอดำสนิทแล้ว
                SwingUtilities.invokeLater(() -> {
                    new part5().setVisible(true); // เปิด Part 5
                    dispose(); // ปิดหน้าจอ Part 4
                });
            }
            fadeOverlay.repaint();
        });
        fadeOut.start();
    }
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new part4().setVisible(true)); }
}

/*class VisualNovelBox extends JPanel {
    private int cornerRadius = 30;
    public VisualNovelBox() { setOpaque(false); }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gradient = new GradientPaint(0, 0, new Color(245, 250, 255, 180), 0, getHeight(), new Color(255, 235, 245, 230));
        g2d.setPaint(gradient); g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        g2d.setColor(new Color(255, 150, 200, 200)); g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);
        g2d.dispose();
    }
}*/