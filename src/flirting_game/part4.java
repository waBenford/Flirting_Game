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
    private JLayeredPane layeredPane;
    private JLabel backgroundLabel, characterLabel, characterLabel2, dialogueArea, nameLabel;
    private float alpha = 1.0f;
    private JPanel fadeOverlay;
    private VisualNovelBox dialoguePanel; 
    private int currentIndex = 0;
    private Clip bgmClip;      
    private Clip effectClip;   
    private JButton choiceButton1, choiceButton2, choiceButton3, choiceButton4;
    private boolean isChoosing = false;
    private boolean isFinishing = false;
    private Timer typewriterTimer;
    private int charIndex = 0;
    private boolean isTyping = false;
    private boolean isFading = false;
    private Map<String, ImageIcon> imageCache = new HashMap<>();

    private float charAlpha1 = 0.0f; 
    private float charAlpha2 = 0.0f; 
    private Timer charFadeTimer1, charFadeTimer2;
    private boolean isAnimatingEntry = false;
    
    private JLabel affinityLabel, statusLabel;
    private JPanel statusOverlay;
    private JLabel onlineCountLabel, affinityStatusLabel;
    
    private JPanel waitOverlay;
    private boolean isWaiting = false;

    private PrintWriter networkOut;

    private String[] imagePaths = {
        "res/scene4/s1.png", "res/scene4/s2.png", "res/scene4/s2.png", "res/scene4/s2.png",
        "res/scene4/s2.png", "res/scene4/s2.png", "res/scene4/s2.png", "res/scene4/s2.png", 
        "res/scene4/s2.png", "res/scene4/s2.png", "res/scene4/s2.png", "res/scene4/s2.png",
        "res/scene4/s2.png", "res/scene4/s2.png", "res/scene4/s2.png", "res/scene4/s3.png", 
        "res/scene4/s3.png", "res/scene4/s3.png", "res/scene4/s3.png", "res/scene4/s3.png", 
        "res/scene4/s3.png", "res/scene4/s3.png", "res/scene4/s3.png", "res/scene4/s3.png",
        "res/scene4/s4.1.png","res/scene4/s4.png", "res/scene4/s4.png", "res/scene4/s4.png", 
        "res/scene4/s4.png", "res/scene4/s4.png", "res/scene4/s4.png", 
        "res/scene4/s5.png", "res/scene4/s5.png", "res/scene4/s5.png", "res/scene4/s5.png", 
        "res/scene4/s5.png", "res/scene4/s5.png", "res/scene4/s5.png", "res/scene4/s5.png", 
        "res/scene4/s5.png", "res/scene4/s5.png", "res/scene4/s6.png", "res/scene4/s6.png", 
        "res/scene4/s6.png", "res/scene4/s6.png", "res/scene4/s6.png", "res/scene4/s6.png", 
        "res/scene4/s6.png", "res/scene4/s6.png", "res/scene4/s6.png", "res/scene4/s7.png", 
        "res/scene4/s7.png", "res/scene4/s7.png", "res/scene4/s7.png", "res/scene4/s7.png", 
        "res/scene4/s7.png", "res/scene4/s7.png", "res/scene4/s7.png", "res/scene4/s7.png", 
        "res/scene4/s3.png", "res/scene4/s3.png", "res/scene4/s3.png", "res/scene4/s3.png", 
        "res/scene4/s3.png", "res/scene4/s3.png","res/scene4/s3.png", "res/scene4/s3.png", "res/scene4/s3.png"
    };
    
    private String[] charPaths = {
        "res/empty.png", "res/Charactor/Mc/body1.PNG", "res/Charactor/Mc/body1.PNG", 
        "res/Charactor/Mc/body1.PNG", "res/Charactor/Mc/body2.PNG", "res/Charactor/Mc/body1.PNG", 
        "res/Charactor/Mc/body2.PNG", "res/Charactor/Mc/body1.PNG", "res/Charactor/Mc/body1.PNG",
        "res/Charactor/Mc/body1.PNG", "res/Charactor/Mc/body1.PNG", "res/Charactor/Mc/body1.PNG",
        "res/Charactor/Mc/body1.PNG", "res/Charactor/Mc/body1.PNG", "res/Charactor/Mc/body1.PNG",
        "res/Charactor/Mc/body1.PNG", "res/Charactor/Mc/body1.PNG", "res/Charactor/Mc/body1.PNG", 
        "res/Charactor/Mc/body1.PNG", "res/Charactor/Mc/body1.PNG", "res/Charactor/Mc/body1.PNG","res/Charactor/Mc/body1.PNG",
        "res/Charactor/Mc/body1.PNG","res/Charactor/Mc/body1.PNG", "res/empty.png",  
        "res/Charactor/factor/Uncle.png", "res/Charactor/factor/Uncle.png", "res/Charactor/factor/Uncle.png", 
        "res/Charactor/factor/Uncle.png", "res/Charactor/factor/Uncle.png", "res/Charactor/factor/Uncle.png", 
        "res/Charactor/factor/demon1.PNG", "res/Charactor/factor/demon1.PNG", "res/Charactor/factor/demon1.PNG",
        "res/Charactor/factor/demon1.PNG", "res/Charactor/factor/demon1.PNG", "res/Charactor/factor/demon1.PNG",
        "res/Charactor/factor/demon1.PNG", "res/Charactor/factor/demon1.PNG", "res/Charactor/factor/demon1.PNG",
        "res/Charactor/factor/demon1.PNG", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/Charactor/factor/demon2.PNG", 
        "res/Charactor/factor/demon2.png", "res/Charactor/factor/demon2.png", "res/Charactor/factor/demon2.png", 
        "res/Charactor/factor/demon2.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png","res/empty.png", "res/empty.png", "res/empty.png"
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
        "res/Charactor/Alice/Girl/Alice-normal1.png", "res/Charactor/Alice/Girl/Alice-normal2.png",
        "res/Charactor/Alice/Girl/Alice-shy2.png", "res/Charactor/Alice/Girl/Alice-shy1.png", 
        "res/Charactor/Alice/Girl/Alice-shy1.png", "res/Charactor/Alice/Girl/Alice-shy2.png",
        "res/Charactor/Alice/Girl/Alice-shy1.png", "res/empty.png",
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
        "res/Charactor/Alice/Girl/Alice-normal2.png","res/Charactor/Alice/Girl/Alice-normal1.png",
        "res/Charactor/Alice/Girl/Alice-normal2.png","res/Charactor/Alice/Girl/Alice-normal1.png",
        "res/Charactor/Alice/Girl/Alice-normal2.png","res/Charactor/Alice/Girl/Alice-shy2.png",
        "res/Charactor/Alice/Girl/Alice-normal1.png",
    };

    private String[] names = {
        " ", "ฉัน", "ฉัน", "อริส", "ฉัน", "อริส", "ฉัน", "อริส", 
        "อริส", "อริส", "อริส", "อริส", "อริส", "อริส","อริส","อริส",
        "ฉัน","อริส","ฉัน","อริส","อริส","อริส","อริส","อริส", 
        " ", "อริส","ชาวบ้าน","ชาวบ้าน","อริส","อริส","ฉัน","ปีศาจ",
        "ปีศาจ","ฉัน", "ปีศาจ", "ฉัน", "ปีศาจ", "ปีศาจ","อริส","อริส","ปีศาจ", 
        "อริส", " ", "อริส"," ", " ", "อริส", "ฉัน", "ปีศาจ",
        "ปีศาจ", "ปีศาจ","ฉัน", "ฉัน", "ปีศาจ","ปีศาจ","ปีศาจ","อริส",
        "ฉัน", "อริส", "อริส", "ฉัน", "ฉัน", "อริส", "อริส", "อริส", 
        "อริส", "ฉัน",
    };
    
    private String[] dialogues = {
        "เวลาผ่าน 2 ปี", "นี่ก็ผ่านไป 2 ปีแล้ว หลังจากที่ฉันได้มาอยู่ในโลกนี้", 
        "ตอนนี้ฉันก็น่าจะแข็งแกร่งขึ้นบ้างละหละ", "(ชื่อตัวละครเรา) ข้าวเที่ยงเสร็จละนะ", 
        "โอเค กำลังจะไปเดี๋ยวนี้แหละ", "..กำลังยืนดูตัวเรา..", "นี่เธอแอบดูกล้ามฉันรึเปล่า?", 
        "เปล่าซะหน่อย ใครมันจะไปดูกัน", "ช่างเรื่องนั้นเถอะ", "นี่..นายคิดว่าชุดนี้เหมาะกับฉันมั้ย?", 
        "มันเป็นชุดสำหรับเดินทางหนะ", 
        "น่ารักอะไรกัน..บ้าจริง", 
        "โถ่ว..นี่นายจะไม่ชมฉันเลยบ้างรึไง", 
        "ชิ!! งั้นข้าวมื้อนี้ไม่ต้องกิน!!",
        "งะ..งั้นหรอ..",
        "เมื่อกี้นายกำลังฝึกหรอ?", "อือ..ก็นิดหน่อยอะ", "รีบกินสิเดี๋ยวมันจะเย็นเอานะ", 
        "(กำลังกิน)", "เป็นไงอร่อยมั้ย?", 
        "จะ..จริงหรอ..งั้นก็กินเยอะๆเลยนะ", 
        "อือๆก็ดีแล้ว", 
        "ถ้าชอบฉันก็ดีใจ",
        "…",
        "นี่!!เปิดประตูหน่อย!!", "เกิดอะไรขึ้นหรอคะ?", 
        "เอ่อ..คือว่า..มันมีปีศาจมาบุกโจมตีหมู่บ้าน", "มีชาวบ้านหลายคนที่ได้รับบาดเจ็บ เเต่ส่วนใหญ่ก็หนีออกมาได้", 
        "แย่ละสิ! ต้องรีบไปจัดการเเล้ว!", "ไปกันเถอะ (ชื่อตัวละครเรา)", "โอเค!!", 
        "ไม่มีพวกเก่งๆเลยรึไง ฮ่าๆ", "มีเเต่ชาวบ้านกระจอกๆแบบนี้ ก็ไม่สนุกนะเส้", 
        "นี่แกกำลังทำอะไร!!", "ก็กำลังเล่นสนุกอยู่ไงละ ฮ่าๆ", "เล่นสนุกอย่างงั้นหรอ?", 
        "พวกแกมันก็ไม่ต่างอะไรจากหนอนเเถลง!!", "ชีวิตของพวกแกก็มีไว้ให้พวกข้าสนุกเท่านั้น", 
        "เลวที่สุด..", "ฉันจะไม่ให้อภัยพวกแกเด็ดขาด!! ", "เเน่จริงก็เข้ามา!!", 
        "เวทย์น้ำแข็ง Ice shot!!","ปีศาจหลบได้ เเละกำลังจะโจมตี อริส", 
        "ขอบคุณที่ช่วยนะ (ชื่อตัวละครเรา)", 
        "อริสหลบการโจมตีได้",
        "อริสหลบได้เเต่โดนโจมตีเล็กน้อย",
        "เวทย์น้ำแข็ง Ice floor", "รับไปซะ! เวทย์ลม wind storm", "เอ่อ..พลังเวทย์ขนาดนี้..มันเป็นใครกันนะ!?", 
        "อ้ากกกก!!", "ข้าแพ้หรอเนี่ย", "ดูเหมือนแกจะประเมินตัวเองไว้สูงเลยสินะ", 
        "เอาหละ..ใครเป็นคนส่งแกมา", "แกรู้ไปจะได้อะไรขึ้นมา", "อย่างพวกแก ไม่มีทางชนะท่านผู้นั้นได้หรอก", 
        "ท่านจอมมารผู้นั้นหนะ..", "จอมมารหรอ?", "อริส เธอรู้เรื่องจอมมารคนนั้นบ้างรึเปล่า?", 
        "ฉันเคยได้ยินว่ามีจอมมารคนนึงที่อยู่ลึกสุดของป่า death end", "เเต่จอมมารคนนั้นดูเหมือนจะเป็น คนที่รักความสงบสุขมาก", 
        "ฟังดูแล้วไม่มีเหตุที่จอมมารคนนั้นจะทำเรื่องแบบนี้เลย", "อริส ฉันว่ามันถึงเวลาที่เราต้องออกเดินทางเเล้วหละ", 
        "เเล้วเราจะไปที่ไหนกันหรอ?", "ไปเดทอะไรบ้ารึเปล่า อร๊ายยยย", "ขอบคุณนะ", 
        "อะ..อื้อ..", "ไม่งั้นอาจจะมีผู้คนต้องตายไปมากกว่านี้",
    };

    public part4() {
        setTitle("ISEKAI DEMO - Part 4");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

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
        if (isChoosing || isFading || isWaiting || isAnimatingEntry || isFinishing) return;

        if (isTyping) { 
            if(typewriterTimer != null) typewriterTimer.stop(); 
            isTyping = false; 
            if (currentIndex < dialogues.length) {
                updateDialogueDisplay(dialogues[currentIndex]); 
            }
            return; 
        }

        if (currentIndex == 10) { 
            showChoices("น่ารักมากๆเลย เหมาะกับเธอสุดๆ", "ก็พอได้นะ", "ก็งั้นๆอะ", "ฉันชอบชุดนี้นะ", 11, 12, 13, 14); 
            return; 
        }
        if (currentIndex == 19) { 
            showChoices("ฉันชอบอาหารฝีมือเธอที่สุดเลย", "ก็อร่อยดีนะ", "อร่อยมากๆเลย", "ก็พอกินได้", 20, 21, 22, 23); 
            return; 
        }
        if (currentIndex == 42) { 
            showChoices("พุ่งเข้าไปปกป้องอริส", "บอกให้อริสหลบเอง", "ไม่ทำอะไรเลย", null, 43, 44, 45, -1); 
            return; 
        }
        if (currentIndex == 62) { 
            showChoices("เราจะไปเดทกันไงละจ๊ะ อริสจัง", "ที่อยู่ของจอมมารยังไงหละ", "ยังจะถามอีก ก็ไปล่าจอมมารไงละ", null, 63, 64, 65, -1); 
            return; 
        }

        int nextIdx;
        if (currentIndex >= 11 && currentIndex <= 14) nextIdx = 15;
        else if (currentIndex >= 20 && currentIndex <= 23) nextIdx = 24;
        else if (currentIndex >= 43 && currentIndex <= 45) nextIdx = 46;
        else if (currentIndex >= 63 && currentIndex <= 65) nextIdx = 66;
        else nextIdx = currentIndex + 1;

        if (nextIdx < dialogues.length) {
            if (nextIdx < imagePaths.length && !imagePaths[currentIndex].equals(imagePaths[nextIdx])) {
                final int target = nextIdx;
                performSceneFade(() -> { 
                    currentIndex = target; 
                    syncOnline(); 
                    updateScene(); 
                });
            } else { 
                currentIndex = nextIdx; 
                syncOnline(); 
                updateScene(); 
            }
        } else { 
            finishPart(); 
        }
    }
