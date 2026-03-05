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

public class part6 extends JFrame {
    private JLayeredPane layeredPane;
    private JLabel backgroundLabel, characterLabel, characterLabel2, dialogueArea, nameLabel;
    private VisualNovelBox dialoguePanel;
    private float alpha = 1.0f;
    private JPanel fadeOverlay;
    private int currentIndex = 0;
    private Clip bgmClip;      
    private Clip effectClip;
    private JButton choiceButton1, choiceButton2, choiceButton3;
    private boolean isChoosing = false;
    private Timer typewriterTimer;
    private int charIndex = 0;
    private boolean isTyping = false;
    private boolean isFading = false;
    private Map<String, ImageIcon> imageCache = new HashMap<>();
    
    private JLabel affinityLabel, statusLabel;
    private JPanel statusOverlay;
    private JLabel onlineCountLabel, affinityStatusLabel;
    private java.io.PrintWriter networkOut;

    private String lastBgPath = "";
    
    // --- ตัวแปรสำหรับจำภาพล่าสุด (ย้ายมาตรงนี้เพื่อแก้ Error static) ---
    private String lastP1 = "";
    private String lastP2 = "";
    
    // --- ระบบ Dual Character ---
    private float charAlpha1 = 0.0f; 
    private float charAlpha2 = 0.0f; 
    private Timer charFadeTimer1, charFadeTimer2;
    
    private JPanel waitOverlay;
    private boolean isWaiting = false;

    private final Font thaiFont = new Font("Tahoma", Font.BOLD, 18);

    private String[] imagePaths = {
       "res/scene6/s1new.png", "res/scene6/s1new.png", "res/scene6/s1new.png", "res/scene6/s1new.png",
       "res/scene6/s1new.png", "res/scene6/s1new.png", "res/scene6/s1new.png", "res/scene6/s1new.png",
       "res/scene6/s1new.png", "res/scene6/s1new.png", "res/scene6/s1new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", 
       "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", 
       "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", 
       "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", 
       "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", 
       "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", 
       "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", 
       "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", 
       "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", 
       "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png",
       "res/scene6/s2new.png"
    };
    
    private String[] charPaths = {
       "res/scene5/Alice-shower2.png", "res/scene5/Alice-shower2.png", "res/scene5/Alice-shower2.png", "res/scene5/Alice-shower2.png",
       "res/scene5/Alice-shower2.png", "res/scene5/Alice-shower3.png", "res/scene5/Alice-shower3.png", "res/scene5/Alice-shower1.png", 
       "res/scene5/Alice-shower1.png", "res/scene5/Alice-shower1.png", "res/scene5/Alice-shower1.png","res/empty.png", "res/empty.png", "res/empty.png",
       "res/Charactor/Alice/Girl/Alice-shy2.png", "res/Charactor/Alice/Girl/Alice-shy1.png", "res/Charactor/Alice/Girl/Alice-normal1.png", "res/Charactor/Alice/Girl/Alice-normal1.png",
       "res/Charactor/Alice/Girl/Alice-normal1.png", "res/Charactor/Alice/Girl/Alice-normal1.png", "res/empty.png", "res/empty.png",
       "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
       "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
       "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
       "res/empty.png", "res/Charactor/Alice/Girl/Alice-normal2.png", "res/Charactor/Alice/Girl/Alice-normal2.png", "res/Charactor/Alice/Girl/Alice-normal1.png", 
       "res/Charactor/Alice/Girl/Alice-shy2.png","res/Charactor/Alice/Girl/Alice-shy1.png","res/Charactor/Alice/Girl/Alice-normal1.png","res/empty.png","res/empty.png", 
       "res/empty.png", "res/empty.png", "res/empty.png", "res/Charactor/Alice/Girl/Alice-normal1.png", 
       "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png","res/empty.png", 
    };

    private String[] charPaths2 = {
       "res/empty.png", "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-showhand1.png",
       "res/Charactor/Dan/dan-showhand1.png", "res/empty.png", "res/empty.png", "res/empty.png", 
       "res/empty.png", "res/empty.png", "res/empty.png","res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png",
       "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png",
       "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png",
       "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", 
       "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png",
       "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png",
       "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/empty.png", "res/empty.png", 
       "res/empty.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png","res/Charactor/Dan/dan-normal2.png", 
       "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png",
       "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png","res/empty.png", 
    };
    
    private String[] names = { 
            "อริส", "อริส", "อริส", "คนลึกลับ", "ฉัน", "อริส", "ฉัน", "อริส", 
            "อริส", "อริส", "อริส", "ฉัน", "Dan", "Dan", "อริส", "อริส", "อริส", 
            "อริส","Dan", "Dan", "ฉัน", "ฉัน", "Dan", "Dan", "ฉัน", "ฉัน", 
            "ฉัน", "ฉัน", "ฉัน", "ฉัน", "Dan", "Dan", "Dan", "Dan", 
            "ฉัน", "อริส", "อริส", "ฉัน", "อริส","อริส","อริส", "Dan", "Dan", 
            "ฉัน", "Dan", "Dan", "ฉัน", "อริส", " ", " ", " ", " "
            };
    
    private String[] dialogues = {
    /* 0  */ "ใครอยู่ตรงนั้น!?",
    /* 1  */ "ออกมาเดี๋ยวนี้นะ!!",
    /* 2  */ "ถ้าไม่ออกมา ฉันจะใช้พลังเวทย์ใส่เเกทั้งเเบบนี้เเหละ",
    /* 3  */ "ใจเย็นก่อน ฉันไม่ได้คิดร้าย",
    /* 4  */ "อริส เกิดอะไรขึ้น!!",
    /* 5  */ "นายไม่ต้องวิ่งมาขนาดนั้นก็ได้ ฉันไม่ได้เป็นอะไรสักหน่อย",
    /* 6  */ "ก็ฉันได้ยินเสียงเธอร้องนี่",
    /* 7  */ "นายเป็นห่วงฉันขนาดนั้นเลยหรอ?",
    /* 8  */ "นะ…นายพูดอะไรแบบนั้นกัน…",//choice1
    /* 9  */ "อือ ก็จริงของนาย",//choice2
    /* 10 */ "อะงั้นหรอกหรอ",//choice3
    /* 11 */ "สรุปเเล้วนายเป็นใครกัน?",
    /* 12 */ "ฉันชื่อ Dan เป็นนักผจญภัยหนะ",
    /* 13 */ "พวกเธอสองคนเป็นคู่รักกันหรอ?",
    /* 14 */ "ดะ…เดี๋ยวสิ! ใครบอกว่าเป็นแบบนั้น!",//choice1
    /* 15 */ "นะ…นายพูดอะไรของนายเนี่ย!!",//choice2
    /* 16 */ "ก็...ตามนั้น", //choice3
    /* 17 */ "เเล้วทําไมเมื่อกี้ต้องซ่อนด้วย",
    /* 18 */ "ฉันไม่ได้จะเเอบดูเธอหรอกนะ ฉันเเค่เดินผ่านมา",
    /* 19 */ "เเล้วพวกเธอหละชื่ออะไร กําลังจะไปที่ไหนกัน?",
    /* 20 */ "ฉันชื่อ..(ชื่อตัวละครเรา) ส่วนนี่ก็ อริส",
    /* 21 */ "พวกเรากําลังจะมุ่งหน้าไปที่ป่า Death End",
    /* 22 */ "Death End หรอ..",
    /* 23 */ "มีเหตุผลอะไรที่พวกเธอต้องไปที่เเบบนั้นหรอ?",
    /* 24 */ "ช่วงนี้เริ่มมีปีศาจโจมตี ในหลายๆพื้นที่",
    /* 25 */ "เเละดูเหมือนว่าจะมีคนที่คอยสั่งเจ้าพวกนั้น",
    /* 26 */ "คนที่สามารถสั่งเจ้าพวกนั้นได้คงต้องเป็นคนที่เเข็งเเกร่งมากเเน่ๆ",
    /* 27 */ "คนเดียวที่ทําเเบบนั้นได้ คือจอมมาร",
    /* 28 */ "เพราะเเบบนั้นพวกเราเลยออกเดินทางเพื่อไปยังที่อยู่ของจอมมาร",
    /* 29 */ "เเหละจบเรื่องนี้ จะได้ไม่มีผู้คนต้องบาดเจ็บ",
    /* 30 */ "มันค่อนข้างอันตรายนะ",
    /* 31 */ "ป่า Death End เป็นป่าที่มีความซับซ้อนของเส้นทาง",
    /* 32 */ "อีกทั้งยังมีปีศาจเเละต้นไม้าอาถรรพ์ที่สามารถทําร้ายเราได้ตลอดเวลา",
    /* 33 */ "ฉันก็เคยเข้าไปครั้งนึง เเต่ก็สามารถรอดออกมาได้",
    /* 34 */ "งั้นนายช่วยมาร่วมเดินทางกับพวกเราหน่อยจะได้มั้ย",
    /* 35 */ "นี่นายเเน่ใจเเล้วหรอ?",
    /* 36 */ "หมอนั่นอาจจะเป็นคนไม่ดีก็ได้นะ",
    /* 37 */ "ไม่เป็นไรหรอก ดูเเล้วคนๆนี้ก็ไม่น่ามีพิษภัยอะไร",
    /* 38 */ "เข้าใจเเล้ว...",//choice1
    /* 39 */ "เอาตามนั้นก็ได้",//choice2
    /* 40 */ "...", //choice3
    /* 41 */ "ดูเหมือนพวกเธอจะสนิทกันดีนะ",
    /* 42 */ "ถ้าอย่างนั้น...ฉันจะนําทางพวกเธอไปที่ป่า Death End เอง",
    /* 43 */ "จริงหรอ!?",
    /* 44 */ "แต่เส้นทางมันยาวนะ พวกเธอคงต้องเตรียมตัวให้พร้อม",
    /* 45 */ "เพราะถ้าเข้าไปในป่านั้นแล้ว...จะไม่มีทางถอยกลับง่ายๆ",
    /* 46 */ "ไม่เป็นไรหรอก พวกเราตัดสินใจแล้ว",
    /* 47 */ "อือ!",
    /* 48 */ "หลังจากนั้น พวกเราเริ่มออกเดินทางไปยังป่า Death End",
    /* 49 */ "การเดินทางที่ยาวนานได้เริ่มต้นขึ้น",
    /* 50 */ "เวลาผ่านไปหลายสัปดาห์...",
    /* 51 */ "...",
};