//
    private Rectangle getCharacterSettings(String path) {
        String p = path.toLowerCase();
        if (p.contains("uncle") || p.contains("ชาวบ้าน")) {
            // ลองปรับ Y (200) ให้ลุงสูงหรือต่ำตามต้องการ แต่ค่านี้ต้องนิ่ง
            return new Rectangle(-100, 200, 900, 900); 
        } else if (p.contains("mc/body")) {
            return new Rectangle(50, 100, 500, 900);
        } else if (p.contains("alice") || p.contains("girl")) {
            return new Rectangle(420, 100, 1050, 700);
        } else if (p.contains("demon")) {
            return new Rectangle(50, 50, 800, 900);
        }
        return new Rectangle(50, 100, 800, 900); // ค่าเริ่มต้น
    }
    
    private void animateCharacterEntry(JLabel label, String path, int startX, int endX, boolean isChar1) {
        isAnimatingEntry = true;
        Rectangle settings = getCharacterSettings(path); // ดึงค่า X, Y, W, H มาตรฐาน

        // รีเซ็ตค่า Alpha เป็น 0 ทันทีที่เริ่ม
        if (isChar1) charAlpha1 = 0.0f; else charAlpha2 = 0.0f;

        label.setIcon(getOptimizedImage(path, settings.width, settings.height));
        label.setBounds(startX, settings.y, settings.width, settings.height);

        // ใช้ Timer เดียวคุมทั้ง Fade และ Slide ให้เกิดพร้อมกัน
        Timer animTimer = new Timer(20, null);
        final long startTime = System.currentTimeMillis();
        final int duration = 600; // ความเร็ว 0.6 วินาที

        animTimer.addActionListener(e -> {
            float progress = Math.min(1.0f, (float) (System.currentTimeMillis() - startTime) / duration);
            
            // 1. อัปเดต Alpha (Fade In)
            if (isChar1) charAlpha1 = progress; else charAlpha2 = progress;
            
            // 2. อัปเดตตำแหน่ง X (Slide)
            int curX = (int) (startX + (endX - startX) * progress);
            label.setBounds(curX, settings.y, settings.width, settings.height);
            
            label.repaint();

            if (progress >= 1.0f) {
                animTimer.stop();
                isAnimatingEntry = false;
            }
        });
        animTimer.start();
    }

    private void updateScene() {
        if (currentIndex < names.length) nameLabel.setText(names[currentIndex]);
        if (currentIndex < dialogues.length) startTypewriter(dialogues[currentIndex]);
        handleSoundEffects(currentIndex);

        String currentBG = imagePaths[currentIndex];
        backgroundLabel.setIcon(getOptimizedImage(currentBG, 1280, 800));

        String currentP1 = (currentIndex < charPaths.length) ? charPaths[currentIndex] : "res/empty.png";
        String prevP1 = (currentIndex > 0) ? charPaths[currentIndex - 1] : "res/empty.png";
        String currentP2 = (currentIndex < charPaths2.length) ? charPaths2[currentIndex] : "res/empty.png";
        String prevP2 = (currentIndex > 0) ? charPaths2[currentIndex - 1] : "res/empty.png";

        // จัดการตัวละครที่ 1 (MC, ลุง, ปีศาจ)
        handleCharacterTransition(characterLabel, prevP1, currentP1, true);

        // จัดการตัวละครที่ 2 (อริส)
        handleCharacterTransition(characterLabel2, prevP2, currentP2, false);

        layeredPane.repaint();
    }

    private void updateCharacterLayer(JLabel label, String[] paths) {
        if (isAnimatingEntry || currentIndex >= paths.length || paths[currentIndex].contains("empty")) {
            if (!isAnimatingEntry && (currentIndex >= paths.length || paths[currentIndex].contains("empty"))) {
                label.setIcon(null);
            }
            return;
        }
        String path = paths[currentIndex];
        Rectangle s = getCharacterSettings(path);
        label.setIcon(getOptimizedImage(path, s.width, s.height));
        label.setBounds(s.x, s.y, s.width, s.height);
    }

    public void screenShake(int intensity, int duration) {
        Point originalLoc = getLocation();
        Timer shakeTimer = new Timer(20, null);
        final long startTime = System.currentTimeMillis();
        shakeTimer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            if (elapsed < duration) {
                int x = (int) (Math.random() * intensity * 2 - intensity);
                int y = (int) (Math.random() * intensity * 2 - intensity);
                setLocation(originalLoc.x + x, originalLoc.y + y);
            } else {
                setLocation(originalLoc);
                ((Timer) e.getSource()).stop();
            }
        });
        shakeTimer.start();
    }

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

    private void stopBGM() { if (bgmClip != null) { if (bgmClip.isRunning()) bgmClip.stop(); bgmClip.close(); bgmClip = null; } }
    private void stopEffect() { if (effectClip != null) { if (effectClip.isRunning()) effectClip.stop(); effectClip.close(); effectClip = null; } }

    private void handleSoundEffects(int index) {
        if (index == 11) playSE("res/sound/baka.wav", false, 5.0f);
        if (index == 12) playSE("res/sound/muuuu.wav", false, 5.0f);
        if (index == 18) playSE("res/sound/hhonto.wav", false, 5.0f);
        if (index == 19) playSE("res/sound/emmm.wav", false, 5.0f);
        if (index == 22) { stopBGM(); playSE("res/sound/soundtrack6.wav", true, -10.0f); }
        if (index == 27) { playSE("res/sound/evillaugh.wav", false, -10.0f); playSE("res/sound/housefire.wav", true, -10.0f); }
        if (index == 42) screenShake(20, 1200);
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
                        relationdata.aliceRel.setAffinity(score);
                        SwingUtilities.invokeLater(() -> {
                            affinityLabel.setText("อริส: " + score); 
                            statusLabel.setText("สถานะ: " + relationdata.aliceRel.getStatus());
                        });
                    } else if (line.startsWith("ALL_STATS:")) { updateLeaderboardUI(line.substring(10)); }
                    if (line.equals("PROCEED_TO_NEXT")) goToNextPart();
                }
            } catch (Exception e) {}
        }).start();
    }

    private void updateLeaderboardUI(String data) {
        StringBuilder sb = new StringBuilder("<html><body style='padding:10px;'>");
        sb.append("<table width='320' style='color:white; font-family:Tahoma;'>");
        sb.append("<tr style='color:#FFD700;'><th>ผู้เล่น</th><th align='right'>คะแนน (อริส)</th></tr>");
        for (String p : data.split(",")) {
            if (p.contains("=")) {
                String[] parts = p.split("=");
                String name = parts[0];
                String rawScores = parts[1];
                String aliceScore = rawScores.contains("/") ? rawScores.split("/")[0] : rawScores;
                String color = name.equals(relationdata.playerName) ? "#00FF7F" : "white";
                sb.append("<tr><td style='color:").append(color).append(";'>").append(name).append("</td>")
                .append("<td align='right' style='color:#FF69B4;'>").append(aliceScore).append(" pt</td></tr>");
            }
        }
        sb.append("</table></body></html>");
        SwingUtilities.invokeLater(() -> {
            affinityStatusLabel.setText(sb.toString());
            onlineCountLabel.setText("ผู้เล่นออนไลน์: " + data.split(",").length);
        });
    }

    private void setupDialogueUI() { 
        dialoguePanel = new VisualNovelBox(); 
        dialoguePanel.setLayout(null); 
        dialoguePanel.setBounds(225, 520, 800, 200); 
        layeredPane.add(dialoguePanel, JLayeredPane.MODAL_LAYER); 
        nameLabel = new JLabel(); 
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 26)); 
        nameLabel.setForeground(new Color(180, 40, 90)); 
        nameLabel.setBounds(60, 15, 300, 40); 
        dialoguePanel.add(nameLabel); 
        dialogueArea = new JLabel(); 
        dialogueArea.setFont(new Font("Tahoma", Font.BOLD, 22)); 
        dialogueArea.setForeground(new Color(45, 65, 115)); 
        dialogueArea.setBounds(60, 65, 700, 110); 
        dialogueArea.setVerticalAlignment(SwingConstants.TOP); 
        dialoguePanel.add(dialogueArea); 
    }

    private void setupRelationshipUI() {
        JPanel relPanel = new JPanel(new GridLayout(2, 1, 0, 0)); 
        relPanel.setBounds(0, 0, 280, 75); 
        relPanel.setBackground(new Color(0, 0, 0, 190)); 
        relPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 105, 180), 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 10)
        ));
        affinityLabel = new JLabel("อริส: " + relationdata.aliceRel.getAffinity());
        affinityLabel.setFont(new Font("Tahoma", Font.BOLD, 18)); 
        affinityLabel.setForeground(new Color(255, 192, 203));
        statusLabel = new JLabel("สถานะ: " + relationdata.aliceRel.getStatus());
        statusLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        statusLabel.setForeground(Color.WHITE);
        relPanel.add(affinityLabel);
        relPanel.add(statusLabel);
        layeredPane.add(relPanel, JLayeredPane.POPUP_LAYER);
    }

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

    private void syncOnline() { if (relationdata.isOnlineMode && networkOut != null) networkOut.println("SYNC_INDEX:" + currentIndex); }
    private void setupTabKeyBinding() { layeredPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("TAB"), "toggleTab"); layeredPane.getActionMap().put("toggleTab", new AbstractAction() { @Override public void actionPerformed(java.awt.event.ActionEvent e) { statusOverlay.setVisible(!statusOverlay.isVisible()); } }); }
    private void startTypewriter(String text) { if (typewriterTimer != null) typewriterTimer.stop(); charIndex = 0; isTyping = true; dialogueArea.setText(""); typewriterTimer = new Timer(25, e -> { if (charIndex < text.length()) { charIndex++; updateDialogueDisplay(text.substring(0, charIndex)); } else { typewriterTimer.stop(); isTyping = false; } }); typewriterTimer.start(); }
    private void updateDialogueDisplay(String text) { dialogueArea.setText("<html><body style='width: 700px;'>" + text + "</body></html>"); }
    private void setupFadeOverlay() { fadeOverlay = new JPanel() { @Override protected void paintComponent(Graphics g) { Graphics2D g2d = (Graphics2D) g; g2d.setColor(new Color(0, 0, 0, (int) (alpha * 255))); g2d.fillRect(0, 0, getWidth(), getHeight()); } }; fadeOverlay.setBounds(0, 0, 1280, 800); fadeOverlay.setOpaque(false); }
    private void startFadeIn() { alpha = 1.0f; if (fadeOverlay.getParent() == null) layeredPane.add(fadeOverlay, JLayeredPane.DRAG_LAYER); Timer fadeTimer = new Timer(40, e -> { alpha -= 0.02f; if (alpha <= 0) { alpha = 0; ((Timer) e.getSource()).stop(); layeredPane.remove(fadeOverlay); } fadeOverlay.repaint(); }); fadeTimer.start(); }
    private void performSceneFade(Runnable onBlack) { isFading = true; alpha = 0.0f; if (fadeOverlay.getParent() == null) layeredPane.add(fadeOverlay, JLayeredPane.DRAG_LAYER); Timer fadeOut = new Timer(30, e -> { alpha += 0.1f; if (alpha >= 1.0f) { alpha = 1.0f; ((Timer)e.getSource()).stop(); onBlack.run(); new Timer(300, ev -> { ((Timer)ev.getSource()).stop(); new Timer(30, eve -> { alpha -= 0.1f; if (alpha <= 0) { alpha = 0; ((Timer)eve.getSource()).stop(); isFading = false; } fadeOverlay.repaint(); }).start(); }).start(); } fadeOverlay.repaint(); }); fadeOut.start(); }
    private ImageIcon getOptimizedImage(String path, int w, int h) { String key = path + w + h; if (!imageCache.containsKey(key)) { imageCache.put(key, scaleImage(path, w, h)); } return imageCache.get(key); }
    public ImageIcon scaleImage(String path, int width, int height) { try { return new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)); } catch (Exception e) { return null; } }
    
    private void showChoices(String text1, String text2, String text3, String text4, int t1, int t2, int t3, int t4) {
        isChoosing = true;
        if (choiceButton1 != null) layeredPane.remove(choiceButton1);
        if (choiceButton2 != null) layeredPane.remove(choiceButton2);
        if (choiceButton3 != null) layeredPane.remove(choiceButton3);
        if (choiceButton4 != null) layeredPane.remove(choiceButton4);
        int startY = (text4 == null || text4.isEmpty()) ? 300 : 250; 
        int gap = 70;
        choiceButton1 = createChoiceButton(text1, startY, t1); 
        choiceButton2 = createChoiceButton(text2, startY + gap, t2);
        choiceButton3 = createChoiceButton(text3, startY + (gap * 2), t3);
        layeredPane.add(choiceButton1, JLayeredPane.POPUP_LAYER);
        layeredPane.add(choiceButton2, JLayeredPane.POPUP_LAYER);
        layeredPane.add(choiceButton3, JLayeredPane.POPUP_LAYER);
        if (text4 != null && !text4.isEmpty() && t4 != -1) {
            choiceButton4 = createChoiceButton(text4, startY + (gap * 3), t4);
            layeredPane.add(choiceButton4, JLayeredPane.POPUP_LAYER);
        }
        layeredPane.revalidate(); layeredPane.repaint();
    }

    private JButton createChoiceButton(String text, int y, int target) {
        JButton btn = new JButton(text) {
            private double scale = 1.0;
            private int alphaMod = 180;
            private Timer animTimer;
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int centerX = getWidth() / 2; int centerY = getHeight() / 2;
                g2.translate(centerX, centerY); g2.scale(scale, scale); g2.translate(-centerX, -centerY);
                g2.setColor(new Color(255, 255, 255, alphaMod));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.setColor(new Color(225, 105, 180)); g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 22, 22);
                g2.dispose(); super.paintComponent(g);
            }
            {
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { startAnimation(1.05, 230); }
                    @Override public void mouseExited(MouseEvent e) { startAnimation(1.0, 180); }
                });
            }
            private void startAnimation(double tS, int tA) {
                if (animTimer != null) animTimer.stop();
                animTimer = new Timer(15, ev -> {
                    if (scale < tS) scale += 0.01; else if (scale > tS) scale -= 0.01;
                    if (alphaMod < tA) alphaMod += 5; else if (alphaMod > tA) alphaMod -= 5;
                    if (Math.abs(scale - tS) < 0.01 && alphaMod == tA) { scale = tS; ((Timer)ev.getSource()).stop(); }
                    repaint();
                });
                animTimer.start();
            }
        };
        btn.setBounds(800, y, 350, 60);
        btn.setFont(new Font("Tahoma", Font.BOLD, 18));
        btn.setForeground(new Color(45, 65, 115));
        btn.setContentAreaFilled(false); btn.setBorderPainted(false); btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> {
            layeredPane.remove(choiceButton1); layeredPane.remove(choiceButton2); layeredPane.remove(choiceButton3);
            if (choiceButton4 != null) layeredPane.remove(choiceButton4);
            isChoosing = false; currentIndex = target; updateScene();
            if (target == 11 || target == 20 || target == 43 || target == 64) relationdata.aliceRel.addAffinity(10);
            else if (target == 14 || target == 22 || target == 65) relationdata.aliceRel.addAffinity(5);
            else if (target == 13 || target == 23 || target == 44 || target == 45 || target == 63) relationdata.aliceRel.decreaseAffinity(5);
            if (relationdata.isOnlineMode && networkOut != null) {
                networkOut.println("UPDATE_AFFINITY:" + relationdata.aliceRel.getAffinity());
                networkOut.println("SYNC_INDEX:" + target);
            }
            affinityLabel.setText("อริส: " + relationdata.aliceRel.getAffinity());
            statusLabel.setText("สถานะ: " + relationdata.aliceRel.getStatus());
            layeredPane.revalidate(); layeredPane.repaint();
        });
        return btn;
    }

    private void handleCharacterTransition(JLabel label, String prevPath, String currPath, boolean isChar1) {
        // 1. กรณีตัวละครเดิมหายไป (Fade Out)
        if (!prevPath.contains("empty") && currPath.contains("empty")) {
            animateFade(label, prevPath, false, isChar1);
        } 
        // 2. กรณีตัวละครใหม่ปรากฏตัวครั้งแรก (Slide + Fade In)
        else if (prevPath.contains("empty") && !currPath.contains("empty")) {
            Rectangle s = getCharacterSettings(currPath);
            animateCharacterEntry(label, currPath, s.x + 50, s.x, isChar1);
        } 
        // 3. กรณีตัวละครอยู่บนจออยู่แล้ว แต่เปลี่ยนท่าทาง/อารมณ์ (Fade In ใหม่)
        else if (!currPath.contains("empty") && !prevPath.equals(currPath)) {
            Rectangle s = getCharacterSettings(currPath);
            label.setIcon(getOptimizedImage(currPath, s.width, s.height));
            label.setBounds(s.x, s.y, s.width, s.height);
            
            // เพิ่มการเรียก Fade In ตรงนี้เพื่อให้เหมือน Part 3
            animateFade(label, currPath, true, isChar1);
        }
    }

    private void finishPart() {
        if (isFinishing) return;
        isFinishing = true; isFading = true; stopBGM(); stopEffect();
        alpha = 0.0f;
        if (fadeOverlay.getParent() == null) layeredPane.add(fadeOverlay, JLayeredPane.DRAG_LAYER);
        Timer fadeOut = new Timer(30, e -> {
            alpha += 0.05f; 
            if (alpha >= 1.0f) {
                alpha = 1.0f; ((Timer)e.getSource()).stop();
                SwingUtilities.invokeLater(() -> {
                    if (relationdata.isOnlineMode) showWaitPoint(); else goToNextPart();
                });
            }
            fadeOverlay.repaint();
        });
        fadeOut.start();
    }

    private void animateFade(JLabel label, String path, boolean fadeIn, boolean isChar1) {
        // ถ้าเป็นการ Fade In ให้เริ่มจาก 0 (โปร่งใส)
        if (fadeIn) {
            if (isChar1) charAlpha1 = 0.0f; else charAlpha2 = 0.0f;
        }

        Timer fadeTimer = new Timer(20, null);
        final long startTime = System.currentTimeMillis();
        final int duration = 300; // ปรับความเร็วตามต้องการ (Part 3 ใช้ประมาณนี้)

        fadeTimer.addActionListener(e -> {
            float progress = Math.min(1.0f, (float) (System.currentTimeMillis() - startTime) / duration);
            float currentAlpha = fadeIn ? progress : (1.0f - progress);

            if (isChar1) charAlpha1 = currentAlpha; else charAlpha2 = currentAlpha;
            
            label.repaint();

            if (progress >= 1.0f) {
                fadeTimer.stop();
                if (!fadeIn) {
                    label.setIcon(null);
                }
            }
        });
        fadeTimer.start();
    }

    private void showWaitPoint() {
        isWaiting = true;
        waitOverlay = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) { g.setColor(new Color(0, 0, 0, 220)); g.fillRect(0, 0, getWidth(), getHeight()); }
        };
        waitOverlay.setBounds(0, 0, 1280, 800); waitOverlay.setOpaque(false);
        JLabel msg = new JLabel("WAITING FOR PLAYERS...", SwingConstants.CENTER);
        msg.setFont(new Font("Monospaced", Font.BOLD, 40)); msg.setForeground(Color.WHITE);
        msg.setBounds(0, 350, 1280, 100); waitOverlay.add(msg);
        layeredPane.add(waitOverlay, JLayeredPane.DRAG_LAYER);
        if (networkOut != null) networkOut.println("READY_FOR_NEXT");
        revalidate(); repaint();
    }

    private void goToNextPart() {
        SwingUtilities.invokeLater(() -> {
            new part5().setVisible(true); dispose(); 
        });
    }

    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new part4().setVisible(true)); }
}

class VisualNovelBox extends JPanel {
    @Override protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gp = new GradientPaint(0, 0, new Color(245, 250, 255, 180), 0, getHeight(), new Color(255, 235, 245, 230));
        g2d.setPaint(gp); g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        g2d.setColor(new Color(255, 150, 200, 200)); g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 30, 30);
        g2d.dispose();
    }
}