    public part6() {
        setTitle("ISEKAI DEMO - Part 6: Hidden Shadow");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                stopAllSounds(); 
                System.exit(0);
            }
        });

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        playSE("res/sound/soundtrack12.wav", true, -10.0f);
        playEffect("res/sound/ahhhhh.wav", 0.0f);

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
        layeredPane.add(characterLabel, Integer.valueOf(100));

        characterLabel2 = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, charAlpha2)); 
                super.paintComponent(g2d); g2d.dispose();
            }
        };
        layeredPane.add(characterLabel2, Integer.valueOf(101));

        for (int i = 0; i < dialogues.length; i++) {
            if (dialogues[i].contains("(ชื่อตัวละครเรา)")) {
                dialogues[i] = dialogues[i].replace("(ชื่อตัวละครเรา)", relationdata.playerName);
            }
        }

        setupDialogueUI();
        setupRelationshipUI(); 
        setupStatusOverlay();
        setupTabKeyBinding();
        initNetwork();
        
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
        updateScene();

        layeredPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleNext();
            }
        });
    }

    private void updateScene() {
        if (currentIndex >= dialogues.length) return;

        nameLabel.setText(currentIndex < names.length ? names[currentIndex] : " ");

        if (currentIndex < imagePaths.length) {
            String currentBg = imagePaths[currentIndex];
            if (!currentBg.equals(lastBgPath)) {
                backgroundLabel.setIcon(getOptimizedImage(currentBg, 1280, 800));
                lastBgPath = currentBg; 
            }
        }

        handleSoundEffects(currentIndex);
        startTypewriter(dialogues[currentIndex]);

        String currentP1 = (currentIndex < charPaths.length) ? charPaths[currentIndex] : "res/empty.png";
        String currentP2 = (currentIndex < charPaths2.length) ? charPaths2[currentIndex] : "res/empty.png";
        String prevP1 = lastP1.isEmpty() ? "res/empty.png" : lastP1;
        String prevP2 = lastP2.isEmpty() ? "res/empty.png" : lastP2;

        boolean hasAlice = !currentP1.contains("empty");
        boolean hasDan = !currentP2.contains("empty");

        // ตรวจสอบว่าตำแหน่งตัวละครจำเป็นต้องเลื่อนหรือไม่
        boolean boundsChanged1 = false;
        boolean boundsChanged2 = false;
        
        if (hasAlice) {
            Rectangle newBounds1 = getCharacterBounds(hasAlice, hasDan, true);
            if (characterLabel.getBounds().x != newBounds1.x) boundsChanged1 = true;
        }
        if (hasDan) {
            Rectangle newBounds2 = getCharacterBounds(hasDan, hasAlice, false);
            if (characterLabel2.getBounds().x != newBounds2.x) boundsChanged2 = true;
        }

        handleTransition(characterLabel, prevP1, currentP1, true, hasDan, boundsChanged1);
        handleTransition(characterLabel2, prevP2, currentP2, false, hasAlice, boundsChanged2);

        lastP1 = currentP1;
        lastP2 = currentP2;

        layeredPane.repaint();
    }

    // ==========================================
    // --- ระบบจัดการ Transition ตัวละคร (อัปเกรด) ---
    // ==========================================
    private Rectangle getCharacterBounds(boolean isActive, boolean isOtherActive, boolean isAlice) {
        if (!isActive) return new Rectangle(0, 0, 0, 0);

        if (isAlice) {
            int x = !isOtherActive ? 40 : -300;
            return new Rectangle(x, 100, 1200, 800);
        } else {
            int x = !isOtherActive ? 100 : 300;
            return new Rectangle(x, 50, 1200, 900);
        }
    }

    private void handleTransition(JLabel label, String prev, String curr, boolean isChar1, boolean isOtherActive, boolean boundsChanged) {
        boolean wasActive = !prev.contains("empty");
        boolean isActive = !curr.contains("empty");

        if (!wasActive && !isActive) return;

        Rectangle target = getCharacterBounds(isActive, isOtherActive, isChar1);

        // 1. Fade Out
        if (wasActive && !isActive) {
            animateFadeOut(label, prev, isChar1);
        } 
        // 2. Entry (Slide + Fade)
        else if (!wasActive && isActive) {
            boolean fromLeft = isChar1; // Alice มาจากซ้าย, Dan มาจากขวา
            animateEntry(label, curr, target, fromLeft, isChar1);
        }
        // 3. Change expression OR Change position (Fade In Place)
        else if (isActive && (!prev.equals(curr) || boundsChanged)) {
            animateFadeInPlace(label, curr, target, isChar1);
        }
    }

    private void animateFadeOut(JLabel label, String path, boolean isChar1) {
        if (isChar1 && charFadeTimer1 != null) charFadeTimer1.stop();
        if (!isChar1 && charFadeTimer2 != null) charFadeTimer2.stop();

        Timer timer = new Timer(20, null);
        final long startTime = System.currentTimeMillis();
        final int duration = 400; 

        timer.addActionListener(e -> {
            float progress = Math.min(1.0f, (float) (System.currentTimeMillis() - startTime) / duration);
            float alphaVal = 1.0f - progress;
            if (isChar1) charAlpha1 = alphaVal; else charAlpha2 = alphaVal;
            label.repaint();
            if (progress >= 1.0f) {
                timer.stop();
                label.setIcon(null); 
            }
        });
        if (isChar1) charFadeTimer1 = timer; else charFadeTimer2 = timer;
        timer.start();
    }

    private void animateEntry(JLabel label, String path, Rectangle target, boolean fromLeft, boolean isChar1) {
        if (isChar1 && charFadeTimer1 != null) charFadeTimer1.stop();
        if (!isChar1 && charFadeTimer2 != null) charFadeTimer2.stop();

        if (isChar1) charAlpha1 = 0.0f; else charAlpha2 = 0.0f;
        
        int startX = fromLeft ? target.x - 60 : target.x + 60;
        label.setIcon(getOptimizedImage(path, target.width, target.height));
        label.setBounds(startX, target.y, target.width, target.height);

        Timer timer = new Timer(20, null);
        final long startTime = System.currentTimeMillis();
        final int duration = 500; 

        timer.addActionListener(e -> {
            float progress = Math.min(1.0f, (float) (System.currentTimeMillis() - startTime) / duration);
            if (isChar1) charAlpha1 = progress; else charAlpha2 = progress;
            
            int curX = (int) (startX + (target.x - startX) * progress);
            label.setBounds(curX, target.y, target.width, target.height);
            label.repaint();
            if (progress >= 1.0f) {
                timer.stop();
            }
        });
        if (isChar1) charFadeTimer1 = timer; else charFadeTimer2 = timer;
        timer.start();
    }

    private void animateFadeInPlace(JLabel label, String path, Rectangle target, boolean isChar1) {
        if (isChar1 && charFadeTimer1 != null) charFadeTimer1.stop();
        if (!isChar1 && charFadeTimer2 != null) charFadeTimer2.stop();

        if (isChar1) charAlpha1 = 0.0f; else charAlpha2 = 0.0f;
        
        label.setIcon(getOptimizedImage(path, target.width, target.height));
        label.setBounds(target.x, target.y, target.width, target.height);

        Timer timer = new Timer(20, null);
        final long startTime = System.currentTimeMillis();
        final int duration = 300; 

        timer.addActionListener(e -> {
            float progress = Math.min(1.0f, (float) (System.currentTimeMillis() - startTime) / duration);
            if (isChar1) charAlpha1 = progress; else charAlpha2 = progress;
            label.repaint();
            if (progress >= 1.0f) {
                timer.stop();
            }
        });
        if (isChar1) charFadeTimer1 = timer; else charFadeTimer2 = timer;
        timer.start();
    }
    // ==========================================

    private void setupRelationshipUI() {
        JPanel relPanel = new JPanel(new GridLayout(2, 1)); 
        relPanel.setBounds(0, 0, 280, 75); 
        relPanel.setBackground(new Color(0, 0, 0, 190)); 
        relPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 105, 180), 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 10)
        ));
        
        affinityLabel = new JLabel("อริส: " + relationdata.aliceRel.getAffinity());
        affinityLabel.setFont(thaiFont);
        affinityLabel.setForeground(new Color(255, 192, 203));
        
        statusLabel = new JLabel("สถานะ: " + relationdata.aliceRel.getStatus());
        statusLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        statusLabel.setForeground(Color.WHITE);
        
        relPanel.add(affinityLabel); 
        relPanel.add(statusLabel);
        layeredPane.add(relPanel, JLayeredPane.POPUP_LAYER);
    }

    private void setupDialogueUI() {
        dialoguePanel = new VisualNovelBox();
        dialoguePanel.setLayout(null);
        dialoguePanel.setBounds(225, 520, 800, 200);
        layeredPane.add(dialoguePanel, JLayeredPane.MODAL_LAYER);

        nameLabel = new JLabel();
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 26));
        nameLabel.setForeground(new Color(180, 40, 90));
        nameLabel.setBounds(60, 10, 300, 40);
        dialoguePanel.add(nameLabel);

        dialogueArea = new JLabel();
        dialogueArea.setFont(new Font("Tahoma", Font.BOLD, 22));
        dialogueArea.setForeground(new Color(45, 65, 115));
        dialogueArea.setVerticalAlignment(SwingConstants.TOP);
        dialogueArea.setBounds(60, 60, 800, 110);
        dialoguePanel.add(dialogueArea);

        JLabel nextArrow = new JLabel("▼");
        nextArrow.setFont(new Font("Arial", Font.BOLD, 20));
        nextArrow.setForeground(new Color(0, 153, 255));
        nextArrow.setBounds(750, 130, 30, 30);
        dialoguePanel.add(nextArrow);
        new Timer(500, ev -> nextArrow.setVisible(!nextArrow.isVisible())).start();
    }

    private void handleNext() {
    	if (isFading || isWaiting || isChoosing) return; 
        if (isTyping) {
            stopTypewriter();
            dialogueArea.setText("<html><body style='width: 750px;'>" + dialogues[currentIndex] + "</body></html>");
            return;
        }

        if (currentIndex == 7) { 
            showChoices("ก็เธอสําคัญกับฉันนี่", "ใครๆก็ต้องช่วยเพื่อนอยู่แล้ว","ฉันนึกว่ามีปีศาจมาโจมตีเลยวิ่งมาดู", 8, 9,10); 
            return; 
        }
        if (currentIndex == 8 || currentIndex == 9 || currentIndex == 10) { 
            jumpToIndex(11); 
            return; 
        }
        if (currentIndex == 13) { 
            showChoices("ตอนนี้อาจจะยังไม่ใช่เเต่อนาคตไม่เเน่", "ถ้าเธออยากเป็นก็ได้นะ","จะเป็นเเบบนั้นงั้นยังได้ไง", 14, 15, 16); 
            return; 
        }
        if (currentIndex == 14 || currentIndex == 15 || currentIndex == 16) { 
            jumpToIndex(17); 
            return; 
        }
        if (currentIndex == 35) { 
            showChoices("ถ้าเกิดอะไรขึ้น ฉันจะปกป้องเธอเอง", "เพราะงั้น ไม่ต้องห่วงหรอก", "เรื่องเเค่นี้จะกลัวทําไม",38, 39, 40); 
            return; 
        }
        if (currentIndex == 38 || currentIndex == 39 || currentIndex == 40) { 
            jumpToIndex(41); 
            return; 
        }

        int nextIndex = currentIndex + 1;
        if (nextIndex < dialogues.length) {
            currentIndex = nextIndex; syncOnline(); updateScene();
        } else {
            finishGame();
        }
    }

    private void jumpToIndex(int target) {
        currentIndex = target; syncOnline(); updateScene();
    }

    private void syncOnline() { if (relationdata.isOnlineMode && networkOut != null) networkOut.println("SYNC_INDEX:" + currentIndex); }

    private void handleSoundEffects(int index) {
        if (index == 8) playEffect("res/sound/baka.wav", 5.0f);
        if (index == 9) playEffect("res/sound/emmm.wav", 5.0f);
        if (index == 13) playEffect("res/sound/choochoto.wav", 5.0f);
        if (index == 14) playEffect("res/sound/Baka janai no.wav", 5.0f);
        if (index == 36) playEffect("res/sound/wakarunai.wav", 5.0f);
        if (index == 37) playEffect("res/sound/soredeiikedo.wav", 5.0f);
        if (index == 44) playEffect("res/sound/emmm.wav", 5.0f);
    }

    private void showChoices(String text1, String text2, String text3, int t1, int t2, int t3) {
        isChoosing = true;
        if (choiceButton1 != null) layeredPane.remove(choiceButton1);
        if (choiceButton2 != null) layeredPane.remove(choiceButton2);
        if (choiceButton3 != null) layeredPane.remove(choiceButton3);
        choiceButton1 = createChoiceButton(text1, 310, t1); 
        choiceButton2 = createChoiceButton(text2, 380, t2); 
        choiceButton3 = createChoiceButton(text3, 450, t3);
        layeredPane.add(choiceButton1, JLayeredPane.POPUP_LAYER);
        layeredPane.add(choiceButton2, JLayeredPane.POPUP_LAYER);
        layeredPane.add(choiceButton3, JLayeredPane.POPUP_LAYER);
        layeredPane.repaint();
    }

    private JButton createChoiceButton(String text, int y, int target) {
        JButton btn = new JButton(text) {
            private double scale = 1.0;
            private int alphaMod = 170; 
            private Timer animTimer;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(); int h = getHeight();
                g2.translate(w / 2, h / 2); g2.scale(scale, scale); g2.translate(-w / 2, -h / 2);
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 255, 255, alphaMod), 0, h, new Color(230, 230, 230, alphaMod));
                g2.setPaint(gp); g2.fillRoundRect(0, 0, w, h, 20, 20);
                g2.setColor(new Color(255, 102, 153)); g2.setStroke(new BasicStroke(2.0f)); g2.drawRoundRect(1, 1, w - 2, h - 2, 18, 18);
                g2.dispose(); super.paintComponent(g);
            }

            {
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { startAnim(1.03, 230); }
                    @Override public void mouseExited(MouseEvent e) { startAnim(1.0, 170); }
                });
            }

            private void startAnim(double ts, int ta) {
                if (animTimer != null) animTimer.stop();
                animTimer = new Timer(15, e -> {
                    scale += (ts - scale) * 0.2;
                    if (alphaMod < ta) alphaMod += 10; else if (alphaMod > ta) alphaMod -= 10;
                    if (Math.abs(scale - ts) < 0.001 && alphaMod == ta) ((Timer)e.getSource()).stop();
                    repaint();
                });
                animTimer.start();
            }
        };

        btn.setBounds(800, y, 380, 60); 
        btn.setFont(new Font("Tahoma", Font.BOLD, 17));
        btn.setForeground(new Color(50, 50, 50));
        btn.setFocusPainted(false); btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            playEffect("res/sound/click.wav", 0.0f);
            layeredPane.remove(choiceButton1);
            layeredPane.remove(choiceButton2);
            layeredPane.remove(choiceButton3);
            isChoosing = false; 

            if (target == 8 || target == 14 || target == 15 || target == 38) {
                relationdata.aliceRel.addAffinity(10); //คะแนนเพิ่ม 10
            } else if (target == 16 || target == 40){
                relationdata.aliceRel.decreaseAffinity(5); //คะแนนลด 5
            } else if (target == 9 || target == 10 || target == 39){ //คะแนนเท่าเดิม
            }

            if (relationdata.isOnlineMode && networkOut != null) {
                final int currentScore = relationdata.aliceRel.getAffinity();
                new Thread(() -> {
                    networkOut.println("UPDATE_AFFINITY:" + currentScore);
                    networkOut.println("SYNC_INDEX:" + target);
                }).start();
            }

            if (affinityLabel != null) {
                affinityLabel.setText("อริส: " + relationdata.aliceRel.getAffinity());
            }
            if (statusLabel != null) statusLabel.setText("สถานะ: " + relationdata.aliceRel.getStatus());
            
            jumpToIndex(target);
            currentIndex = target; 
            updateScene(); 
        });
        return btn;
    }

    private ImageIcon getOptimizedImage(String path, int w, int h) {
        String key = path + w + h;
        if (!imageCache.containsKey(key)) imageCache.put(key, scaleImage(path, w, h));
        return imageCache.get(key);
    }

    private void setupStatusOverlay() {
        statusOverlay = new JPanel(new BorderLayout());
        statusOverlay.setBounds(440, 150, 400, 400); 
        statusOverlay.setBackground(new Color(0, 0, 0, 210)); 
        statusOverlay.setVisible(false);
        
        affinityStatusLabel = new JLabel("กำลังโหลดข้อมูล Scoreboard...", SwingConstants.CENTER);
        affinityStatusLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        affinityStatusLabel.setForeground(Color.WHITE);
        statusOverlay.add(affinityStatusLabel, BorderLayout.CENTER);

        onlineCountLabel = new JLabel("ผู้เล่นออนไลน์: 0", SwingConstants.CENTER);
        onlineCountLabel.setForeground(Color.YELLOW);
        onlineCountLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        statusOverlay.add(onlineCountLabel, BorderLayout.SOUTH);

        layeredPane.add(statusOverlay, JLayeredPane.DRAG_LAYER);
    }

    private void setupTabKeyBinding() {
        layeredPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("TAB"), "toggleTab");
        layeredPane.getActionMap().put("toggleTab", new AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) { statusOverlay.setVisible(!statusOverlay.isVisible()); }
        });
    }

    private void initNetwork() {
        if (!relationdata.isOnlineMode || relationdata.globalSocket == null) return;
        new Thread(() -> {
            try {
                // *** ดึงข้อมูลจากส่วนกลาง ไม่ต้อง new Socket ใหม่ เหมือน Part 5 และ 7 ***
                networkOut = relationdata.globalOut;
                java.io.BufferedReader in = relationdata.globalIn;

                networkOut.println("SET_NAME:" + relationdata.playerName);
                networkOut.println("SET_PART:6");
                networkOut.println("GET_AFFINITY"); 

                String line;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("LOAD_AFFINITY:")) {
                        int score = Integer.parseInt(line.substring(14).trim());
                        relationdata.aliceRel.setAffinity(score);
                        SwingUtilities.invokeLater(() -> {
                            affinityLabel.setText("อริส: " + score); 
                            statusLabel.setText("สถานะ: " + relationdata.aliceRel.getStatus());
                        });
                    } else if (line.startsWith("ALL_STATS:")) {
                        updateLeaderboardUI(line.substring(10));
                    }
                    else if (line.equals("PROCEED_TO_NEXT")) {
                        goToNextPart();
                        break; // *** สำคัญ! ต้องมี break เพื่อหยุด loop การรอรับข้อมูล ***
                    }
                }
            } catch (Exception e) { e.printStackTrace(); }
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
                
                String aliceScore = rawScores;
                if (rawScores.contains("/")) {
                    aliceScore = rawScores.split("/")[0]; 
                }

                String color = name.equals(relationdata.playerName) ? "#00FF7F" : "white";
                sb.append("<tr>")
                .append("<td style='color:").append(color).append(";'>").append(name).append("</td>")
                .append("<td align='right' style='color:#FF69B4;'>").append(aliceScore).append(" pt</td>")
                .append("</tr>");
            }
        }
        sb.append("</table></body></html>");
        
        SwingUtilities.invokeLater(() -> {
            affinityStatusLabel.setText(sb.toString());
            onlineCountLabel.setText("ผู้เล่นออนไลน์: " + data.split(",").length);
        });
    }

    private void playSE(String path, boolean loop, float vol) {
        try {
            AudioInputStream ai = AudioSystem.getAudioInputStream(new File(path));
            bgmClip = AudioSystem.getClip(); bgmClip.open(ai);
            FloatControl fc = (FloatControl) bgmClip.getControl(FloatControl.Type.MASTER_GAIN);
            fc.setValue(vol); if (loop) bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            bgmClip.start();
        } catch (Exception e) {}
    }

    public void playEffect(String path, float vol) {
        try {
            AudioInputStream ai = AudioSystem.getAudioInputStream(new File(path));
            Clip c = AudioSystem.getClip(); c.open(ai);
            FloatControl fc = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
            fc.setValue(vol); c.start();
        } catch (Exception e) {}
    }

    private void stopAllSounds() { if (bgmClip != null) bgmClip.stop(); }

    private void startFadeIn() {
        Timer ft = new Timer(35, e -> {
            alpha -= 0.05f;
            if (alpha <= 0) { alpha = 0; ((Timer)e.getSource()).stop(); fadeOverlay.setVisible(false); }
            fadeOverlay.repaint();
        });
        ft.start();
    }

    private void startTypewriter(String text) {
        stopTypewriter(); isTyping = true; charIndex = 0;
        typewriterTimer = new Timer(30, e -> {
            if (charIndex < text.length()) {
                charIndex++;
                dialogueArea.setText("<html><body style='width: 750px;'>" + text.substring(0, charIndex) + "</body></html>");
            } else stopTypewriter();
        });
        typewriterTimer.start();
    }

    private void stopTypewriter() { if (typewriterTimer != null) typewriterTimer.stop(); isTyping = false; }

    private void finishGame() {
        if (isFading) return; isFading = true;
        alpha = 0.0f; fadeOverlay.setVisible(true);
        Timer fo = new Timer(20, e -> {
            alpha += 0.05f;
            if (alpha >= 1.0f) {
                ((Timer)e.getSource()).stop();
                SwingUtilities.invokeLater(() -> {
                    if (relationdata.isOnlineMode) {
                        showWaitPoint(); 
                    } else {
                        goToNextPart(); 
                    }
                });
            }
            fadeOverlay.repaint();
        });
        fo.start();
    }

    public ImageIcon scaleImage(String p, int w, int h) {
        try { return new ImageIcon(new ImageIcon(p).getImage().getScaledInstance(w, h, Image.SCALE_FAST)); } catch (Exception e) { return null; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new part6().setVisible(true));
    }
    private void showWaitPoint() {
        isWaiting = true;
        waitOverlay = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(new Color(0, 0, 0, 220)); 
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        waitOverlay.setBounds(0, 0, 1280, 800);
        waitOverlay.setOpaque(false);
        JLabel msg = new JLabel("WAITING FOR PLAYERS...", SwingConstants.CENTER);
        msg.setFont(new Font("Monospaced", Font.BOLD, 40)); 
        msg.setForeground(Color.WHITE);
        msg.setBounds(0, 350, 1280, 100);
        waitOverlay.add(msg);
        layeredPane.add(waitOverlay, JLayeredPane.DRAG_LAYER);
        layeredPane.moveToFront(waitOverlay);
        if (networkOut != null) networkOut.println("READY_FOR_NEXT");
        revalidate(); repaint();
    }

    private void goToNextPart() {
        SwingUtilities.invokeLater(() -> {
            new part7().setVisible(true); 
            dispose(); 
        });
    }
}